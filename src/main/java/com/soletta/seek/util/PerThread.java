package com.soletta.seek.util;


/** A PerThread storage that doesn't support checked exceptions during create and dispose
 * operations.
 * 
 * @author rjudson
 *
 * @param <T>
 */
abstract public class PerThread<T> extends AbstractPerThread<T, RuntimeException> {

    protected abstract T create();
    
    protected abstract void dispose(T t);
}

