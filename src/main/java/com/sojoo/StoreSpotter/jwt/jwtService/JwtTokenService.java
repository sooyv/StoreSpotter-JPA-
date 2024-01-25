package com.sojoo.StoreSpotter.jwt.jwtService;

import com.sojoo.StoreSpotter.jwt.config.JwtTokenProvider;
import com.sojoo.StoreSpotter.entity.Member.Member;
import com.sojoo.StoreSpotter.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class JwtTokenService {
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final MemberService memberService;


    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if (!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        // 유효한 토큰일때 리프레시 토큰으로 사용자 id 찾기
        Long memberId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        // 사용자 id를 찾은 뒤, generateToken() 메서드로 새로운 액세스 토큰 생성
        Member member = memberService.findById(memberId);
        return tokenProvider.generateToken(member, Duration.ofHours(2));
    }
}
