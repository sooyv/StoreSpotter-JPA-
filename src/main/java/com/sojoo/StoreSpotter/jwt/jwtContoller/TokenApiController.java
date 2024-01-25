package com.sojoo.StoreSpotter.jwt.jwtContoller;

import com.sojoo.StoreSpotter.jwt.CreateAccessTokenRequest;
import com.sojoo.StoreSpotter.jwt.CreateAccessTokenResponse;
import com.sojoo.StoreSpotter.jwt.jwtService.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final JwtTokenService jwtTokenService;

    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = jwtTokenService.createNewAccessToken(request.getRefreshToken());
        System.out.println("createNewAccessToken" +  newAccessToken);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }
}
