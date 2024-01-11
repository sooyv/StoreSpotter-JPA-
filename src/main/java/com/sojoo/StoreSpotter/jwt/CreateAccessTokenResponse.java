package com.sojoo.StoreSpotter.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class CreateAccessTokenResponse {
    private String accessToken;
}
