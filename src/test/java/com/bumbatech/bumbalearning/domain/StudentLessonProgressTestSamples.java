package com.bumbatech.bumbalearning.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StudentLessonProgressTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static StudentLessonProgress getStudentLessonProgressSample1() {
        return new StudentLessonProgress().id(1L).score(1).xpEarned(1);
    }

    public static StudentLessonProgress getStudentLessonProgressSample2() {
        return new StudentLessonProgress().id(2L).score(2).xpEarned(2);
    }

    public static StudentLessonProgress getStudentLessonProgressRandomSampleGenerator() {
        return new StudentLessonProgress()
            .id(longCount.incrementAndGet())
            .score(intCount.incrementAndGet())
            .xpEarned(intCount.incrementAndGet());
    }
}
