package com.soletta.seek.util;

import java.util.ListIterator;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
class ImmutableListImpl<E> extends AbstractImmutableList<E> {

    private final E[] items;

    /**
     * Constructor for ImmutableListImpl.
     * 
     * @param items
     *            E[]
     */
    ImmutableListImpl(E... items) {
        this.items = items;
    }

    /**
     * Method immutableIterator.
     * 
     * @return ImmutableListIterator<E> * @see com.soletta.seek.util.ImmutableList#immutableIterator()
     */
    @Override
    public ImmutableListIterator<E> immutableIterator() {
        return new Iter(0);
    }

    /**
     * Method immutableListIterator.
     * 
     * @return ImmutableListIterator<E> * @see com.soletta.seek.util.ImmutableList#immutableListIterator()
     */
    @Override
    public ImmutableListIterator<E> immutableListIterator() {
        return new Iter(0);
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
        if (index < 0 || index > items.length)
            throw new IllegalArgumentException();
        return new Iter(index);
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
        return items[index];
    }

    /**
     * Method size.
     * 
     * @return int
     * @see com.soletta.seek.util.ImmutableList#size()
     */
    @Override
    public int size() {
        return items.length;
    }

    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    class Iter implements ImmutableListIterator<E>, ListIterator<E> {

        int pos;

        /**
         * Constructor for Iter.
         * 
         * @param pos
         *            int
         */
        Iter(int pos) {
            this.pos = pos;
        }

        /**
         * Method hasNext.
         * 
         * @return boolean
         * @see java.util.ListIterator#hasNext()
         */
        @Override
        public boolean hasNext() {
            return pos < items.length;
        }

        /**
         * Method next.
         * 
         * @return E
         * @see java.util.ListIterator#next()
         */
        @Override
        public E next() {
            if (!hasNext())
                throw new IllegalStateException();
            return items[pos++];
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
            return pos > 0;
        }

        /**
         * Method previous.
         * 
         * @return E * @see com.soletta.seek.util.ImmutableListIterator#previous()
         */
        @Override
        public E previous() {
            if (pos < 1)
                throw new IllegalStateException();
            return items[--pos];
        }

        /**
         * Method nextIndex.
         * 
         * @return int * @see com.soletta.seek.util.ImmutableListIterator#nextIndex()
         */
        @Override
        public int nextIndex() {
            return pos;
        }

        /**
         * Method previousIndex.
         * 
         * @return int * @see com.soletta.seek.util.ImmutableListIterator#previousIndex()
         */
        @Override
        public int previousIndex() {
            return pos - 1;
        }

    }
}
