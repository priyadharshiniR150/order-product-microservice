package com.example.common_module.Util;

import java.time.Duration;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class UtilRedis {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public <T> T getOrSet(String key, Class<T> type, Supplier<T> dbCall) {

        try {

            Object cached = redisTemplate.opsForValue().get(key);

            if (cached != null) {
                System.out.println("Fetched from Redis");
                return type.cast(cached);
            }

        } catch (Exception e) {

            System.out.println("Invalid Redis cache. Removing...");
            redisTemplate.delete(key);
        }

        System.out.println("Fetched from DB");

        T data = dbCall.get();

        try {

            redisTemplate.opsForValue().set(key, data, Duration.ofMinutes(10));

        } catch (Exception e) {

            System.out.println("Redis save failed");
        }

        return data;
    }
}