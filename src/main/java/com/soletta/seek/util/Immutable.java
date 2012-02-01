package com.soletta.seek.util;

public class Immutable {
    
    private Immutable() {}
    
    public static <E> ImmutableList<E> list(E...elements) {
        if (elements == null)
            throw new IllegalArgumentException();
        switch (elements.length) {
            case 0: return None.none();
            case 1: return Some.some(elements[0]);
            default: return new ImmutableListImpl<E>(elements);
        }
    }
    
}
