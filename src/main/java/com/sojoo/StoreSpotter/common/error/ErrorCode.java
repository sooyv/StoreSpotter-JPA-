package com.sojoo.StoreSpotter.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // 400
    EMAIL_DUPLICATION(400,"USER-DUPLICATE-400","EMAIL DUPLICATED"),

    // 500
    USER_NOT_FOUND(500, "USER-NOT-FOUND-500", "USER NOT FOUND"),

    //500
    SMTP_SEND_FAILED(421, "SMTP-SEND-FAILED-421", "SMTP_SEND_FAILED");

    private final int status;
    private final String errorCode;
    private final String message;
}