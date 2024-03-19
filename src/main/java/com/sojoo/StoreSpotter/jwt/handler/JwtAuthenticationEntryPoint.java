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
    // 유저 정보 없이 접근한 경우 : SC_UNAUTHORIZED (401) 응답

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
        String exception = (String) request.getAttribute("exception");

        JwtErrorCode errorCode;

        log.debug("log: exception: {} ", exception);


        /**
         * 토큰 만료된 경우
         */
        if(exception.equals(JwtErrorCode.EXPIRED_TOKEN.getCode())) {
            errorCode = JwtErrorCode.EXPIRED_TOKEN;
            setResponse(response, errorCode);
            return;
        }

        /**
         * 토큰 시그니처가 다른 경우
         */
        if(exception.equals(JwtErrorCode.INVALID_TOKEN.getCode())) {
            errorCode = JwtErrorCode.INVALID_TOKEN;
            setResponse(response, errorCode);
        }

        /**
         * 인증이 없는 사용자일 경우
         */
        if(exception.equals(JwtErrorCode.ACCESS_DENIED.getCode())) {
            errorCode = JwtErrorCode.ACCESS_DENIED;
            setResponse(response, errorCode);
        }
    }

    /**
     * 한글 출력을 위해 getWriter() 사용
     */
    private void setResponse(HttpServletResponse response, JwtErrorCode errorCode) throws IOException {
//        response.setContentType("application/json;charset=UTF-8");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().println("{ \"message\" : \"" + errorCode.getMessage()
//                + "\", \"code\" : \"" +  errorCode.getCode()
//                + "\", \"status\" : " + errorCode.getMessage()
//                + ", \"errors\" : [ ] }");
        response.sendRedirect("/");
    }

}