package com.soletta.seek.util;

import java.util.Collection;
import java.util.ListIterator;

/**
 * Partner class to Some<E>, representing no value. It functions as an unmodifiable List.
 * 
 * @author rjudson
 * @version $Revision: 1.0 $
 */
final public class None<E> extends Maybe<E> {

    private final static Object[] ZERO_ARRAY = new Object[0];

    private None() {
    }

    @SuppressWarnings("rawtypes")
    public final static None NONE = new None<Object>();

    @SuppressWarnings("rawtypes")
    final static Iter NONE_ITER = new Iter<Object>();

    /**
     * Method none.
     * 
     * @return None<T>
     */
    @SuppressWarnings("unchecked")
    public static <T> None<T> none() {
        return (None<T>) NONE;
    }

    /**
     * Method iterator.
     * 
     * @return ListIterator<E>
     * @see java.util.List#iterator()
     */
    @SuppressWarnings("unchecked")
    @Override
    public ListIterator<E> iterator() {
        return NONE_ITER;
    }

    /**
     * Method size.
     * 
     * @return int
     * @see com.soletta.seek.util.ImmutableList#size()
     */
    @Override
    public int size() {
        return 0;
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
        return -1;
    }

    /**
     * Method isEmpty.
     * 
     * @return boolean
     * @see com.soletta.seek.util.ImmutableList#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return true;
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
        return false;
    }

    /**
     * Method toArray.
     * 
     * @return Object[]
     * @see com.soletta.seek.util.ImmutableList#toArray()
     */
    @Override
    public Object[] toArray() {
        return ZERO_ARRAY;
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
        return "None";
    }

    /**
     * Method readResolve.
     * 
     * @return Object
     */
    private Object readResolve() {
        return NONE;
    }

    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    static class Iter<E> implements ImmutableListIterator<E>, ListIterator<E> {

        Iter() {
        }

        /**
         * Method hasNext.
         * 
         * @return boolean
         * @see java.util.ListIterator#hasNext()
         */
        @Override
        public boolean hasNext() {
            return false;
        }

        /**
         * Method next.
         * 
         * @return E
         * @see java.util.ListIterator#next()
         */
        @Override
        public E next() {
            throw new IllegalStateException();
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
            return false;
        }

        /**
         * Method previous.
         * 
         * @return E * @see com.soletta.seek.util.ImmutableListIterator#previous()
         */
        @Override
        public E previous() {
            throw new IllegalStateException();
        }

        /**
         * Method nextIndex.
         * 
         * @return int * @see com.soletta.seek.util.ImmutableListIterator#nextIndex()
         */
        @Override
        public int nextIndex() {
            return 0;
        }

        /**
         * Method previousIndex.
         * 
         * @return int * @see com.soletta.seek.util.ImmutableListIterator#previousIndex()
         */
        @Override
        public int previousIndex() {
            return -1;
        }

    }

    /**
     * Method immutableIterator.
     * 
     * @return ImmutableListIterator<E> * @see com.soletta.seek.util.ImmutableList#immutableIterator()
     */
    @SuppressWarnings("unchecked")
    @Override
    public ImmutableListIterator<E> immutableIterator() {
        return NONE_ITER;
    }

    /**
     * Method immutableListIterator.
     * 
     * @return ImmutableListIterator<E> * @see com.soletta.seek.util.ImmutableList#immutableListIterator()
     */
    @SuppressWarnings("unchecked")
    @Override
    public ImmutableListIterator<E> immutableListIterator() {
        return NONE_ITER;
    }

    /**
     * Method immutableListIterator.
     * 
     * @param index
     *            int
     * @return ImmutableListIterator<E> * @see com.soletta.seek.util.ImmutableList#immutableListIterator(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public ImmutableListIterator<E> immutableListIterator(int index) {
        return NONE_ITER;
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
        // TODO Auto-generated method stub
        return null;
    }
}
