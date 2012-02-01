package com.soletta.seek.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Memoizer, from the Goetz book. The default structure used to hold the cache values
 * is a ConcurrentHashMap. 
 * 
 * @param <A>
 * @param <V>
 */
public class Memoizer<A, V> implements Computable<A, V>, Iterable<Map.Entry<A, V>> {
    private final ConcurrentMap<A, Future<V>> cache;
    private final Computable<A, V> func;

    /** Construct a Memoizer that requires a Computable to be supplied on each
     * call to compute.
     * 
     */
    public Memoizer() {
        this(new ConcurrentHashMap<A, Future<V>>());
    }
    
    /** Construct a Memoizer that uses the given Computable, to create the values in its
     * cache.
     * 
     * @param func The computable that can be used to calculate the values in the Memoizer.
     */
    public Memoizer(Computable<A,V> func) {
        this(new ConcurrentHashMap<A, Future<V>>(), func);
    }
    
    public Memoizer(ConcurrentMap<A, Future<V>> map, Computable<A, V> func) {
        this.func = func;
        this.cache = map;
    }

    /** Construct a Memoizer that requires a Computable to be supplied on each
     * call to compute, and uses the given ConcurrentMap as a cache structure.
     * 
     */
    public Memoizer(ConcurrentMap<A, Future<V>> map) {
        func = null;
        cache = map;
    }
    
    public Computable<A,V> getFunc() {
        return func;
    }
    
    /** Subclasses can decide, on a per-object basis, if the given object can
     * be cached. The default is to cache (true).
     * 
     * @param v
     * @return
     */
    protected boolean shouldCache(V v) {
        return true;
    }

    /** Request computation of the value, in a thread-safe and thread-sharing way,
     * of the argument presented.
     * 
     */
    public V compute(A arg) throws InterruptedException, ExecutionException {
        return compute(func, arg);
    }

    /** Request computation of the value, in a thread-safe and thread-sharing way,
     * of the argument presented, with the Computable presented.
     * 
     * @param c
     * @param arg
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public V compute(final Computable<A, V> c, final A arg) throws InterruptedException, ExecutionException {
        while (true) {
            Future<V> f = cache.get(arg);
            if (f == null) {
                Callable<V> eval = new Callable<V>() {
                    public V call() throws InterruptedException, ExecutionException {
                        return c.compute(arg);
                    }
                };
                FutureTask<V> ft = new FutureTask<V>(eval);
                f = cache.putIfAbsent(arg, ft);
                if (f == null) {
                    f = ft;
                    ft.run();
                }
            }
            try {
                V v = f.get();
                if (!shouldCache(v))
                    cache.remove(arg, f);
                return v;
            } catch (CancellationException e) {
                cache.remove(arg, f);
            }
        }
    }

    /** Add the given value to the cache, so it won't need to be computed if it 
     * is requested.
     * 
     * @param a
     * @param v
     */
    public void addCacheValue(A a, final V v) {
        cache.put(a, new Future<V>() {
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            public V get() throws InterruptedException, ExecutionException {
                return v;
            }

            public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return v;
            }

            public boolean isCancelled() {
                return false;
            }

            public boolean isDone() {
                return true;
            }
        });
    }

    /** Remove the given value from the cache, if it exists.
     * 
     * @param a
     */
    public void remove(A a) {
        cache.remove(a);
    }

    /** Clear the cache.
     * 
     */
    public void clear() {
        cache.clear();
    }
    
    /** Return the number of entries in the cache.
     * 
     * @return
     */
    public int size() {
        return cache.size();
    }
    
    /** Subclasses can look at the cache structure.
     * 
     * @return
     */
    protected ConcurrentMap<A, Future<V>> getCache() {
        return cache;
    }

    /** Return an iterator across the cache. The iterator gets the values
     * for each entry returned, so it can potentially block if a given
     * value is still being computed.
     * 
     */
    public Iterator<Entry<A, V>> iterator() {

        return new Iterator<Entry<A, V>>() {
            Iterator<Entry<A, Future<V>>> iter = cache.entrySet().iterator();

            public boolean hasNext() {
                return iter.hasNext();
            }

            public Entry<A, V> next() {
                return new Entry<A, V>() {
                    Entry<A, Future<V>> entry = iter.next();

                    public A getKey() {
                        return entry.getKey();
                    }

                    public V getValue() {
                        try {
                            return entry.getValue().get();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    public V setValue(V value) {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}