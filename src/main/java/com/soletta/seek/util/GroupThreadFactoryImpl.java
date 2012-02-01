package com.soletta.seek.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
final class GroupThreadFactoryImpl implements GroupThreadFactory {

    private final ThreadGroup group;
    private final String name;
    AtomicInteger index = new AtomicInteger();
    private final Logger exceptionLog;

    /**
     * Constructor for GroupThreadFactoryImpl.
     * 
     * @param group
     *            ThreadGroup
     * @param name
     *            String
     */
    GroupThreadFactoryImpl(ThreadGroup group, String name) {
        this(group, name, null);
    }

    /**
     * Constructor for GroupThreadFactoryImpl.
     * 
     * @param group
     *            ThreadGroup
     * @param name
     *            String
     * @param exceptionLog
     *            Logger
     */
    GroupThreadFactoryImpl(ThreadGroup group, String name, Logger exceptionLog) {
        this.group = group;
        this.name = name;
        this.exceptionLog = exceptionLog;
    }

    /**
     * Method newThread.
     * 
     * @param r
     *            Runnable
     * @return Thread * @see java.util.concurrent.ThreadFactory#newThread(Runnable)
     */
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

    /**
     * Method getThreadGroup.
     * 
     * @return ThreadGroup * @see com.soletta.seek.util.GroupThreadFactory#getThreadGroup()
     */
    @Override
    public ThreadGroup getThreadGroup() {
        return group;
    }
}
