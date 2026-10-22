package com.example.miniwallet.service;

import com.example.miniwallet.constant.TransactionStatus;
import com.example.miniwallet.constant.TransactionType;
import com.example.miniwallet.dto.*;
import com.example.miniwallet.entity.Wallet;
import com.example.miniwallet.entity.WalletTransaction;
import com.example.miniwallet.exception.InsufficientBalanceException;
import com.example.miniwallet.exception.InvalidTransactionException;
import com.example.miniwallet.exception.WalletNotFoundException;
import com.example.miniwallet.repository.WalletRepository;
import com.example.miniwallet.repository.WalletTransactionRepository;
import com.example.miniwallet.util.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {

    private static final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);
    private static final String CLASS_NAME = WalletServiceImpl.class.getSimpleName();
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    public WalletServiceImpl(WalletRepository walletRepository, WalletTransactionRepository walletTransactionRepository) {
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }

    @Override
    public WalletResponse createWallet(CreateWalletRequest request) {

        final String METHOD_NAME = "createWallet";
        LoggerUtil.logEntry(logger, METHOD_NAME, CLASS_NAME);
        try {
            if (walletRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("A wallet with this email already exists");
            }
            Wallet wallet = new Wallet();
            wallet.setName(request.getName());
            wallet.setEmail(request.getEmail());
            wallet.setBalance(request.getInitialBalance());
            Wallet savedWallet = walletRepository.save(wallet);
            return new WalletResponse(
                    savedWallet.getId(),
                    savedWallet.getName(),
                    savedWallet.getEmail(),
                    savedWallet.getBalance()
            );
        } catch (RuntimeException exception) {
            LoggerUtil.logExitWithException(logger, METHOD_NAME, CLASS_NAME, exception);
            throw exception;
        } finally {
            LoggerUtil.logExit(logger, METHOD_NAME, CLASS_NAME);
        }
    }

    @Override
    public WalletResponse getWallet(Long walletId) {
        final String METHOD_NAME = "getWallet";
        LoggerUtil.logEntry(logger, METHOD_NAME, CLASS_NAME);

        try {
            Wallet wallet = walletRepository.findById(walletId).orElseThrow(() ->
                    new WalletNotFoundException("Wallet not found with ID: " + walletId));

            return new WalletResponse(wallet.getId(), wallet.getName(), wallet.getEmail(), wallet.getBalance());
        } catch (RuntimeException exception) {
            LoggerUtil.logExitWithException(logger, METHOD_NAME, CLASS_NAME, exception);
            throw exception;
        } finally {
            LoggerUtil.logExit(logger, METHOD_NAME, CLASS_NAME);
        }
    }

    @Override
    @Transactional
    public DepositResponse deposit(Long walletId, DepositRequest request) {
        final String METHOD_NAME = "deposit";
        LoggerUtil.logEntry(logger, METHOD_NAME, CLASS_NAME);

        try {
            Wallet wallet = walletRepository.findByIdForUpdate(walletId).orElseThrow(() ->
                    new WalletNotFoundException("Wallet not found with ID: " + walletId));
            BigDecimal previousBalance = wallet.getBalance();
            BigDecimal amount = request.getAmount();

            wallet.setBalance(previousBalance.add(amount));
            walletRepository.save(wallet);

            WalletTransaction transaction = new WalletTransaction();
            transaction.setWallet(wallet);
            transaction.setType(TransactionType.DEPOSIT);
            transaction.setAmount(amount);
            transaction.setStatus(TransactionStatus.SUCCESS);
            walletTransactionRepository.save(transaction);

            return new DepositResponse(wallet.getId(), previousBalance, amount, wallet.getBalance());
        } catch (RuntimeException exception) {
            LoggerUtil.logExitWithException(logger, METHOD_NAME, CLASS_NAME, exception);
            throw exception;
        } finally {
            LoggerUtil.logExit(logger, METHOD_NAME, CLASS_NAME);
        }
    }

    @Override
    @Transactional
    public TransferResponse transfer(TransferRequest request) {
        final String METHOD_NAME = "transfer";
        LoggerUtil.logEntry(logger, METHOD_NAME, CLASS_NAME);

        try {
            Long fromWalletId = request.getFromWalletId();
            Long toWalletId = request.getToWalletId();
            BigDecimal amount = request.getAmount();

            if (fromWalletId.equals(toWalletId)) {
                throw new InvalidTransactionException("Cannot transfer money to the same wallet");
            }

            Long firstId = Math.min(fromWalletId, toWalletId);
            Long secondId = Math.max(fromWalletId, toWalletId);

            Wallet firstWallet = walletRepository.findByIdForUpdate(firstId).orElseThrow(() ->
                    new WalletNotFoundException("Wallet not found with ID: " + firstId));
            Wallet secondWallet = walletRepository.findByIdForUpdate(secondId).orElseThrow(() ->
                    new WalletNotFoundException("Wallet not found with ID: " + secondId));

            Wallet fromWallet = fromWalletId.equals(firstId) ? firstWallet : secondWallet;
            Wallet toWallet = toWalletId.equals(firstId) ? firstWallet : secondWallet;

            if (fromWallet.getBalance().compareTo(amount) < 0) {
                throw new InsufficientBalanceException("Insufficient balance to complete the transfer");
            }

            fromWallet.setBalance(fromWallet.getBalance().subtract(amount));
            toWallet.setBalance(toWallet.getBalance().add(amount));

            String referenceId = UUID.randomUUID().toString();

            WalletTransaction outgoingTransaction = new WalletTransaction();
            outgoingTransaction.setWallet(fromWallet);
            outgoingTransaction.setType(TransactionType.TRANSFER_OUT);
            outgoingTransaction.setAmount(amount);
            outgoingTransaction.setStatus(TransactionStatus.SUCCESS);
            outgoingTransaction.setReferenceId(referenceId);

            WalletTransaction savedTransaction = walletTransactionRepository.save(outgoingTransaction);

            WalletTransaction incomingTransaction = new WalletTransaction();
            incomingTransaction.setWallet(toWallet);
            incomingTransaction.setType(TransactionType.TRANSFER_IN);
            incomingTransaction.setAmount(amount);
            incomingTransaction.setStatus(TransactionStatus.SUCCESS);
            incomingTransaction.setReferenceId(referenceId);

            walletTransactionRepository.save(incomingTransaction);
            walletRepository.save(fromWallet);
            walletRepository.save(toWallet);

            return new TransferResponse(savedTransaction.getId(), fromWalletId, toWalletId, amount, TransactionStatus.SUCCESS.name());
        } catch (RuntimeException exception) {
            LoggerUtil.logExitWithException(logger, METHOD_NAME, CLASS_NAME, exception);
            throw exception;
        } finally {
            LoggerUtil.logExit(logger, METHOD_NAME, CLASS_NAME);
        }
    }

    @Override
    public List<WalletTransactionResponse> getWalletTransactions(Long walletId) {
        final String METHOD_NAME = "getWalletTransactions";
        LoggerUtil.logEntry(logger, METHOD_NAME, CLASS_NAME);

        try {
            if (!walletRepository.existsById(walletId)) {
                throw new WalletNotFoundException("Wallet not found with ID: " + walletId);
            }

            List<WalletTransaction> transactions = walletTransactionRepository.findByWalletIdOrderByCreatedAtDesc(walletId);

            return transactions.stream()
                    .map(transaction -> new WalletTransactionResponse(
                            transaction.getId(),
                            transaction.getType().name(),
                            transaction.getAmount(),
                            transaction.getStatus().name(),
                            transaction.getCreatedAt()
                    ))
                    .toList();

        } catch (RuntimeException exception) {
            LoggerUtil.logExitWithException(logger, METHOD_NAME, CLASS_NAME, exception);
            throw exception;
        } finally {
            LoggerUtil.logExit(logger, METHOD_NAME, CLASS_NAME);
        }
    }
}