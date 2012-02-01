package com.soletta.seek.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

final class GroupThreadFactoryImpl implements GroupThreadFactory {
    
    private final ThreadGroup group;
    private final String name;
    AtomicInteger index = new AtomicInteger();
    private final Logger exceptionLog;

    GroupThreadFactoryImpl(ThreadGroup group, String name) {
        this(group, name, null);
    }
    GroupThreadFactoryImpl(ThreadGroup group, String name, Logger exceptionLog) {
        this.group = group;
        this.name = name;
        this.exceptionLog = exceptionLog;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(group, r, name + '-' + index.incrementAndGet()) {
            @Override
            public void run() {
                try {
                    super.run();
                } catch (Exception ex) {
                    if (!ex.getClass().getName().contains("RecycleThread")) {
                        Logger l = exceptionLog != null ? exceptionLog : Threads.log;
                        l.log(Level.WARNING, name + " exception", ex);
                        ex.printStackTrace();
                    }
                } finally {
                    // System.out.println("Yeearrggh.");
                }
            }
            
        };
        // Restore some of the things that might have been changed.
        if (thread.isDaemon())
            thread.setDaemon(false);
        if (thread.getPriority() != Thread.NORM_PRIORITY)
            thread.setPriority(Thread.NORM_PRIORITY);
        return thread;
     }

    @Override
    public ThreadGroup getThreadGroup() {
        return group;
    }
}
