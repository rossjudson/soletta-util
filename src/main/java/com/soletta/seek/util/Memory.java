package com.soletta.seek.util;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * A memoizer that doesn't have interruptible calculation functions. You can use this without worrying about it throwing
 * InterruptedExceptions. Checking for the interrupt flag is probably a good idea, for callers.
 * 
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public class Memory<A, V> extends Memoizer<A, V> implements Calculator<A, V> {

    public Memory() {
        super();
    }

    /**
     * Constructor for Memory.
     * 
     * @param func
     *            Calculator<A,V>
     */
    public Memory(Calculator<A, V> func) {
        super(func);
    }

    /**
     * Constructor for Memory.
     * 
     * @param map
     *            ConcurrentMap<A,Future<V>>
     * @param func
     *            Calculator<A,V>
     */
    public Memory(ConcurrentMap<A, Future<V>> map, Calculator<A, V> func) {
        super(map, func);
    }

    /**
     * Constructor for Memory.
     * 
     * @param map
     *            ConcurrentMap<A,Future<V>>
     */
    public Memory(ConcurrentMap<A, Future<V>> map) {
        super(map);
    }

    /**
     * Method compute.
     * 
     * @param arg
     *            A
     * @return V
     * @see com.soletta.seek.util.Calculator#compute(A)
     */
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

    /**
     * Method calculate.
     * 
     * @param func
     *            Calculator<A,V>
     * @param arg
     *            A
     * @return V
     */
    public V calculate(Calculator<A, V> func, A arg) {
        return compute(func, arg);
    }

    /**
     * Method compute.
     * 
     * @param c
     *            Computable<A,V>
     * @param arg
     *            A
     * @return V
     */
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
