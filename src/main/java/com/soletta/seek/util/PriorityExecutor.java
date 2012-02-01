package com.soletta.seek.util;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

public class PriorityExecutor extends ThreadPoolExecutor {

    public PriorityExecutor(int corePoolSize, int maxPoolSize) {
        super(corePoolSize, maxPoolSize, 60L, SECONDS, new PriorityBlockingQueue<Runnable>());
        allowCoreThreadTimeOut(true);
    }

    public PriorityExecutor(int corePoolSize, int maxPoolSize, final ThreadFactory threadFactory) {
        super(corePoolSize, maxPoolSize, 60L, SECONDS, new PriorityBlockingQueue<Runnable>(), threadFactory);
        allowCoreThreadTimeOut(true);
    }

    @SuppressWarnings("unchecked")
    private PriorityBlockingQueue<PriorityTask<?>> getPQ() {
        BlockingQueue<?> queue = getQueue();
        return (PriorityBlockingQueue<PriorityTask<?>>) queue;
    }
    
    @Override
    protected <T> PriorityTask<T> newTaskFor(final Callable<T> callable) {
        int priority = (callable instanceof Important) ? ((Important)callable).getPriority() : 0;
        return new PriorityTask<T>(priority, callable);
    }

    @Override
    protected <T> PriorityTask<T> newTaskFor(final Runnable runnable, final T value) {
        int priority = (runnable instanceof Important) ? ((Important)runnable).getPriority() : 0;
        return new PriorityTask<T>(priority, runnable, value);
    }
    
    @Override
    public PriorityTask<?> submit(Runnable task) {
        return (PriorityTask<?>) super.submit(task);
    }

    @Override
    public <T> PriorityTask<T> submit(Runnable task, T result) {
        return (PriorityTask<T>) super.submit(task, result);
    }

    @Override
    public <T> PriorityTask<T> submit(Callable<T> task) {
        return (PriorityTask<T>) super.submit(task);
    }

    public <T> PriorityTask<T> submit(int priority, Callable<T> task) {
        PriorityTask<T> ftask = new PriorityTask<T>(priority, task);
        execute(ftask);
        return ftask;
    }
    
    public PriorityTask<Void> submit(int priority, Runnable task) {
        PriorityTask<Void> ftask = new PriorityTask<Void>(priority, task, null);
        execute(ftask);
        return ftask;
    }

    public interface Important {
        int getPriority();
    }

    public final class PriorityTask<T> extends FutureTask<T> implements Comparable<PriorityTask<T>> {
        private volatile int priority;

        public PriorityTask(final int priority, final Callable<T> tCallable) {
            super(tCallable);
            this.priority = priority;
        }

        public PriorityTask(final int priority, final Runnable runnable, final T result) {
            super(runnable, result);
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            if (priority != this.priority) {
                if (getPQ().remove(this)) {
                    this.priority = priority;
                    getPQ().add(this);
                }
            }
        }

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
