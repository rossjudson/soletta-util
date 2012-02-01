package com.soletta.seek.util;

import java.util.ListIterator;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
class ImmutableSubList<E> extends AbstractImmutableList<E> {

    private final ImmutableList<E> base;
    private final int from;
    private final int to;

    /**
     * Constructor for ImmutableSubList.
     * 
     * @param base
     *            ImmutableList<E>
     * @param from
     *            int
     * @param to
     *            int
     */
    ImmutableSubList(ImmutableList<E> base, int from, int to) {
        this.base = base;
        this.from = from;
        this.to = to;

        if (from < 0 || from > base.size() || to < from || to > base.size())
            throw new IllegalArgumentException();

    }

    /**
     * Method immutableIterator.
     * 
     * @return ImmutableListIterator<E> * @see com.soletta.seek.util.ImmutableList#immutableIterator()
     */
    @Override
    public ImmutableListIterator<E> immutableIterator() {
        return new Iter<E>(base, from, to, 0);
    }

    /**
     * Method immutableListIterator.
     * 
     * @return ImmutableListIterator<E> * @see com.soletta.seek.util.ImmutableList#immutableListIterator()
     */
    @Override
    public ImmutableListIterator<E> immutableListIterator() {
        return new Iter<E>(base, from, to, 0);
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
        return new Iter<E>(base, from, to, index);
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
        return (fromIndex == from && toIndex == to) ? this : new ImmutableSubList<E>(base, from + fromIndex, from + toIndex);
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
        return base.get(from + index);
    }

    /**
     * Method size.
     * 
     * @return int
     * @see com.soletta.seek.util.ImmutableList#size()
     */
    @Override
    public int size() {
        return from - to;
    }

    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    static class Iter<E> implements ImmutableListIterator<E>, ListIterator<E> {

        final ImmutableListIterator<E> baseIter;
        final int from, to;
        int pos;

        /**
         * Constructor for Iter.
         * 
         * @param base
         *            ImmutableList<E>
         * @param from
         *            int
         * @param to
         *            int
         * @param pos
         *            int
         */
        Iter(ImmutableList<E> base, int from, int to, int pos) {
            this.baseIter = base.immutableListIterator(from);
            this.from = from;
            this.to = to;
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
            return pos < to;
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

            pos++;
            return baseIter.next();
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
            return pos > from;
        }

        /**
         * Method previous.
         * 
         * @return E * @see com.soletta.seek.util.ImmutableListIterator#previous()
         */
        @Override
        public E previous() {
            if (pos == from)
                throw new IllegalStateException();
            pos--;
            return baseIter.previous();
        }

        /**
         * Method nextIndex.
         * 
         * @return int * @see com.soletta.seek.util.ImmutableListIterator#nextIndex()
         */
        @Override
        public int nextIndex() {
            return pos - from;
        }

        /**
         * Method previousIndex.
         * 
         * @return int * @see com.soletta.seek.util.ImmutableListIterator#previousIndex()
         */
        @Override
        public int previousIndex() {
            return pos == from ? -1 : pos - 1;
        }

    }

}
