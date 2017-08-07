package com.github.snovelli.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class TaskProgress {
    private static final Logger logger = LoggerFactory.getLogger(TaskProgress.class);

    private final AtomicInteger ticks = new AtomicInteger(0);
    private volatile int totalTicks;

    public void tick() {
        ticks.incrementAndGet();
        logger.info("CurPct: {}", getPercentageCompleted());
    }

    public int getPercentageCompleted() {
        if (ticks.get() == 0) {
            return 0;
        }
        return (int) (((double) ticks.get() / (double) totalTicks) * 100.0);
    }

    public void setTotalTicks(int totalTicks) {
        this.totalTicks = totalTicks;
    }
}
