package com.example.api.service;

import static com.example.api.util.constant.KafkaConst.*;
import static com.example.api.util.constant.RedisConst.*;

import org.springframework.stereotype.Service;

import com.example.api.producer.CouponCreateProducer;
import com.example.api.repository.AppliedUserRepository;
import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;

@Service
public class ApplyService {

	private final CouponRepository couponRepository;

	private final CouponCountRepository couponCountRepository;

	private final CouponCreateProducer couponCreateProducer;

	private final AppliedUserRepository appliedUserRepository;

	public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository,
		CouponCreateProducer couponCreateProducer, AppliedUserRepository appliedUserRepository) {
		this.couponRepository = couponRepository;
		this.couponCountRepository = couponCountRepository;
		this.couponCreateProducer = couponCreateProducer;
		this.appliedUserRepository = appliedUserRepository;
	}

	public void apply(Long userId) {
		Long apply = appliedUserRepository.add(userId);

		if (isApplied(apply)) {
			return;
		}

		Long count = couponCountRepository.increment(COUPON_INCREMENT_KEY);

		if (count > 100) {
			return;
		}

		couponCreateProducer.create(COUPON_CREATE_TOPIC, userId);
	}

	private static boolean isApplied(Long apply) {
		return apply != 1;
	}
}
