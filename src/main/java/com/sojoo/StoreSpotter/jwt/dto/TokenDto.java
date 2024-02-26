package com.sojoo.StoreSpotter.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.Cookie;

@Data
@Builder
//@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {
//    String token;
    private Cookie accessToken;
    private Cookie refreshToken;

    public TokenDto(Cookie accessToken, Cookie refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}