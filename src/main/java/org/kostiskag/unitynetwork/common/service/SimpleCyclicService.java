package org.kostiskag.unitynetwork.common.service;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SimpleCyclicService extends SimpleUnstoppedCyclicService {

    private final int time;

    protected SimpleCyclicService(int timeInSec) throws IllegalAccessException {
        if (timeInSec <= 0) {
            throw new IllegalAccessException("time was 0 or below!");
        }
        this.time = timeInSec * 1000;
    }

    @Override
    protected final void cyclicActions() {
        try {
            sleep(time);
        } catch (InterruptedException ex) {
            interruptedMessage(ex);
        } finally {
            if (getKillValue()) return;
        }
        cyclicPayload();
    }

    protected abstract void interruptedMessage(InterruptedException ex);

    protected abstract void cyclicPayload();

    protected final int getTime() {
        return time;
    }
}
