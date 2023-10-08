package com.example.api.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CouponCreateProducer {

	private final KafkaTemplate<String, Long> kafkaTemplate;

	public CouponCreateProducer(KafkaTemplate<String, Long> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}


	public void create(String topic, Long userId) {
		kafkaTemplate.send(topic, userId);
	}
}
