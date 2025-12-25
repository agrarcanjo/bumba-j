package com.bumbatech.bumbalearning.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AchievementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Achievement getAchievementSample1() {
        return new Achievement().id(1L).code("code1").name("name1").iconUrl("iconUrl1").description("description1");
    }

    public static Achievement getAchievementSample2() {
        return new Achievement().id(2L).code("code2").name("name2").iconUrl("iconUrl2").description("description2");
    }

    public static Achievement getAchievementRandomSampleGenerator() {
        return new Achievement()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .iconUrl(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
