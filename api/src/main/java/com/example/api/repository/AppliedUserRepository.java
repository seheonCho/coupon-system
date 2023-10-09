package com.example.api.repository;

import static com.example.api.util.constant.RedisConst.*;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AppliedUserRepository {

	private final RedisTemplate<String, String> redisTemplate;

	public AppliedUserRepository(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public Long add(Long userId) {
		return redisTemplate
			.opsForSet()
			.add(APPLIED_USER_KEY, userId.toString());
	}
}
