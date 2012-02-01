package com.soletta.seek.util;

import java.util.AbstractCollection;
import java.util.Collection;

abstract public class ImmutableCollection<E> extends AbstractCollection<E> {
    
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
    
}
