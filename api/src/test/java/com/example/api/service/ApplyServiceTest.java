package com.example.api.service;

import static com.example.api.util.constant.RedisConst.*;
import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;

@SpringBootTest
class ApplyServiceTest {

	@Autowired
	private ApplyService applyService;

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private CouponCountRepository couponCountRepository;

	@Test
	@DisplayName("한번만 응모")
	void applyOneTimes() {
		applyService.apply(1L);

		long count = couponRepository.count();

		assertThat(count).isEqualTo(1);
	}

	@BeforeEach
	void beforeEach() {
		couponCountRepository.flush(COUPON_INCREMENT_KEY);
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

		Thread.sleep(10_000);

		long count = couponRepository.count();

		assertThat(count).isEqualTo(100);

	}

	@Test
	@DisplayName("한 명당 한 개의 쿠폰 발급")
	void oneCouponForPerson() throws InterruptedException {
		int threadCount = 1000;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch countDownLatch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					applyService.apply(1L);
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		countDownLatch.await();

		Thread.sleep(10_000);

		long count = couponRepository.count();

		assertThat(count).isEqualTo(1);

	}
}