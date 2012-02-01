package com.soletta.seek.util;

import java.util.AbstractList;
import java.util.Collection;

abstract public class ImmutableList<E> extends AbstractList<E> {

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

}
