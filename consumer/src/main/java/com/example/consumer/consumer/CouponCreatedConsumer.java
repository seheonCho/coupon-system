package com.example.consumer.consumer;

import static com.example.consumer.util.constant.KafkaConsumerConst.*;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.consumer.domain.Coupon;
import com.example.consumer.repository.CouponRepository;

@Component
public class CouponCreatedConsumer {

	private final CouponRepository couponRepository;

	public CouponCreatedConsumer(CouponRepository couponRepository) {
		this.couponRepository = couponRepository;
	}

	@KafkaListener(topics = COUPON_CREATE_TOPIC, groupId = GROUP_TOPIC)
	public void listener(Long userID) {
		couponRepository.save(new Coupon(userID));
	}
}
