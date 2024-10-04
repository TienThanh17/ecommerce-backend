package com.bamito.exception;

import lombok.Getter;

@Getter
public class CustomizedException extends RuntimeException {
    ErrorCode errorCode;

    public CustomizedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
