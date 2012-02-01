package com.soletta.seek.util;

import java.util.AbstractList;
import java.util.Collection;

/**
 * An abstract base class that throws exceptions on any list operations that modify the list.
 * 
 * @author rjudson
 * @version $Revision: 1.0 $
 */
abstract public class AbstractImmutableList<E> extends AbstractList<E> implements ImmutableList<E> {

    /**
     * Method add.
     * 
     * @param e
     *            E
     * @return boolean
     * @see java.util.List#add(E)
     */
    final public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    /**
     * Method remove.
     * 
     * @param o
     *            Object
     * @return boolean
     * @see java.util.List#remove(Object)
     */
    final public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * Method addAll.
     * 
     * @param coll
     *            Collection<? extends E>
     * @return boolean
     * @see java.util.List#addAll(Collection<? extends E>)
     */
    final public boolean addAll(Collection<? extends E> coll) {
        throw new UnsupportedOperationException();
    }

    /**
     * Method removeAll.
     * 
     * @param coll
     *            Collection<?>
     * @return boolean
     * @see java.util.List#removeAll(Collection<?>)
     */
    final public boolean removeAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    /**
     * Method retainAll.
     * 
     * @param coll
     *            Collection<?>
     * @return boolean
     * @see java.util.List#retainAll(Collection<?>)
     */
    final public boolean retainAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    /**
     * Method clear.
     * 
     * @see java.util.List#clear()
     */
    final public void clear() {
        throw new UnsupportedOperationException();
    }

    /**
     * Method set.
     * 
     * @param index
     *            int
     * @param element
     *            E
     * @return E
     * @see java.util.List#set(int, E)
     */
    final public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    /**
     * Method add.
     * 
     * @param index
     *            int
     * @param element
     *            E
     * 
     @see java.util.List#add(int, E)
     */
    final public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    /**
     * Method remove.
     * 
     * @param index
     *            int
     * @return E
     * @see java.util.List#remove(int)
     */
    final public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    /**
     * Method addAll.
     * 
     * @param index
     *            int
     * @param c
     *            Collection<? extends E>
     * @return boolean
     * @see java.util.List#addAll(int, Collection<? extends E>)
     */
    final public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Method removeRange.
     * 
     * @param fromIndex
     *            int
     * @param toIndex
     *            int
     */
    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
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
        if (fromIndex == 0 && toIndex == size())
            return this;
        else
            return new ImmutableSubList<E>(this, fromIndex, toIndex);
    }

}
