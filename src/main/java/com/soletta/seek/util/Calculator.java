package com.soletta.seek.util;

/**
 * A computable that isn't interruptible, and won't throw exceptions (other than runtime exceptions).
 * 
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public interface Calculator<A, V> extends Computable<A, V> {
    /**
     * Method compute.
     * 
     * @param arg
     *            A
     * @return V
     * @see com.soletta.seek.util.Computable#compute(A)
     */

    V compute(A arg);
}
