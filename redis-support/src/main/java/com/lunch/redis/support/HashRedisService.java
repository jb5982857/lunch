package com.lunch.redis.support;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class HashRedisService<T> extends BaseRedisService<T> {
    public static final String DEFAULT_KEY = "hash_key";

    public static final String TEMPLATE = "lunch_hash_key:";
    @Resource
    private HashOperations<String, String, T> hashOperations;

    /**
     * 存入redis中的key
     *
     * @return
     */
    protected String getRedisKey() {
        return DEFAULT_KEY;
    }

    @Override
    public void put(String hKey, T domain, long expire) {
        hashOperations.put(TEMPLATE + getRedisKey(), hKey, domain);
        if (expire != -1) {
            redisTemplate.expire(getRedisKey(), expire, TimeUnit.SECONDS);
        }
    }

    public void put(String key, String hKey, T domain, long expire) {
        hashOperations.put(TEMPLATE + key, hKey, domain);
        if (expire != -1) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void put(String key, String hKey, T domain) {
        put(key, hKey, domain, -1);
    }

    @Override
    public T get(String hKey) {
        return hashOperations.get(TEMPLATE + getRedisKey(), hKey);
    }

    public T get(String key, String hKey) {
        return hashOperations.get(TEMPLATE + key, hKey);
    }

    @Override
    public Set<String> getKeys() {
        return hashOperations.keys(getRedisKey());
    }

    public Set<String> getKeys(String key) {
        return hashOperations.keys(TEMPLATE + key);
    }

    @Override
    public boolean isKeyExists(String hKey) {
        return hashOperations.hasKey(getRedisKey(), hKey);
    }


    public boolean isKeyExists(String key, String hKey) {
        return hashOperations.hasKey(TEMPLATE + key, hKey);
    }


    @Override
    public List<T> getAll() {
        return hashOperations.values(getRedisKey());
    }

    public List<T> getAll(String key) {
        return hashOperations.values(TEMPLATE + key);
    }

    @Override
    public long count() {
        return hashOperations.size(getRedisKey());
    }

    public long count(String key) {
        return hashOperations.size(TEMPLATE + key);
    }

    @Override
    public void empty() {
        Set<String> set = hashOperations.keys(getRedisKey());
        set.stream().forEach(key -> hashOperations.delete(getRedisKey(), key));
    }

    public void empty(String key) {
        Set<String> set = hashOperations.keys(TEMPLATE + key);
        set.stream().forEach(setKey -> hashOperations.delete(getRedisKey(), setKey));
    }

    public void remove(String key, Object... hKeys) {
        hashOperations.delete(TEMPLATE + key, hKeys);
    }
}
