package com.soletta.seek.util;

import static java.lang.management.ManagementFactory.getPlatformMBeanServer;

import java.util.concurrent.atomic.AtomicReference;

import javax.management.ObjectName;
import javax.management.StandardMBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Methods for dealing with finaliztion issues.
 * 
 * @author rjudson
 *
 */
public class LibFinalization {
    private static AtomicReference<FinalizationMonitorThread> monitor = new AtomicReference<FinalizationMonitorThread>();
    private static ObjectName monitorName;
    static final Logger log = LoggerFactory.getLogger(LibFinalization.class);
    
    public static void monitorFinalization() {
        
        FinalizationMonitorThread fmt = monitor.get();
        if (fmt == null) {
            FinalizationMonitorThread thread = new FinalizationMonitorThread();
            if (monitor.compareAndSet(null, thread)) {
                
                thread.start();
    
                if (monitorName == null)
                    try {
                        monitorName = ObjectName.getInstance("seek", "Monitor", "Finalization");
                        getPlatformMBeanServer().registerMBean(new StandardMBean(fmt, FinalizationMonitorMXBean.class, true), monitorName);
                    } catch (Exception e) {
                        log.warn("Can't register finalization monitor", e);
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
    
    public interface FinalizationMonitorMXBean {
        long getIterations();
        long getDelay();
        void setDelay(long delay);
    }
    
    public static class FinalizationMonitorThread extends Thread implements FinalizationMonitorMXBean {

        private volatile long iterations, delay;
        
        FinalizationMonitorThread() {
            super("Finalization Monitor");
        }
        
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
                        log.debug(String.format("Free: %,dmb  Max: %,dmb  Total: %,dmb", Runtime.getRuntime()
                            .freeMemory() / 1024 / 1024, Runtime.getRuntime().maxMemory() / 1024 / 1024, Runtime
                            .getRuntime().totalMemory() / 1024 / 1024));
                    }
                    
                    Thread.sleep(30000);
                }
            } catch (InterruptedException ie) {
                // ignore.
            } finally {
                log.info("Monitor thread is exiting.");
            }
        }

        public long getDelay() {
            return delay;
        }

        public void setDelay(long delay) {
            this.delay = delay;
        }

        public long getIterations() {
            return iterations;
        }
        
        
        
    }
    
}
