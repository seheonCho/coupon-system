package com.example.api.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CouponCountRepository {

	private final RedisTemplate<String, String> redisTemplate;

	public CouponCountRepository(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public Long increment(String key) {
		return redisTemplate
			.opsForValue()
			.increment(key);
	}

	public void flush(String key) {
		redisTemplate.delete(key);
	}
}
