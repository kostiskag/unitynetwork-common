package org.kostiskag.unitynetwork.common.service;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SimpleCyclicService extends Thread {

    private final int time;
    private final AtomicBoolean kill = new AtomicBoolean(false);

    protected SimpleCyclicService(int timeInSec) throws IllegalAccessException {
        if (timeInSec <= 0) {
            throw new IllegalAccessException("time was 0 or below!");
        }
        this.time = timeInSec * 1000;
    }

    @Override
    public final void run() {
        preActions();
        while (!kill.get()) {
            try {
                sleep(time);
            } catch (InterruptedException ex) {
                interruptedMessage(ex);
            } finally {
                if (kill.get()) break;
            }
            cyclicPayload();
        }
        postActions();
    }

    public final void kill(){
        kill.set(true);
    }

    protected abstract void preActions();

    protected abstract void postActions();

    protected abstract void interruptedMessage(InterruptedException ex);

    protected abstract void cyclicPayload();

    protected final int getTime() {
        return time;
    }
}
