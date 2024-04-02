package com.sojoo.StoreSpotter.jwt.handler;

import com.sojoo.StoreSpotter.jwt.exception.JwtErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String exception = (String) request.getAttribute("exception");

        JwtErrorCode errorCode;

        if(exception.equals(JwtErrorCode.EXPIRED_TOKEN.getCode())) {
            errorCode = JwtErrorCode.EXPIRED_TOKEN;
            setResponse(response, errorCode);
            return;
        }

        if(exception.equals(JwtErrorCode.INVALID_TOKEN.getCode())) {
            errorCode = JwtErrorCode.INVALID_TOKEN;
            setResponse(response, errorCode);
        }

        if(exception.equals(JwtErrorCode.ACCESS_DENIED.getCode())) {
            errorCode = JwtErrorCode.ACCESS_DENIED;
            setResponse(response, errorCode);
        }
    }

    private void setResponse(HttpServletResponse response, JwtErrorCode errorCode) throws IOException {
        response.sendRedirect("/");
    }

}