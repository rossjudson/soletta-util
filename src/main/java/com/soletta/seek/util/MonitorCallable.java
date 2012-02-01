package com.soletta.seek.util;

import java.util.concurrent.Callable;

import com.soletta.seek.util.Threads.Monitor;

class MonitorCallable implements Callable<Void> {

    private final long started = System.currentTimeMillis();
    private int callbackNumber;
    private long [] delay;
    private Monitor callback;
    private Thread thread;

    MonitorCallable(Thread thread, Monitor callback, long... delay) {
        this.thread = thread;
        this.delay = delay;
        this.callback = callback;
    }

    @Override
    public Void call() throws Exception {
        try {
            while (thread.isAlive() && !Thread.interrupted()) {
                long d;
                if (delay == null || delay.length == 0) 
                    d = 1000;
                else 
                    d = delay[Math.min(delay.length-1, callbackNumber)];
                thread.join(d);
                if (!Thread.interrupted() && thread.isAlive()) {
                    callback.aliveFor(thread, callbackNumber++, System.currentTimeMillis() - started);
                }
            }
        } catch (InterruptedException e) {
            // ignore.
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }
    
}