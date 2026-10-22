package com.example.miniwallet.controller;

import com.example.miniwallet.dto.*;
import com.example.miniwallet.service.WalletService;
import com.example.miniwallet.util.LoggerUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private static final Logger logger = LoggerFactory.getLogger(WalletController.class);
    private static final String CLASS_NAME = WalletController.class.getSimpleName();
    private final WalletService walletService;
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(@Valid @RequestBody CreateWalletRequest request) {
        final String API_NAME = "/api/wallets";
        LoggerUtil.logEntryController(logger, API_NAME, CLASS_NAME);
        try {
            WalletResponse response = walletService.createWallet(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } finally {
            LoggerUtil.logExitController(logger, API_NAME, CLASS_NAME);
        }
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<WalletResponse> getWallet(@PathVariable Long walletId) {
        final String API_NAME = "/{walletId}";
        LoggerUtil.logEntryController(logger, API_NAME, CLASS_NAME);
        try {
            WalletResponse response = walletService.getWallet(walletId);
            return ResponseEntity.ok(response);
        } finally {
            LoggerUtil.logExitController(logger, API_NAME, CLASS_NAME);
        }
    }

    @PostMapping("/{walletId}/deposit")
    public ResponseEntity<DepositResponse> deposit(@PathVariable Long walletId, @Valid @RequestBody DepositRequest request) {
        final String API_NAME = "/{walletId}/deposit";
        LoggerUtil.logEntryController(logger, API_NAME, CLASS_NAME);
        try {
            DepositResponse response = walletService.deposit(walletId, request);
            return ResponseEntity.ok(response);
        } finally {
            LoggerUtil.logExitController(logger, API_NAME, CLASS_NAME);
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest request) {
        final String API_NAME = "/transfer";
        LoggerUtil.logEntryController(logger, API_NAME, CLASS_NAME);
        try {
            TransferResponse response = walletService.transfer(request);
            return ResponseEntity.ok(response);
        } finally {
            LoggerUtil.logExitController(logger, API_NAME, CLASS_NAME);
        }
    }

    @GetMapping("/{walletId}/transactions")
    public ResponseEntity<List<WalletTransactionResponse>> getWalletTransactions(@PathVariable Long walletId) {
        final String API_NAME = "/{walletId}/transactions";
        LoggerUtil.logEntryController(logger, API_NAME, CLASS_NAME);

        try {
            List<WalletTransactionResponse> response = walletService.getWalletTransactions(walletId);
            return ResponseEntity.ok(response);
        } finally {
            LoggerUtil.logExitController(logger, API_NAME, CLASS_NAME);
        }
    }
}
