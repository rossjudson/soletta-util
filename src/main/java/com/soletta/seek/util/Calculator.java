package com.soletta.seek.util;


/** A computable that isn't interruptible, and won't throw exceptions (other than runtime 
 * exceptions).
 * 
 * @author rjudson
 *
 * @param <A>
 * @param <V>
 */
public interface Calculator<A,V> extends Computable<A,V> {
    V compute(A arg);
}
