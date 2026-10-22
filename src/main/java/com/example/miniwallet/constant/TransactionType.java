package com.example.miniwallet.constant;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TransactionType implements EnumInterface {

    DEPOSIT(1, "Deposit", "Money deposited into a wallet"),
    TRANSFER_IN(2, "Transfer In", "Money received from another wallet"),
    TRANSFER_OUT(3, "Transfer Out", "Money transferred to another wallet");

    private final Integer id;
    private final String name;
    private final String description;

    TransactionType(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
