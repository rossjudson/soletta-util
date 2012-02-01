package com.soletta.seek.util;

import static java.util.Collections.singletonList;

import java.util.Collection;
import java.util.ListIterator;

/**
 * Counterpart to None<E>, representing an E object.
 * 
 * @author rjudson
 * @version $Revision: 1.0 $
 */
final public class Some<E> extends Maybe<E> {

    private final E value;

    /**
     * Constructor for Some.
     * 
     * @param value
     *            E
     */
    public Some(E value) {
        if (value == null)
            throw new IllegalArgumentException();
        this.value = value;
    }

    /**
     * Method some.
     * 
     * @param t
     *            T
     * @return Some<T>
     */
    public static <T> Some<T> some(T t) {
        return new Some<T>(t);
    }

    /**
     * Method iterator.
     * 
     * @return ListIterator<E>
     * @see java.util.List#iterator()
     */
    @Override
    public ListIterator<E> iterator() {
        return singletonList(value).listIterator();
    }

    /**
     * Method size.
     * 
     * @return int
     * @see com.soletta.seek.util.ImmutableList#size()
     */
    @Override
    public int size() {
        return 1;
    }

    /**
     * Method get.
     * 
     * @param index
     *            int
     * @return E
     * @see com.soletta.seek.util.ImmutableList#get(int)
     */
    @Override
    public E get(int index) {
        if (index == 0)
            return value;
        else
            throw new IndexOutOfBoundsException();
    }

    /**
     * Method indexOf.
     * 
     * @param o
     *            Object
     * @return int
     * @see com.soletta.seek.util.ImmutableList#indexOf(Object)
     */
    @Override
    public int indexOf(Object o) {
        return value.equals(o) ? 0 : -1;
    }

    /**
     * Method isEmpty.
     * 
     * @return boolean
     * @see com.soletta.seek.util.ImmutableList#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * Method contains.
     * 
     * @param o
     *            Object
     * @return boolean * @see com.soletta.seek.util.ImmutableList#contains(Object)
     */
    @Override
    public boolean contains(Object o) {
        return value.equals(o);
    }

    /**
     * Method toArray.
     * 
     * @return Object[]
     * @see com.soletta.seek.util.ImmutableList#toArray()
     */
    @Override
    public Object[] toArray() {
        return new Object[] { value };
    }

    /**
     * Method toArray.
     * 
     * @param a
     *            T[]
     * @return T[]
     * @see com.soletta.seek.util.ImmutableList#toArray(T[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length == 1) {
            a[0] = (T) value;
            return a;
        } else {
            return super.toArray(a);
        }
    }

    /**
     * Method containsAll.
     * 
     * @param c
     *            Collection<?>
     * @return boolean * @see com.soletta.seek.util.ImmutableCollection#containsAll (Collection<?>)
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        return c.isEmpty();
    }

    /**
     * Method toString.
     * 
     * @return String
     */
    @Override
    public String toString() {
        return "Some(" + value + ')';
    }

    /**
     * Method immutableIterator.
     * 
     * @return ImmutableListIterator<E> * @see com.soletta.seek.util.ImmutableList#immutableIterator()
     */
    @Override
    public ImmutableListIterator<E> immutableIterator() {
        return new Iter<E>(value, false);
    }

    /**
     * Method immutableListIterator.
     * 
     * @return ImmutableListIterator<E> * @see com.soletta.seek.util.ImmutableList#immutableListIterator()
     */
    @Override
    public ImmutableListIterator<E> immutableListIterator() {
        return new Iter<E>(value, false);
    }

    /**
     * Method immutableListIterator.
     * 
     * @param index
     *            int
     * @return ImmutableListIterator<E> * @see com.soletta.seek.util.ImmutableList#immutableListIterator(int)
     */
    @Override
    public ImmutableListIterator<E> immutableListIterator(int index) {
        if (index < 0 || index > 1)
            throw new IllegalArgumentException();
        return new Iter<E>(value, index == 1);
    }

    /**
     * Method immutableSubList.
     * 
     * @param fromIndex
     *            int
     * @param toIndex
     *            int
     * @return ImmutableList<E> * @see com.soletta.seek.util.ImmutableList#immutableSubList(int, int)
     */
    @Override
    public ImmutableList<E> immutableSubList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || fromIndex > 1 || toIndex < fromIndex || toIndex > 1)
            throw new IllegalArgumentException();
        if (fromIndex == toIndex)
            return None.none();
        else
            return this;
    }

    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    static class Iter<E> implements ImmutableListIterator<E>, ListIterator<E> {

        boolean done;
        final E val;

        /**
         * Constructor for Iter.
         * 
         * @param val
         *            E
         * @param done
         *            boolean
         */
        Iter(E val, boolean done) {
            this.val = val;
            this.done = done;
        }

        /**
         * Method hasNext.
         * 
         * @return boolean
         * @see java.util.ListIterator#hasNext()
         */
        @Override
        public boolean hasNext() {
            return !done;
        }

        /**
         * Method next.
         * 
         * @return E
         * @see java.util.ListIterator#next()
         */
        @Override
        public E next() {
            if (done)
                throw new IllegalStateException();
            else
                return val;
        }

        /**
         * Method remove.
         * 
         * @see java.util.ListIterator#remove()
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * Method set.
         * 
         * @param e
         *            E
         * 
         @see java.util.ListIterator#set(E)
         */
        @Override
        public void set(E e) {
            throw new UnsupportedOperationException();
        }

        /**
         * Method add.
         * 
         * @param e
         *            E
         * 
         @see java.util.ListIterator#add(E)
         */
        @Override
        public void add(E e) {
            throw new UnsupportedOperationException();
        }

        /**
         * Method hasPrevious.
         * 
         * @return boolean * @see com.soletta.seek.util.ImmutableListIterator#hasPrevious()
         */
        @Override
        public boolean hasPrevious() {
            return done;
        }

        /**
         * Method previous.
         * 
         * @return E * @see com.soletta.seek.util.ImmutableListIterator#previous()
         */
        @Override
        public E previous() {
            if (done) {
                done = false;
                return val;
            } else
                throw new IllegalStateException();
        }

        /**
         * Method nextIndex.
         * 
         * @return int * @see com.soletta.seek.util.ImmutableListIterator#nextIndex()
         */
        @Override
        public int nextIndex() {
            return done ? 1 : 0;
        }

        /**
         * Method previousIndex.
         * 
         * @return int * @see com.soletta.seek.util.ImmutableListIterator#previousIndex()
         */
        @Override
        public int previousIndex() {
            return done ? 0 : -1;
        }

    }
}
