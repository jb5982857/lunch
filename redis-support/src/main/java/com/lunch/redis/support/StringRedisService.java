package com.lunch.redis.support;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class StringRedisService<T> extends BaseRedisService<T> {
    @Resource
    private ValueOperations<String, T> valueOperations;

    @Override
    public void put(String key, T doamin, long expire) {
        valueOperations.set(key, doamin);
        if (expire != -1) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void put(String key, T domain) {
        this.put(key, domain, -1);
    }

    @Override
    public T get(String key) {
        return valueOperations.get(key);
    }

    @Override
    public Set<String> getKeys() {
        return redisTemplate.keys("*");
    }

    @Override
    public boolean isKeyExists(String key) {
        return redisTemplate.hasKey(key);
    }
}
