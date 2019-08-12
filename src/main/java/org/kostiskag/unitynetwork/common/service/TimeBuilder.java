package org.kostiskag.unitynetwork.common.service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * In other words a subclass is only allowed to call pre and post Actions
 * and define the time range
 */
public abstract class TimeBuilder extends SimpleCyclicService {

    private final AtomicInteger respondTimer = new AtomicInteger(0);
    private final int maxWaitTime;
    private final int buildStep;

    protected TimeBuilder(int buildStepSec, int maxWaitTimeSec) throws IllegalAccessException {
        super(buildStepSec);
        this.buildStep = buildStepSec;
        this.maxWaitTime = maxWaitTimeSec;
    }

    @Override
    protected final void cyclicPayload() {
        if (getTotalElapsedTime() >= maxWaitTime) {
            kill();
        } else {
            step();
        }
    }

    private int getTotalElapsedTime() {
        return respondTimer.get();
    }

    private void step() {
        respondTimer.addAndGet(this.buildStep);
    }

    public final void resetClock() {
        respondTimer.set(0);
    }
}
