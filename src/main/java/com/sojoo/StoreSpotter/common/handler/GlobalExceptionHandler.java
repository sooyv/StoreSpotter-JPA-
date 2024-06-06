package com.sojoo.StoreSpotter.common.handler;

import com.sojoo.StoreSpotter.common.error.ErrorResponse;
import com.sojoo.StoreSpotter.common.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailDuplicateException.class)
    public ModelAndView handleEmailDuplicateException(EmailDuplicateException ex) {
        log.error("handleEmailDuplicateException", ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ModelAndView("redirect:/");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        log.error("handleUserNotFoundException", ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(SmtpSendFailedException.class)
    public ResponseEntity<ErrorResponse> handleSmtpSendFailedException(SmtpSendFailedException ex) {
        log.error("handleApiDataNotFoundException", ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(DataRecommendNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleDataRecommendNotFoundException(DataRecommendNotFoundException ex) {
        log.error("handleDataRecommendNotFoundException", ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(ApiDataNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleApiDataNotFoundException(ApiDataNotFoundException ex) {
        log.info("handleApiDataNotFoundException", ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(DataPairCreateFailedException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleDataPairCreateFailedException(DataPairCreateFailedException ex) {
        log.info("handleDataPairCreateFailedException", ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

}