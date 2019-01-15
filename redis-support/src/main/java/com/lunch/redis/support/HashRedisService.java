package com.lunch.redis.support;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public abstract class HashRedisService<T> extends BaseRedisService<T> {
    @Resource
    private HashOperations<String, String, T> hashOperations;

    /**
     * 存入redis中的key
     *
     * @return
     */
    protected abstract String getRedisKey();

    @Override
    public void put(String key, T doamin, long expire) {
        hashOperations.put(getRedisKey(), key, doamin);
        if (expire != -1) {
            redisTemplate.expire(getRedisKey(), expire, TimeUnit.SECONDS);
        }
    }

    @Override
    public T get(String key) {
        return hashOperations.get(getRedisKey(), key);
    }

    @Override
    public Set<String> getKeys() {
        return hashOperations.keys(getRedisKey());
    }

    @Override
    public boolean isKeyExists(String key) {
        return hashOperations.hasKey(getRedisKey(), key);
    }

    @Override
    public List<T> getAll() {
        return hashOperations.values(getRedisKey());
    }

    @Override
    public long count() {
        return hashOperations.size(getRedisKey());
    }

    @Override
    public void empty() {
        Set<String> set = hashOperations.keys(getRedisKey());
        set.stream().forEach(key -> hashOperations.delete(getRedisKey(), key));
    }
}
