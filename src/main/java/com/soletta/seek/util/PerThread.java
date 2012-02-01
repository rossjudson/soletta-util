package com.soletta.seek.util;

/**
 * A PerThread storage that doesn't support checked exceptions during create and dispose operations.
 * 
 * @author rjudson
 * @version $Revision: 1.0 $
 */
abstract public class PerThread<T> extends AbstractPerThread<T, RuntimeException> {

    /**
     * Method create.
     * 
     * @return T
     */
    protected abstract T create();

    /**
     * Method dispose.
     * 
     * @param t
     *            T
     */
    protected abstract void dispose(T t);
}
