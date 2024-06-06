package com.sojoo.StoreSpotter.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.Cookie;


@NoArgsConstructor
public class TokenDto {
    private Cookie accessToken;
    private Cookie refreshToken;

}