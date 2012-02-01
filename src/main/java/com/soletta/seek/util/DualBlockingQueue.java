package com.soletta.seek.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

// not complete yet
public class DualBlockingQueue<T> implements BlockingQueue<T> {

    
    private final BlockingQueue<T> head, tail;

    public DualBlockingQueue() {
        this(new LinkedBlockingQueue<T>(), new LinkedBlockingQueue<T>());
    }
    
    public DualBlockingQueue(BlockingQueue<T> head, BlockingQueue<T> tail) {
        this.head = head;
        this.tail = tail;
    }
    
    public int size() {
        return head.size() + tail.size();
    }

    public boolean isEmpty() {
        return head.isEmpty() && tail.isEmpty();
    }

    public boolean add(T e) {
        return tail.add(e);
    }

    public Iterator<T> iterator() {
        return tail.iterator();
    }

    public T remove() {
        T ret = head.poll();
        if (ret == null)
            ret = tail.remove();
        return ret;
    }

    public boolean offer(T e) {
        return tail.offer(e);
    }

    public Object[] toArray() {
        return tail.toArray();
    }

    public T poll() {
        T ret = head.poll();
        if (ret == null)
            ret = tail.poll();
        return ret;
    }

    public T element() {
        return peek();
    }

    public T peek() {
        T ret = head.peek();
        if (ret == null)
            ret = tail.peek();
        return ret;
    }

    public <K> K[] toArray(K[] a) {
        return tail.toArray(a);
    }

    public void put(T e) throws InterruptedException {
        tail.put(e);
    }

    public boolean offer(T e, long timeout, TimeUnit unit) throws InterruptedException {
        return tail.offer(e, timeout, unit);
    }

    public T take() throws InterruptedException {
        return tail.take();
    }

    public T poll(long timeout, TimeUnit unit) throws InterruptedException {
        return tail.poll(timeout, unit);
    }

    public int remainingCapacity() {
        return head.remainingCapacity() + tail.remainingCapacity();
    }

    public boolean remove(Object o) {
        return head.remove(o) | tail.remove(o);
    }

    public boolean contains(Object o) {
        return head.contains(o) | tail.contains(o);
    }

    public int drainTo(Collection<? super T> c) {
        return head.drainTo(c) + tail.drainTo(c);
    }

    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public int drainTo(Collection<? super T> c, int maxElements) {
        int did = head.drainTo(c, maxElements);
        maxElements -= did;
        if (maxElements < 0)
            return did;
        else
            return did + tail.drainTo(c, maxElements);
    }

    public boolean addAll(Collection<? extends T> c) {
        return tail.addAll(c);
    }

    public boolean removeAll(Collection<?> c) {
        return head.removeAll(c) | tail.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return head.retainAll(c) | tail.retainAll(c);
    }

    public void clear() {
        head.clear();
        tail.clear();
    }

    
}
