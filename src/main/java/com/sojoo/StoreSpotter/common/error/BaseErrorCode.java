package com.sojoo.StoreSpotter.common.error;

// today
public interface BaseErrorCode {
    public ErrorResponse getErrorResponse();

    String getExplainError() throws NoSuchFieldException;
}
