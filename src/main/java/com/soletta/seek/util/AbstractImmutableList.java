package com.soletta.seek.util;

import java.util.AbstractList;
import java.util.Collection;

/** An abstract base class that throws exceptions on any list operations that modify the list.
 * 
 * @author rjudson
 *
 * @param <E>
 */
abstract public class AbstractImmutableList<E> extends AbstractList<E> implements ImmutableList<E> {

    final public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    final public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    final public boolean addAll(Collection<? extends E> coll) {
        throw new UnsupportedOperationException();
    }

    final public boolean removeAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    final public boolean retainAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    final public void clear() {
        throw new UnsupportedOperationException();
    }

    final public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    final public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    final public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    final public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableList<E> immutableSubList(int fromIndex, int toIndex) {
        if (fromIndex == 0 && toIndex == size())
            return this;
        else
            return  new ImmutableSubList<E>(this, fromIndex, toIndex);
    }
    
    

}
