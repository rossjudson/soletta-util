package com.soletta.seek.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/** A manager of multiple PerThreads, so it's easy to deal with a number of them.
 * 
 * @author rjudson
 *
 */
public class PerThreads implements Closeable {

    private final ConcurrentMap<Class<?>, PerThread<?>> registry = new ConcurrentHashMap<Class<?>, PerThread<?>>();
    private final AtomicBoolean closed = new AtomicBoolean();
    
    public PerThreads() {
        
    }

    /** Registers a per-thread helpe for the given type.
     * 
     * @param <T>
     * @param type
     * @param perThread
     */
    public <T> void register(Class<T> type, PerThread<T> perThread) {
        registry.put(type, perThread);
    }
    
    /** Retrieves the per-thread instance of the given type.
     * 
     * @param <T>
     * @param type
     * @return
     */
    public <T> T get(Class<T> type) {
        @SuppressWarnings("unchecked")
        PerThread<T> pt = (PerThread<T>) registry.get(type);
        return pt.get();
    }

    /** Dispose of the current thread's value for the given type.
     * 
     * @param <T>
     * @param type
     */
    public <T> void dispose(Class<T> type) {
        @SuppressWarnings("unchecked")
        PerThread<T> pt = (PerThread<T>) registry.get(type);
        pt.dispose();
    }
    
    @Override
    public void close() throws IOException {
        if (closed.compareAndSet(false, true)) {
            for (PerThread<?> pt: registry.values()) {
                pt.close();
            }
            registry.clear();
        }
    }
    
    
    
}
