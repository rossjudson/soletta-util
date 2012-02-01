package com.soletta.seek.util;

import java.util.Collection;
import java.util.Collections;
import java.util.ListIterator;

/** Partner class to Some<E>, representing no value. It functions as an unmodifiable 
 * List.
 * 
 * @author rjudson
 *
 * @param <E>
 */
final public class None<E> extends Maybe<E> {

    private final static Object[] ZERO_ARRAY = new Object[0];
    
    private None() {}
    
    @SuppressWarnings("rawtypes")
    public final static None NONE = new None<Object>();
    
    @SuppressWarnings("unchecked")
    public static <T> None<T> none() {
        return (None<T>) NONE;
    }
    
    @Override
    public ListIterator<E> iterator() {
        return Collections.<E>emptyList().listIterator();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public E get(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int indexOf(Object o) {
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Object[] toArray() {
        return ZERO_ARRAY;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return c.isEmpty();
    }

    @Override
    public String toString() {
        return "None";
    }
    
    

}
