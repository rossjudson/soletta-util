package com.soletta.seek.util;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public class PriorityExecutor extends ThreadPoolExecutor {

    /**
     * Constructor for PriorityExecutor.
     * 
     * @param corePoolSize
     *            int
     * @param maxPoolSize
     *            int
     */
    public PriorityExecutor(int corePoolSize, int maxPoolSize) {
        super(corePoolSize, maxPoolSize, 60L, SECONDS, new PriorityBlockingQueue<Runnable>());
        allowCoreThreadTimeOut(true);
    }

    /**
     * Constructor for PriorityExecutor.
     * 
     * @param corePoolSize
     *            int
     * @param maxPoolSize
     *            int
     * @param threadFactory
     *            ThreadFactory
     */
    public PriorityExecutor(int corePoolSize, int maxPoolSize, final ThreadFactory threadFactory) {
        super(corePoolSize, maxPoolSize, 60L, SECONDS, new PriorityBlockingQueue<Runnable>(), threadFactory);
        allowCoreThreadTimeOut(true);
    }

    /**
     * Method getPQ.
     * 
     * @return PriorityBlockingQueue<PriorityTask<?>>
     */
    @SuppressWarnings("unchecked")
    private PriorityBlockingQueue<PriorityTask<?>> getPQ() {
        BlockingQueue<?> queue = getQueue();
        return (PriorityBlockingQueue<PriorityTask<?>>) queue;
    }

    /**
     * Method newTaskFor.
     * 
     * @param callable
     *            Callable<T>
     * @return PriorityTask<T>
     */
    @Override
    protected <T> PriorityTask<T> newTaskFor(final Callable<T> callable) {
        int priority = (callable instanceof Important) ? ((Important) callable).getPriority() : 0;
        return new PriorityTask<T>(priority, callable);
    }

    /**
     * Method newTaskFor.
     * 
     * @param runnable
     *            Runnable
     * @param value
     *            T
     * @return PriorityTask<T>
     */
    @Override
    protected <T> PriorityTask<T> newTaskFor(final Runnable runnable, final T value) {
        int priority = (runnable instanceof Important) ? ((Important) runnable).getPriority() : 0;
        return new PriorityTask<T>(priority, runnable, value);
    }

    /**
     * Method submit.
     * 
     * @param task
     *            Runnable
     * @return PriorityTask<?> * @see java.util.concurrent.ExecutorService#submit(Runnable)
     */
    @Override
    public PriorityTask<?> submit(Runnable task) {
        return (PriorityTask<?>) super.submit(task);
    }

    /**
     * Method submit.
     * 
     * @param task
     *            Runnable
     * @param result
     *            T
     * @return PriorityTask<T> * @see java.util.concurrent.ExecutorService#submit(Runnable, T)
     */
    @Override
    public <T> PriorityTask<T> submit(Runnable task, T result) {
        return (PriorityTask<T>) super.submit(task, result);
    }

    /**
     * Method submit.
     * 
     * @param task
     *            Callable<T>
     * @return PriorityTask<T> * @see java.util.concurrent.ExecutorService#submit(Callable<T>)
     */
    @Override
    public <T> PriorityTask<T> submit(Callable<T> task) {
        return (PriorityTask<T>) super.submit(task);
    }

    /**
     * Method submit.
     * 
     * @param priority
     *            int
     * @param task
     *            Callable<T>
     * @return PriorityTask<T>
     */
    public <T> PriorityTask<T> submit(int priority, Callable<T> task) {
        PriorityTask<T> ftask = new PriorityTask<T>(priority, task);
        execute(ftask);
        return ftask;
    }

    /**
     * Method submit.
     * 
     * @param priority
     *            int
     * @param task
     *            Runnable
     * @return PriorityTask<Void>
     */
    public PriorityTask<Void> submit(int priority, Runnable task) {
        PriorityTask<Void> ftask = new PriorityTask<Void>(priority, task, null);
        execute(ftask);
        return ftask;
    }

    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    public interface Important {
        /**
         * Method getPriority.
         * 
         * @return int
         */
        int getPriority();
    }

    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    public final class PriorityTask<T> extends FutureTask<T> implements Comparable<PriorityTask<T>> {
        private volatile int priority;

        /**
         * Constructor for PriorityTask.
         * 
         * @param priority
         *            int
         * @param tCallable
         *            Callable<T>
         */
        public PriorityTask(final int priority, final Callable<T> tCallable) {
            super(tCallable);
            this.priority = priority;
        }

        /**
         * Constructor for PriorityTask.
         * 
         * @param priority
         *            int
         * @param runnable
         *            Runnable
         * @param result
         *            T
         */
        public PriorityTask(final int priority, final Runnable runnable, final T result) {
            super(runnable, result);
            this.priority = priority;
        }

        /**
         * Method getPriority.
         * 
         * @return int
         */
        public int getPriority() {
            return priority;
        }

        /**
         * Method setPriority.
         * 
         * @param priority
         *            int
         */
        public void setPriority(int priority) {
            if (priority != this.priority) {
                if (getPQ().remove(this)) {
                    this.priority = priority;
                    getPQ().add(this);
                }
            }
        }

        /**
         * Method compareTo.
         * 
         * @param o
         *            PriorityTask<T>
         * @return int
         */
        @Override
        public int compareTo(final PriorityTask<T> o) {
            if (priority < o.priority)
                return -1;
            else if (priority > o.priority)
                return 1;
            else
                return 0;
        }

    }

}
