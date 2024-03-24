package com.sojoo.StoreSpotter.common.handler;

import com.sojoo.StoreSpotter.common.error.ErrorResponse;
import com.sojoo.StoreSpotter.common.exception.EmailDuplicateException;
import com.sojoo.StoreSpotter.common.exception.SmtpSendFailedException;
import com.sojoo.StoreSpotter.common.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(EmailDuplicateException.class)
//    public ResponseEntity<ErrorResponse> handleEmailDuplicateException(EmailDuplicateException ex) {
//        log.error("handleEmailDuplicateException", ex);
//        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
//        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
//    }
    @ExceptionHandler(EmailDuplicateException.class)
    public ModelAndView handleEmailDuplicateException(EmailDuplicateException ex) {
        log.error("handleEmailDuplicateException", ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ModelAndView("redirect:/");
    }

//    @ExceptionHandler(UserNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
//        log.error("handleUserNotFoundException", ex);
//        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
//        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
//    }
    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleUserNotFoundException(UserNotFoundException ex) {
        log.error("handleUserNotFoundException", ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ModelAndView("redirect:/signup");
    }

    @ExceptionHandler(SmtpSendFailedException.class)
    public ResponseEntity<ErrorResponse> handleSmtpSendFailedException(SmtpSendFailedException ex) {
        log.error("handleApiDataNotFoundException", ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }


}