package com.soletta.seek.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

// not complete yet
/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
class DualBlockingQueue<T> implements BlockingQueue<T> {

    private final BlockingQueue<T> head, tail;

    public DualBlockingQueue() {
        this(new LinkedBlockingQueue<T>(), new LinkedBlockingQueue<T>());
    }

    /**
     * Constructor for DualBlockingQueue.
     * 
     * @param head
     *            BlockingQueue<T>
     * @param tail
     *            BlockingQueue<T>
     */
    public DualBlockingQueue(BlockingQueue<T> head, BlockingQueue<T> tail) {
        this.head = head;
        this.tail = tail;
    }

    /**
     * Method size.
     * 
     * @return int
     * @see java.util.Collection#size()
     */
    public int size() {
        return head.size() + tail.size();
    }

    /**
     * Method isEmpty.
     * 
     * @return boolean
     * @see java.util.Collection#isEmpty()
     */
    public boolean isEmpty() {
        return head.isEmpty() && tail.isEmpty();
    }

    /**
     * Method add.
     * 
     * @param e
     *            T
     * @return boolean
     */
    public boolean add(T e) {
        return tail.add(e);
    }

    /**
     * Method iterator.
     * 
     * @return Iterator<T>
     * @see java.util.Collection#iterator()
     */
    public Iterator<T> iterator() {
        return tail.iterator();
    }

    /**
     * Method remove.
     * 
     * @return T
     * @see java.util.Queue#remove()
     */
    public T remove() {
        T ret = head.poll();
        if (ret == null)
            ret = tail.remove();
        return ret;
    }

    /**
     * Method offer.
     * 
     * @param e
     *            T
     * @return boolean
     */
    public boolean offer(T e) {
        return tail.offer(e);
    }

    /**
     * Method toArray.
     * 
     * @return Object[]
     * @see java.util.Collection#toArray()
     */
    public Object[] toArray() {
        return tail.toArray();
    }

    /**
     * Method poll.
     * 
     * @return T
     * @see java.util.Queue#poll()
     */
    public T poll() {
        T ret = head.poll();
        if (ret == null)
            ret = tail.poll();
        return ret;
    }

    /**
     * Method element.
     * 
     * @return T
     * @see java.util.Queue#element()
     */
    public T element() {
        return peek();
    }

    /**
     * Method peek.
     * 
     * @return T
     * @see java.util.Queue#peek()
     */
    public T peek() {
        T ret = head.peek();
        if (ret == null)
            ret = tail.peek();
        return ret;
    }

    /**
     * Method toArray.
     * 
     * @param a
     *            K[]
     * @return K[]
     */
    public <K> K[] toArray(K[] a) {
        return tail.toArray(a);
    }

    /**
     * Method put.
     * 
     * @param e
     *            T
     * @throws InterruptedException
     */
    public void put(T e) throws InterruptedException {
        tail.put(e);
    }

    /**
     * Method offer.
     * 
     * @param e
     *            T
     * @param timeout
     *            long
     * @param unit
     *            TimeUnit
     * @return boolean * @throws InterruptedException
     */
    public boolean offer(T e, long timeout, TimeUnit unit) throws InterruptedException {
        return tail.offer(e, timeout, unit);
    }

    /**
     * Method take.
     * 
     * @return T * @throws InterruptedException * @see java.util.concurrent.BlockingQueue#take()
     */
    public T take() throws InterruptedException {
        return tail.take();
    }

    /**
     * Method poll.
     * 
     * @param timeout
     *            long
     * @param unit
     *            TimeUnit
     * @return T * @throws InterruptedException * @see java.util.concurrent.BlockingQueue#poll(long, TimeUnit)
     */
    public T poll(long timeout, TimeUnit unit) throws InterruptedException {
        return tail.poll(timeout, unit);
    }

    /**
     * Method remainingCapacity.
     * 
     * @return int
     * @see java.util.concurrent.BlockingQueue#remainingCapacity()
     */
    public int remainingCapacity() {
        return head.remainingCapacity() + tail.remainingCapacity();
    }

    /**
     * Method remove.
     * 
     * @param o
     *            Object
     * @return boolean
     * @see java.util.concurrent.BlockingQueue#remove(Object)
     */
    public boolean remove(Object o) {
        return head.remove(o) | tail.remove(o);
    }

    /**
     * Method contains.
     * 
     * @param o
     *            Object
     * @return boolean * @see java.util.concurrent.BlockingQueue#contains(Object)
     */
    public boolean contains(Object o) {
        return head.contains(o) | tail.contains(o);
    }

    /**
     * Method drainTo.
     * 
     * @param c
     *            Collection<? super T>
     * @return int * @see java.util.concurrent.BlockingQueue#drainTo(Collection<? super T>)
     */
    public int drainTo(Collection<? super T> c) {
        return head.drainTo(c) + tail.drainTo(c);
    }

    /**
     * Method containsAll.
     * 
     * @param c
     *            Collection<?>
     * @return boolean
     * @see java.util.Collection#containsAll(Collection<?>)
     */
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Method drainTo.
     * 
     * @param c
     *            Collection<? super T>
     * @param maxElements
     *            int
     * @return int * @see java.util.concurrent.BlockingQueue#drainTo(Collection<? super T>, int)
     */
    public int drainTo(Collection<? super T> c, int maxElements) {
        int did = head.drainTo(c, maxElements);
        maxElements -= did;
        if (maxElements < 0)
            return did;
        else
            return did + tail.drainTo(c, maxElements);
    }

    /**
     * Method addAll.
     * 
     * @param c
     *            Collection<? extends T>
     * @return boolean
     * @see java.util.Collection#addAll(Collection<? extends T>)
     */
    public boolean addAll(Collection<? extends T> c) {
        return tail.addAll(c);
    }

    /**
     * Method removeAll.
     * 
     * @param c
     *            Collection<?>
     * @return boolean
     * @see java.util.Collection#removeAll(Collection<?>)
     */
    public boolean removeAll(Collection<?> c) {
        return head.removeAll(c) | tail.removeAll(c);
    }

    /**
     * Method retainAll.
     * 
     * @param c
     *            Collection<?>
     * @return boolean
     * @see java.util.Collection#retainAll(Collection<?>)
     */
    public boolean retainAll(Collection<?> c) {
        return head.retainAll(c) | tail.retainAll(c);
    }

    /**
     * Method clear.
     * 
     * @see java.util.Collection#clear()
     */
    public void clear() {
        head.clear();
        tail.clear();
    }

}
