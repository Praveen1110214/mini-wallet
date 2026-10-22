package com.example.miniwallet.repository;

import com.example.miniwallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    boolean existsByEmail(String email);
}
