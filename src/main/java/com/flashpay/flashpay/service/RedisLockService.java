package com.flashpay.flashpay.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisLockService {

    private final StringRedisTemplate redisTemplate;

    public boolean tryLock(String key, long timeoutMillis) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, "lock", timeoutMillis, TimeUnit.MILLISECONDS);
        return Boolean.TRUE.equals(success);
    }

    public void releaseLock(String key) {
        redisTemplate.delete(key);
    }
}