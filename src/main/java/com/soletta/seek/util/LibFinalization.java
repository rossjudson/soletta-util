package com.soletta.seek.util;

import static java.lang.management.ManagementFactory.getPlatformMBeanServer;

import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.ObjectName;
import javax.management.StandardMBean;

/**
 * Methods for dealing with finaliztion issues.
 * 
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public class LibFinalization {
    private static AtomicReference<FinalizationMonitorThread> monitor = new AtomicReference<FinalizationMonitorThread>();
    private static ObjectName monitorName;
    static final Logger log = Logger.getLogger(LibFinalization.class.getName());

    public static void monitorFinalization() {

        FinalizationMonitorThread fmt = monitor.get();
        if (fmt == null) {
            FinalizationMonitorThread thread = new FinalizationMonitorThread();
            if (monitor.compareAndSet(null, thread)) {

                thread.start();

                if (monitorName == null)
                    try {
                        monitorName = ObjectName.getInstance("seek", "Monitor", "Finalization");
                        getPlatformMBeanServer().registerMBean(new StandardMBean(fmt, FinalizationMonitorMXBean.class, true),
                                monitorName);
                    } catch (Exception e) {
                        log.log(Level.WARNING, "Can't register finalization monitor", e);
                    }
            }
        }
    }

    synchronized public static void stopFinalizationMonitor() {
        FinalizationMonitorThread thread = monitor.get();
        if (monitor.compareAndSet(thread, null)) {
            thread.interrupt();
            try {
                getPlatformMBeanServer().unregisterMBean(monitorName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    public interface FinalizationMonitorMXBean {
        /**
         * Method getIterations.
         * 
         * @return long
         */
        long getIterations();

        /**
         * Method getDelay.
         * 
         * @return long
         */
        long getDelay();

        /**
         * Method setDelay.
         * 
         * @param delay
         *            long
         */
        void setDelay(long delay);
    }

    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    public static class FinalizationMonitorThread extends Thread implements FinalizationMonitorMXBean {

        private volatile long iterations, delay;

        FinalizationMonitorThread() {
            super("Finalization Monitor");
        }

        /**
         * Method run.
         * 
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            try {

                log.info("Finalization monitor started");

                long lastMemoryPrint = System.currentTimeMillis();

                while (!Thread.interrupted()) {
                    System.gc();
                    System.runFinalization();

                    if (System.currentTimeMillis() - lastMemoryPrint > 60000) {
                        lastMemoryPrint = System.currentTimeMillis();
                        log.fine(String.format("Free: %,dmb  Max: %,dmb  Total: %,dmb",
                                Runtime.getRuntime().freeMemory() / 1024 / 1024, Runtime.getRuntime().maxMemory() / 1024 / 1024,
                                Runtime.getRuntime().totalMemory() / 1024 / 1024));
                    }

                    Thread.sleep(30000);
                }
            } catch (InterruptedException ie) {
                // ignore.
            } finally {
                log.info("Monitor thread is exiting.");
            }
        }

        /**
         * Method getDelay.
         * 
         * @return long * @see com.soletta.seek.util.LibFinalization$FinalizationMonitorMXBean #getDelay()
         */
        public long getDelay() {
            return delay;
        }

        /**
         * Method setDelay.
         * 
         * @param delay
         *            long
         * 
         @see com.soletta.seek.util.LibFinalization$FinalizationMonitorMXBean#setDelay(long)
         */
        public void setDelay(long delay) {
            this.delay = delay;
        }

        /**
         * Method getIterations.
         * 
         * @return long * @see com.soletta.seek.util.LibFinalization$FinalizationMonitorMXBean #getIterations()
         */
        public long getIterations() {
            return iterations;
        }

    }

}
