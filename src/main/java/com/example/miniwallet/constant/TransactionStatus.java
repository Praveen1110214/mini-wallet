package com.example.miniwallet.constant;

public enum TransactionStatus implements EnumInterface{

    SUCCESS(1, "Success", "Transaction completed successfully"),
    FAILED(2, "Failed", "Transaction failed");

    private final Integer id;
    private final String name;
    private final String description;

    TransactionStatus(Integer id, String name, String description) {
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
