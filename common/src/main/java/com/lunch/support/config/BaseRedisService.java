package com.lunch.support.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Set;

public abstract class BaseRedisService<T> {
    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    /**
     * 添加
     *
     * @param key    key
     * @param doamin 对象
     * @param expire 过期时间(单位:秒),传入 -1 时表示不设置过期时间
     *               <p>
     */
    public abstract void put(String key, T doamin, long expire);

    /**
     * 删除
     *
     * @param key 传入key的名称
     */
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 查询
     *
     * @param key 查询的key
     */

    public abstract T get(String key);

    /**
     * 获取当前redis库下所有对象
     *
     * @return
     */
    public List<T> getAll() {
        return null;
    }

    /**
     * 查询查询当前redis库下所有key
     *
     */
    public abstract Set<String> getKeys();

    /**
     * 判断key是否存在redis中
     *
     * @param key 传入key的名称
     * @return
     */
    public abstract boolean isKeyExists(String key);

    /**
     * 查询当前key下缓存数量
     *
     * @return
     */
    public long count() {
        return 0;
    }

    /**
     * 清空redis
     * <p>
     */
    public void empty() {

    }
}
