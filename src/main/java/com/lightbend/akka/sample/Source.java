package com.lightbend.akka.sample;

import java.util.*;

public class Source {
    private static Deque<Long> data = new LinkedList<>();

    public Source() {
        for (int i = 0; i < 100; i++) {
            data.add((long) i);
        }
    }

    public List<Long> getData() throws InterruptedException {
        int sleep = new Random().nextInt(50);
        System.out.println("Sleep " + sleep);
        Thread.sleep(sleep);
        if (data.isEmpty()) {
            return Arrays.asList(-1L, -2L);
        }
        List<Long> results = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            results.add(data.pop());
        }
        return results;
    }
}
