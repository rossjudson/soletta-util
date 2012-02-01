package com.soletta.seek.util;

import java.util.concurrent.ExecutionException;

/** Computable, from the Goetz book.
 * 
 * @author rjudson
 *
 * @param <A>
 * @param <V>
 */
public interface Computable<A, V> {
    V compute(A arg) throws InterruptedException, ExecutionException;
}
