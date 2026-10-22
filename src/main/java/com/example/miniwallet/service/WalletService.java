package com.example.miniwallet.service;

import com.example.miniwallet.dto.*;

import java.util.List;

public interface WalletService {
    WalletResponse createWallet(CreateWalletRequest request);
    WalletResponse getWallet(Long WalletId);
    DepositResponse deposit(Long walletId, DepositRequest request);
    TransferResponse transfer(TransferRequest request);
    List<WalletTransactionResponse> getWalletTransactions(Long walletId);
}
