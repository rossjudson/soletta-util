package com.soletta.seek.util;

import java.util.AbstractCollection;
import java.util.Collection;

/**
 * Use this as a base class if you want to create ImmutableCollections.
 * 
 * @author rjudson
 * @version $Revision: 1.0 $
 */
abstract public class AbstractImmutableCollection<E> extends AbstractCollection<E> implements ImmutableCollection<E> {

    /**
     * Method add.
     * 
     * @param e
     *            E
     * @return boolean
     * @see java.util.Collection#add(E)
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
     * @see java.util.Collection#remove(Object)
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
     * @see java.util.Collection#addAll(Collection<? extends E>)
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
     * @see java.util.Collection#removeAll(Collection<?>)
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
     * @see java.util.Collection#retainAll(Collection<?>)
     */
    final public boolean retainAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    /**
     * Method clear.
     * 
     * 
     @see java.util.Collection#clear()
     */
    final public void clear() {
        throw new UnsupportedOperationException();
    }

}
