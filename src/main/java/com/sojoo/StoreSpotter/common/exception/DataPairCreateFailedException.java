package com.sojoo.StoreSpotter.common.exception;

import com.sojoo.StoreSpotter.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class DataPairCreateFailedException extends RuntimeException {
    private final ErrorCode errorCode;

    public DataPairCreateFailedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
