package com.sojoo.StoreSpotter.common.exception;

import com.sojoo.StoreSpotter.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class ApiDataNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public ApiDataNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
