package com.example.api.service;

import static com.example.api.util.constant.RedisConst.*;

import org.springframework.stereotype.Service;

import com.example.api.domain.Coupon;
import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;

@Service
public class ApplyService {

	private final CouponRepository couponRepository;

	private final CouponCountRepository couponCountRepository;

	public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository) {
		this.couponRepository = couponRepository;
		this.couponCountRepository = couponCountRepository;
	}

	public void apply(Long userId) {
		Long count = couponCountRepository.increment(COUPON_INCREMENT_KEY);

		if (count > 100) {
			return;
		}

		couponRepository.save(new Coupon(userId));
	}
}
