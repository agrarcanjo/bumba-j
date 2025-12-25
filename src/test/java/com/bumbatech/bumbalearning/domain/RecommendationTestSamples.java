package com.bumbatech.bumbalearning.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RecommendationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Recommendation getRecommendationSample1() {
        return new Recommendation().id(1L).reason("reason1");
    }

    public static Recommendation getRecommendationSample2() {
        return new Recommendation().id(2L).reason("reason2");
    }

    public static Recommendation getRecommendationRandomSampleGenerator() {
        return new Recommendation().id(longCount.incrementAndGet()).reason(UUID.randomUUID().toString());
    }
}
