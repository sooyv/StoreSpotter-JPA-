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

    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    // 키-벨류 설정
    public void setValues(String username, String refreshToken) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();

        values.set(username, refreshToken, Duration.ofDays(14));    // refresh token - 14일
    }

    // 키값으로 벨류 가져오기
    public String getValues(String username) {

        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(username);
    }

    public void changeValues(String username, String refreshToken) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();

        values.set(username, refreshToken);
    }

    // 키-벨류 삭제
    public void delValues(String username) {

        redisTemplate.delete(username.substring(7));
    }



}
