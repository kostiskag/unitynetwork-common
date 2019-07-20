package org.kostiskag.unitynetwork.common.service;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SimpleUnstoppedCyclicService extends Thread {

    private final AtomicBoolean kill = new AtomicBoolean(false);

    protected SimpleUnstoppedCyclicService()  { }

    @Override
    public final void run() {
        preActions();
        while (!kill.get()) {
            cyclicActions();
        }
        postActions();
    }

    public void kill(){
        kill.set(true);
    }

    public boolean getKillValue() {
        return kill.get();
    }

    protected abstract void preActions();

    protected abstract void postActions();

    protected abstract void cyclicActions();

}
