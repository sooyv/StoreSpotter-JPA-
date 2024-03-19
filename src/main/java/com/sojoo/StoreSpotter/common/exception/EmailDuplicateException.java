package com.sojoo.StoreSpotter.common.exception;

import com.sojoo.StoreSpotter.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class EmailDuplicateException extends RuntimeException{

    private final ErrorCode errorCode;

    public EmailDuplicateException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}