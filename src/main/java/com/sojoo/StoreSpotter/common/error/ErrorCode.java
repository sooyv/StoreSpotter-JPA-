package com.sojoo.StoreSpotter.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // 400
    EMAIL_DUPLICATION(400,"USER-DUPLICATE-400","EMAIL DUPLICATED"),

    // 401
    USER_NOT_FOUND(401, "USER-NOT-FOUND-401", "USER NOT FOUND");

    private final int status;
    private final String errorCode;
    private final String message;
}