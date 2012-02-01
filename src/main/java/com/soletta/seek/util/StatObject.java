package com.soletta.seek.util;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public abstract class StatObject {
    
    protected final Object key;
    protected final ThreadLocal<Long> markTime = new ThreadLocal<Long>();
    protected final ThreadLocal<Long> delta = new ThreadLocal<Long>();
    protected final AtomicLong time = new AtomicLong();
    protected final AtomicLong sum = new AtomicLong(); 
    protected final AtomicReference<String> note = new AtomicReference<String>();
    
    protected StatObject(Object key) {
        this.key = key;

        if (key == null)
            throw new IllegalArgumentException("StatObject key cannot be null");
    }
    
    public void begin() {
        markTime.set(System.currentTimeMillis());
    }
    
    protected void end(long amount) {
        long marked = markTime.get();
        assert marked > 0;
        processed(System.currentTimeMillis() - marked, amount);
        markTime.set(0L);
    }
    
    public long mark() {
        Long current = markTime.get();
        long now = System.currentTimeMillis();
        markTime.set(now);
        if (current != null) {
            long dur = now - current;
            return time.addAndGet(dur);
        } else {
            return time.get();
        }
    }
    
    protected void processed(long duration, long amount) {
        assert duration >= 0;
        assert amount >= 0;
        time.addAndGet(duration);
        sum.addAndGet(amount);
    }

    public Object getKey() {
        return key;
    }
    
    public long getTime() {
        return time.get();
    }
    
    public long getSum() {
        return sum.get();
    }
    
    public String getNote() {
        return note.get();
    }
    
    public void setNote(String note) {
        this.note.set(note);
    }
    
    /** Get the number of operations since the last call to getDelta().
     * 
     * @return
     */
    public long getDelta() {
        Long d = delta.get();
        long sg = sum.get();
        long ret = sg - (d == null ? 0 : d);
        delta.set(sg);
        return ret;
    }
    
}
