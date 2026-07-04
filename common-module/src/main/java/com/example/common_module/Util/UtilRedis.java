package com.example.common_module.Util;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class UtilRedis {
	

	    @Autowired
	    private RedisTemplate<String, Object> redisTemplate;

	    @SuppressWarnings("unchecked")
		public <T> T getOrSet(String key, Class<T> type, Supplier<T> dbCall) {

	        try {
	            Object cached = redisTemplate.opsForValue().get(key);

	            if (cached != null) {
	                System.out.println("Fetched from Redis");
	                return (T) cached;
	            }
	        } catch (Exception e) {
	            System.out.println("Redis down, fallback to DB");
	        }

	        System.out.println("Fetched from DB");

	        T data = dbCall.get();

	        try {
	            redisTemplate.opsForValue().set(key, data);
	        } catch (Exception e) {
	            System.out.println("Redis save failed");
	        }

	        return data;
	    }
	}

