package com.soletta.seek.util;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A base class used to construct PerThread classes, which keep a resource per-thread. It is parameterized by the type
 * of the object, and by the type of the exceptions that the create and dispose operations can throw.
 * 
 * @author rjudson
 * @version $Revision: 1.0 $
 */
abstract public class AbstractPerThread<T, E extends Exception> {

    /**
     * Method get.
     * 
     * @return T * @throws E
     */
    public T get() throws E {
        if (shutdown.get())
            throw new IllegalStateException("PerThread has already been shut down.");
        Thread thread = Thread.currentThread();
        T ret = map.get(thread);
        if (ret == null) {
            ret = create();
            T prior = map.putIfAbsent(thread, ret);
            if (prior != null) {
                dispose(ret);
                ret = prior;
            }
            for (Iterator<Thread> iter = map.keySet().iterator(); iter.hasNext();) {
                if (!iter.next().isAlive())
                    iter.remove();
            }
        } else if (!reuse(ret)) {
            dispose();
            return get();
        }
        return ret;
    }

    /**
     * Hook method for subclasses so they can know when an object is used again.
     * 
     * @param ret
     * @return boolean
     */
    protected boolean reuse(T ret) {
        return true;
    }

    /**
     * Dispose of the current T value, for this thread, if there is one.
     * 
     * @throws E
     */
    public void dispose() throws E {
        T t = map.remove(Thread.currentThread());
        if (t != null)
            dispose(t);
    }

    /**
     * Dispose of this PerThread, closing out the values.
     * 
     * @throws E
     *             * @throws IOException
     */
    public void close() throws E {
        close(false);
    }

    /**
     * Disposes of the values in this PerThread, accepting a flag that indicates if any further gets should result in an
     * IllegalStateException.
     * 
     * @param preventFurtherGets
     * @throws E
     */
    public void close(boolean preventFurtherGets) throws E {
        if (!preventFurtherGets || shutdown.compareAndSet(false, true)) {
            for (T t : map.values())
                dispose(t);
            map.clear();
        }
    }

    /**
     * Provides a means for subclasses to be informed of exceptions during bulk operations, like during dispose().
     * 
     * @param e
     */
    protected void handleException(E e) {

    }

    /**
     * Subclasses must provide a means of creating the objects.
     * 
     * @return T * @throws E
     */
    protected abstract T create() throws E;

    /**
     * Subclasses must provide a means of disposing of the objects.
     * 
     * @param t
     * @throws E
     */
    protected abstract void dispose(T t) throws E;

    private final AtomicBoolean shutdown = new AtomicBoolean();
    private final ConcurrentMap<Thread, T> map = new ConcurrentHashMap<Thread, T>();

}
