package com.sojoo.StoreSpotter.jwt.jwtService;

import com.sojoo.StoreSpotter.jwt.RefreshToken;
import com.sojoo.StoreSpotter.jwt.jwtRepository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        System.out.println("RefreshTokenService - findByRefreshToken 동작");
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
