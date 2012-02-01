package com.soletta.seek.util;

import java.util.concurrent.TimeUnit;

public class Stat extends StatObject {
    
    public Stat(Object key) {
        super(key);
    }
    
    /** End one operation.
     * 
     */
    public void end() {
        end(1);
    }
    
    /** Note that one operation has happened.
     * @return 
     * 
     */
    public long operation() {
        return sum.incrementAndGet();
    }
    
    /** Indicate that one operation took a certain amount of time.
     * 
     * @param duration
     */
    public void operation(long duration) {
        processed(duration, 1);
    }
    
    public String toString() {
        
        long duration = time.get();
        long amount = sum.get();
        
        long hours = TimeUnit.HOURS.convert(duration, TimeUnit.MILLISECONDS);
        duration -= TimeUnit.MILLISECONDS.convert(hours, TimeUnit.HOURS);
        long minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS);
        duration -= TimeUnit.MILLISECONDS.convert(minutes, TimeUnit.MINUTES);
        long seconds = TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS);
        duration -= TimeUnit.MILLISECONDS.convert(seconds, TimeUnit.SECONDS);

        return String.format("%s %,d %dh:%02d:%02d.%03d", key, amount, hours, minutes, seconds, duration);
    }

    public long get() {
        return sum.get();
    }
    
    
    
}
