package com.example.miniwallet.exception;

import com.example.miniwallet.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleWalletNotFound(WalletNotFoundException exception) {
        logger.warn("Wallet not found: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorResponse("WALLET_NOT_FOUND", exception.getMessage()));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateEmail(DuplicateEmailException exception) {
        logger.warn("Duplicate email: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiErrorResponse("DUPLICATE_EMAIL", exception.getMessage()));
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ApiErrorResponse> handleInsufficientBalance(InsufficientBalanceException exception) {
        logger.warn("Insufficient balance: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse("INSUFFICIENT_BALANCE", exception.getMessage()));
    }

    @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidTransaction(InvalidTransactionException exception) {
        logger.warn("Invalid transaction: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse("INVALID_TRANSACTION", exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream().findFirst().map(error -> error.getDefaultMessage()).orElse("Invalid request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse("VALIDATION_ERROR", message));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        logger.warn("Database constraint violation occurred");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiErrorResponse("DATA_CONFLICT", "The request conflicts with existing database data"));
    }

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<ApiErrorResponse> handleTransactionException(TransactionException exception) {
        logger.error("Transaction failure occurred", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse("TRANSACTION_FAILED", "The transaction could not be completed"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException(Exception exception, HttpServletRequest request) {
        logger.error("Unexpected error while processing {}", request.getRequestURI(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected error occurred"));
    }
}
