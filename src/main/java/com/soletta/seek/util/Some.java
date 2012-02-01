package com.soletta.seek.util;

import java.util.Collection;
import java.util.Collections;
import java.util.ListIterator;

/** Counterpart to None<E>, representing an E object.
 * 
 * @author rjudson
 *
 * @param <E>
 */
final public class Some<E> extends Maybe<E> {
    
    private final E value;

    public Some(E value) {
        if (value == null)
            throw new IllegalArgumentException();
        this.value = value;
    }
    
    public static <T> Some<T> some(T t) {
        return new Some<T>(t);
    }

    @Override
    public ListIterator<E> iterator() {
        return Collections.singletonList(value).listIterator();
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public E get(int index) {
        if (index == 0)
            return value;
        else
            throw new IndexOutOfBoundsException();
    }

    @Override
    public int indexOf(Object o) {
        return value.equals(o) ? 0 : -1;
    }
    
    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return value.equals(o);
    }

    @Override
    public Object[] toArray() {
        return new Object[] { value };
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length == 1) { 
            a[0] = (T)value;
            return a;
        } else {
            return super.toArray(a);
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return c.isEmpty();
    }

    @Override
    public String toString() {
        return "Some(" + value + ')';
    }
    
    
}
