package com.sojoo.StoreSpotter.controller.jwt;

import com.sojoo.StoreSpotter.dto.jwt.CreateAccessTokenRequest;
import com.sojoo.StoreSpotter.dto.jwt.CreateAccessTokenResponse;
import com.sojoo.StoreSpotter.service.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final JwtTokenService jwtTokenService;

    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = jwtTokenService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }
}
