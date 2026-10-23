package com.example.miniwallet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiErrorResponse {
    private String errorCode;
    private String message;
}
