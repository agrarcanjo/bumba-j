package com.bumbatech.bumbalearning.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClassRoomTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ClassRoom getClassRoomSample1() {
        return new ClassRoom().id(1L).name("name1").description("description1");
    }

    public static ClassRoom getClassRoomSample2() {
        return new ClassRoom().id(2L).name("name2").description("description2");
    }

    public static ClassRoom getClassRoomRandomSampleGenerator() {
        return new ClassRoom().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
