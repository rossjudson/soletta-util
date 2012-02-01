package com.soletta.seek.util;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public interface ImmutableIterator<E> {

    /**
     * Method hasNext.
     * 
     * @return boolean
     */
    boolean hasNext();

    /**
     * Method next.
     * 
     * @return E
     */
    E next();

}
