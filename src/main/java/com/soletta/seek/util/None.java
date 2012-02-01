package com.soletta.seek.util;

import java.util.Collection;
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

    @SuppressWarnings("rawtypes")
    final static Iter NONE_ITER = new Iter<Object>();
    
    @SuppressWarnings("unchecked")
    public static <T> None<T> none() {
        return (None<T>) NONE;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public ListIterator<E> iterator() {
        return NONE_ITER;
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
    
    private Object readResolve() {
        return NONE;
    }

    static class Iter<E> implements ImmutableListIterator<E>, ListIterator<E> {

        Iter() {
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public E next() {
            throw new IllegalStateException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(E e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(E e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public E previous() {
            throw new IllegalStateException();
        }

        @Override
        public int nextIndex() {
            return 0;
        }

        @Override
        public int previousIndex() {
            return -1;
        }
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public ImmutableListIterator<E> immutableIterator() {
        return NONE_ITER;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ImmutableListIterator<E> immutableListIterator() {
        return NONE_ITER;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ImmutableListIterator<E> immutableListIterator(int index) {
        return NONE_ITER;
    }

    @Override
    public ImmutableList<E> immutableSubList(int fromIndex, int toIndex) {
        // TODO Auto-generated method stub
        return null;
    }
}
