package com.soletta.seek.util;

import java.util.ListIterator;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
abstract public class Maybe<E> extends AbstractImmutableList<E> {
    /**
     * This constructor is package-private to prevent creation of new subclasses.
     */
    Maybe() {
    }

    /**
     * Method iterator.
     * 
     * @return ListIterator<E>
     * @see java.util.List#iterator()
     */
    abstract public ListIterator<E> iterator();

}
