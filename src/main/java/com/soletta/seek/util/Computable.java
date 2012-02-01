package com.soletta.seek.util;

import java.util.concurrent.ExecutionException;

/**
 * Computable, from the Goetz book.
 * 
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public interface Computable<A, V> {
    /**
     * Method compute.
     * 
     * @param arg
     *            A
     * @return V * @throws InterruptedException * @throws ExecutionException
     */
    V compute(A arg) throws InterruptedException, ExecutionException;
}
