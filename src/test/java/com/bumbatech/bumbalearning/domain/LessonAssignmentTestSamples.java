package com.bumbatech.bumbalearning.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class LessonAssignmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static LessonAssignment getLessonAssignmentSample1() {
        return new LessonAssignment().id(1L);
    }

    public static LessonAssignment getLessonAssignmentSample2() {
        return new LessonAssignment().id(2L);
    }

    public static LessonAssignment getLessonAssignmentRandomSampleGenerator() {
        return new LessonAssignment().id(longCount.incrementAndGet());
    }
}
