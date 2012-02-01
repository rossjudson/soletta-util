package com.soletta.seek.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Helper methods for handling threads and thread groups.
 * 
 * @author rjudson
 *
 */
public class Threads {

    static final Logger log = LoggerFactory.getLogger(Threads.class.getName());
    static ExecutorService monitorExec = Executors.newCachedThreadPool(groupedThreadFactory("thread monitors"));
    
    private Threads() {}

    /** Create a fixed pool exec with a named thread group.
     * 
     * @param name
     * @param nThreads
     * @return
     */
    public static ExecutorService newFixedThreadPool(String name, int nThreads) {
        ExecutorService pool = Executors.newFixedThreadPool(nThreads, groupedThreadFactory(name));
        return pool;
    }

    /** Create a fixed pool exec with a named thread group, that allows the threads to time out, if they are idle.
     * 
     * @param name
     * @param nThreads
     * @param timeout
     * @param units
     * @return
     */
    public static ExecutorService newFixedThreadPool(String name, int nThreads, long timeout, TimeUnit units) {
        ThreadPoolExecutor ret = new ThreadPoolExecutor(nThreads, nThreads,
            timeout, units,
            new LinkedBlockingQueue<Runnable>(),
            groupedThreadFactory(name));
        ret.allowCoreThreadTimeOut(true);
        return ret;
    }
    
    public static ThreadGroup createThreadGroup(String name) {
        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
        return new ThreadGroup(currentGroup, name);
    }

    /** Create a ThreadFactory inside a new thread group nested inside the current
     * thread group. ThreadFactories are used by java.util.concurrent to create
     * threads as necessary.
     * 
     * @param name
     * @return
     */
    public static GroupThreadFactory groupedThreadFactory(final String name) {
        final ThreadGroup group = createThreadGroup(name);
        return new GroupThreadFactoryImpl(group, name);
    }
    
    /** Create a ThreadFactory inside a new thread group nested inside the current
     * thread group; runtime exceptions are routed to the exceptionLog you provide. 
     * ThreadFactories are used by java.util.concurrent to create
     * threads as necessary.
     * 
     * @param name
     * @param exceptionLog
     * @return
     */
    public static GroupThreadFactory groupedThreadFactory(final String name, Logger exceptionLog) {
        final ThreadGroup group = createThreadGroup(name);
        return new GroupThreadFactoryImpl(group, name, exceptionLog);
    }

    /** Create a monitor for the calling thread; a monitor is a secondary thread that will periodically invoke
     * the specified callback, on the provided delay schedule. delay[0] is the first delay, followed by delay[1], 
     * and so forth. The last provided delay is repeated. <p>
     * 
     * When the calling thread finishes its activity, it should cancel the future that is returned. This will
     * cancel the monitor, and no further callbacks will occur.
     * 
     * 
     * 
     * @param callback
     * @param delayMillis The callback delay, in milliseconds.
     * 
     * @return
     */
    public static Future<Void> monitor(Monitor callback, long...delayMillis) {
        return monitorExec.submit(new MonitorCallable(Thread.currentThread(), callback, delayMillis)); 
    }
    
    /** Create a monitor for a specified thread; a monitor is a secondary thread that will periodically invoke
     * the specified callback, on the provided delay schedule. delay[0] is the first delay, followed by delay[1], 
     * and so forth. The last provided delay is repeated. <p>
     * 
     * When the calling thread finishes its activity, it should cancel the future that is returned. This will
     * cancel the monitor, and no further callbacks will occur.
     * 
     * 
     * @param thread The thead to monitor 
     * @param callback
     * @param delayMillis The callback delay, in milliseconds.
     * 
     * @return
     */
    public static Future<Void> monitor(Thread thread, Monitor callback, long... delayMillis) {
        return monitorExec.submit(new MonitorCallable(thread, callback, delayMillis)); 
    }
    
    /** Defines the callback for monitors. The thread being monitored is passed to the callback, 
     * which can interrupt the thread if it wants to.
     * 
     * @author rjudson
     *
     */
    public interface Monitor 
    {
        /** Tells the monitored thread how long it has been alive, and how many times it has been 
         * called back. The callback can interrupt the thread if it wants to. 
         * 
         * @param t
         * @param callbackNumber
         * @param timeInMillis
         */
        void aliveFor(Thread t, int callbackNumber, long timeInMillis);
    }
}
