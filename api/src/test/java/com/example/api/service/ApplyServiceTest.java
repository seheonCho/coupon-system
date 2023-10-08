package com.example.api.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.api.repository.CouponRepository;

@SpringBootTest
class ApplyServiceTest {

	@Autowired
	private ApplyService applyService;

	@Autowired
	private CouponRepository couponRepository;

	@Test
	@DisplayName("한번만 응모")
	void applyOneTimes() {
		applyService.apply(1L);

		long count = couponRepository.count();

		assertThat(count).isEqualTo(1);
	}

	@Test
	@DisplayName("여러명 응모")
	void applyManyTimes() throws InterruptedException {
	    int threadCount = 1000;

		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch countDownLatch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			long userId = i;
			executorService.submit(() -> {
				try {
					applyService.apply(userId);
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		countDownLatch.await();

		long count = couponRepository.count();

		assertThat(count).isEqualTo(100);

	}

}