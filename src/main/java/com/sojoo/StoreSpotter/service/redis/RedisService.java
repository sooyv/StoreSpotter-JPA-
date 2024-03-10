package com.sojoo.StoreSpotter.service.redis;

import com.sojoo.StoreSpotter.jwt.jwt.TokenProvider;
import io.netty.handler.ssl.util.SimpleTrustManagerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final TokenProvider tokenProvider;

    public RedisService(RedisTemplate<String, String> redisTemplate, TokenProvider tokenProvider) {
        this.redisTemplate = redisTemplate;
        this.tokenProvider = tokenProvider;
    }


    // 키-벨류 설정
    public void setValues(String accessToken, String refreshToken) {
        String username = tokenProvider.getUsernameFromToken(accessToken);
        ValueOperations<String, String> values = redisTemplate.opsForValue();

        values.set(username, refreshToken, Duration.ofDays(14));    // refresh token - 14일
    }

    // 키값으로 벨류 가져오기
    public String getValues(String accessToken) {
        String username = tokenProvider.getUsernameFromToken(accessToken);

        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(username);
    }

//    public void changeValues(String accessToken, String refreshToken) {
//        String username = tokenProvider.getUsernameFromToken(accessToken);
//        ValueOperations<String, String> values = redisTemplate.opsForValue();
//
//        values.set(username, refreshToken);
//    }

    // 키-벨류 삭제
    public void delValues(String accessToken) {
        String username = tokenProvider.getUsernameFromToken(accessToken);

        redisTemplate.delete(username.substring(7));
    }



}
