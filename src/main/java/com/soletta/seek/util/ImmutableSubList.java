package com.soletta.seek.util;

import java.util.ListIterator;

class ImmutableSubList<E> extends AbstractImmutableList<E>  {

    private final ImmutableList<E> base;
    private final int from;
    private final int to;
    
    ImmutableSubList(ImmutableList<E> base, int from, int to) {
        this.base = base;
        this.from = from;
        this.to = to;
        
        if (from < 0 || from > base.size() || to < from || to > base.size())
            throw new IllegalArgumentException();
        
    }

    @Override
    public ImmutableListIterator<E> immutableIterator() {
        return new Iter<E>(base, from, to, 0);
    }

    @Override
    public ImmutableListIterator<E> immutableListIterator() {
        return new Iter<E>(base, from, to, 0);
    }

    @Override
    public ImmutableListIterator<E> immutableListIterator(int index) {
        return new Iter<E>(base, from, to, index);
    }

    @Override
    public ImmutableList<E> immutableSubList(int fromIndex, int toIndex) {
        return (fromIndex == from && toIndex == to) ? this : new ImmutableSubList<E>(base, from + fromIndex, from + toIndex);
    }

    @Override
    public E get(int index) {
        return base.get(from + index);
    }

    @Override
    public int size() {
        return from - to;
    }
    
    static class Iter<E> implements ImmutableListIterator<E>, ListIterator<E> {

        final ImmutableListIterator<E> baseIter;
        final int from, to;
        int pos;
        
        Iter(ImmutableList<E> base, int from, int to, int pos) {
            this.baseIter = base.immutableListIterator(from);
            this.from = from;
            this.to = to;
            this.pos = pos;
        }

        @Override
        public boolean hasNext() {
            return pos < to; 
        }

        @Override
        public E next() {
            if (!hasNext())
                throw new IllegalStateException();
            
            pos++;
            return baseIter.next();
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
            return pos > from;
        }

        @Override
        public E previous() {
            if (pos == from)
                throw new IllegalStateException();
            pos--;
            return baseIter.previous();
        }

        @Override
        public int nextIndex() {
            return pos - from;
        }

        @Override
        public int previousIndex() {
            return pos == from ? -1 : pos - 1;
        }
        
    }

}
