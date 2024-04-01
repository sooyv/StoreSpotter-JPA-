package com.sojoo.StoreSpotter.jwt.dto;

import lombok.NoArgsConstructor;

import javax.servlet.http.Cookie;

@NoArgsConstructor
public class TokenDto {
    private Cookie accessToken;
    private Cookie refreshToken;

    public TokenDto(Cookie accessToken, Cookie refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}