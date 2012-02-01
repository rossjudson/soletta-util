package com.soletta.seek.util;

import static java.util.Collections.singletonList;

import java.util.Collection;
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
        return singletonList(value).listIterator();
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

    @Override
    public ImmutableListIterator<E> immutableIterator() {
        return new Iter<E>(value, false);
    }

    @Override
    public ImmutableListIterator<E> immutableListIterator() {
        return new Iter<E>(value, false);
    }

    @Override
    public ImmutableListIterator<E> immutableListIterator(int index) {
        if (index < 0 || index > 1)
            throw new IllegalArgumentException();
        return new Iter<E>(value, index == 1);
    }

    @Override
    public ImmutableList<E> immutableSubList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || fromIndex > 1 || toIndex < fromIndex || toIndex > 1)
            throw new IllegalArgumentException();
        if (fromIndex == toIndex)
            return None.none();
        else 
            return this; 
    }

    static class Iter<E> implements ImmutableListIterator<E>, ListIterator<E> {

        boolean done;
        final E val;
        
        Iter(E val, boolean done) {
            this.val = val;
            this.done = done;
        }

        @Override
        public boolean hasNext() {
            return !done;
        }

        @Override
        public E next() {
            if (done)
                throw new IllegalStateException();
            else
                return val;
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
            return done;
        }

        @Override
        public E previous() {
            if (done) {
                done = false;
                return val;
            } else
                throw new IllegalStateException();
        }

        @Override
        public int nextIndex() {
            return done ? 1 : 0;
        }

        @Override
        public int previousIndex() {
            return done ? 0 : -1;
        }
        
    }
}
