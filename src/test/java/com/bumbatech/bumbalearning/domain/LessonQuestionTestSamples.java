package com.bumbatech.bumbalearning.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LessonQuestionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static LessonQuestion getLessonQuestionSample1() {
        return new LessonQuestion().id(1L).orderIndex(1);
    }

    public static LessonQuestion getLessonQuestionSample2() {
        return new LessonQuestion().id(2L).orderIndex(2);
    }

    public static LessonQuestion getLessonQuestionRandomSampleGenerator() {
        return new LessonQuestion().id(longCount.incrementAndGet()).orderIndex(intCount.incrementAndGet());
    }
}
