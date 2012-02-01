package com.soletta.seek.util;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/** A memoizer that doesn't have interruptible calculation functions. You can use this without
 * worrying about it throwing InterruptedExceptions. Checking for the interrupt flag is probably
 * a good idea, for callers.
 * 
 * @author rjudson
 *
 */
public class Memory<A,V> extends Memoizer<A, V> implements Calculator<A, V> {

    public Memory() {
        super();
    }

    public Memory(Calculator<A, V> func) {
        super(func);
    }

    public Memory(ConcurrentMap<A, Future<V>> map, Calculator<A, V> func) {
        super(map, func);
    }

    public Memory(ConcurrentMap<A, Future<V>> map) {
        super(map);
    }

    @Override
    public V compute(A arg) {
        try {
            return super.compute(arg);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public V calculate(Calculator<A, V> func, A arg) {
        return compute(func, arg);
    }
    
    @Override
    public V compute(Computable<A, V> c, A arg) {
        try {
            return super.compute(c, arg);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    
}
