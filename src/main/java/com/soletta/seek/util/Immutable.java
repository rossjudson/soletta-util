package com.soletta.seek.util;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public class Immutable {

    private Immutable() {
    }

    /**
     * Method list.
     * 
     * @param elements
     *            E[]
     * @return ImmutableList<E>
     */
    public static <E> ImmutableList<E> list(E... elements) {
        if (elements == null)
            throw new IllegalArgumentException();
        switch (elements.length) {
        case 0:
            return None.none();
        case 1:
            return Some.some(elements[0]);
        default:
            return new ImmutableListImpl<E>(elements);
        }
    }

}
