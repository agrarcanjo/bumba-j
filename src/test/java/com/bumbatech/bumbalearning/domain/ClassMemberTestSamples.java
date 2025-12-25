package com.bumbatech.bumbalearning.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ClassMemberTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static ClassMember getClassMemberSample1() {
        return new ClassMember().id(1L);
    }

    public static ClassMember getClassMemberSample2() {
        return new ClassMember().id(2L);
    }

    public static ClassMember getClassMemberRandomSampleGenerator() {
        return new ClassMember().id(longCount.incrementAndGet());
    }
}
