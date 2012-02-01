package com.soletta.seek.util;

import java.util.ListIterator;

class ImmutableListImpl<E> extends AbstractImmutableList<E> {

    private final E[] items;

    ImmutableListImpl(E...items) {
        this.items = items;
    }
    
    @Override
    public ImmutableListIterator<E> immutableIterator() {
        return new Iter(0);
    }

    @Override
    public ImmutableListIterator<E> immutableListIterator() {
        return new Iter(0);
    }

    @Override
    public ImmutableListIterator<E> immutableListIterator(int index) {
        if (index < 0 || index > items.length)
            throw new IllegalArgumentException();
        return new Iter(index);
    }

    @Override
    public E get(int index) {
        return items[index];
    }

    @Override
    public int size() {
        return items.length;
    }

    class Iter implements ImmutableListIterator<E>, ListIterator<E> {

        int pos;
        
        Iter(int pos) {
            this.pos = pos;
        }

        @Override
        public boolean hasNext() {
            return pos < items.length; 
        }

        @Override
        public E next() {
            if (!hasNext())
                throw new IllegalStateException();
            return items[pos++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(E e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(E e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasPrevious() {
            return pos > 0;
        }

        @Override
        public E previous() {
            if (pos < 1)
                throw new IllegalStateException();
            return items[--pos];
        }

        @Override
        public int nextIndex() {
            return pos;
        }

        @Override
        public int previousIndex() {
            return pos-1;
        }
        
    }    
}
