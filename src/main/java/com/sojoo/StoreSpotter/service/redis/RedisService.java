package com.sojoo.StoreSpotter.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public void setValues(String username, String value, long expirationTime, TimeUnit timeUnit) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(username, value, expirationTime, timeUnit);
    }

    public String getValues(String username) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(username);
    }

    public void changeValues(String username, String refreshToken) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(username, refreshToken);
    }

    public void delValues(String username) {
        redisTemplate.delete(username);
    }

}
