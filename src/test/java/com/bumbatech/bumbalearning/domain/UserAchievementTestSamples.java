package com.bumbatech.bumbalearning.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class UserAchievementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserAchievement getUserAchievementSample1() {
        return new UserAchievement().id(1L);
    }

    public static UserAchievement getUserAchievementSample2() {
        return new UserAchievement().id(2L);
    }

    public static UserAchievement getUserAchievementRandomSampleGenerator() {
        return new UserAchievement().id(longCount.incrementAndGet());
    }
}
