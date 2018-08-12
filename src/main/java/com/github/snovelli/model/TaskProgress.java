package com.github.snovelli.model;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
public class TaskProgress {
    private static final Logger logger = LoggerFactory.getLogger(TaskProgress.class);

    private final AtomicInteger ticks = new AtomicInteger(0);
    private volatile int totalTicks;
    private volatile int lastLogPct = -1;

    public void tick() {
        ticks.incrementAndGet();

        int percentageCompleted = getPercentageCompleted();
        if (lastLogPct < percentageCompleted) { //Not really threadsafe. Worse that can happen it will print it more than once with the same %
            logger.info("CurPct: {}", percentageCompleted);
            lastLogPct = percentageCompleted;
        }
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
