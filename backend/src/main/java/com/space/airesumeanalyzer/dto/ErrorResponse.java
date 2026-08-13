package com.space.airesumeanalyzer.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String errorCode;
    private final String errorMessage;

    @Builder
    public ErrorResponse(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}