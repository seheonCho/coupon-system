package com.example.consumer.consumer;

import static com.example.consumer.util.constant.KafkaConsumerConst.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.consumer.domain.Coupon;
import com.example.consumer.domain.FailedEvent;
import com.example.consumer.repository.CouponRepository;
import com.example.consumer.repository.FailedEventRepository;

@Component
public class CouponCreatedConsumer {

	private final CouponRepository couponRepository;

	private final FailedEventRepository failedEventRepository;

	private final Logger logger = LoggerFactory.getLogger(CouponCreatedConsumer.class);

	public CouponCreatedConsumer(CouponRepository couponRepository, FailedEventRepository failedEventRepository) {
		this.couponRepository = couponRepository;
		this.failedEventRepository = failedEventRepository;
	}

	@KafkaListener(topics = COUPON_CREATE_TOPIC, groupId = GROUP_TOPIC)
	public void listener(Long userID) {
		try {
			couponRepository.save(new Coupon(userID));
		} catch (Exception e) {
			logger.error("failed to create coupon::", userID);
			failedEventRepository.save(new FailedEvent(userID));
		}

	}
}
