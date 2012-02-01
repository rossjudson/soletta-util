package com.soletta.seek.util;

import java.util.ListIterator;

abstract public class Maybe<E> extends ImmutableList<E> {
    /**
     * This constructor is package-private to prevent creation of new
     * subclasses.
     */
    Maybe() {
    }

    abstract public ListIterator<E> iterator();
    
    

}
