package com.github.snovelli.model;

import java.util.concurrent.atomic.AtomicInteger;

public class TaskProgress {
    private final AtomicInteger ticks = new AtomicInteger(0);
    private volatile int totalTicks;

    public void tick() {
        ticks.incrementAndGet();
    }

    public int getPerventageCompleted() {
        if (ticks.get() == 0) {
            return 0;
        }
        return (int) (((double) ticks.get() / (double) totalTicks) * 100.0);
    }

    public void setTotalTicks(int totalTicks) {
        this.totalTicks = totalTicks;
    }
}
