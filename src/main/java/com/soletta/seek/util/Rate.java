package com.soletta.seek.util;

import java.util.concurrent.TimeUnit;

public class Rate extends StatObject {
    
    private final Object unit;
    private final long divisor;

//    private final String [] abbreviations = {
//        "ns", "us", "ms", "s", "m", "h", "d"
//    };
//    private final String [] formats = {
//        "ns", "us", ".%03dms", ":%02ds", ":%02dm", ":%02dh", ":%,dd"
//    };

    public Rate(Object key, Object unit) {
        this(key, unit, 1);
    }
    
    public Rate(Object key, Object unit, long divisor) {
        super(key);
        this.unit = unit;
        this.divisor = divisor;
    }

    public void end(long amount) {
        super.end(amount);
    }
    
    public void processed(long duration, long amount) {
        super.processed(duration, amount);
    }
    
    public String toString() {
        
        long duration = mark();
        long amount = sum.get() / divisor;
        
        long hours = TimeUnit.HOURS.convert(duration, TimeUnit.MILLISECONDS);
        duration -= TimeUnit.MILLISECONDS.convert(hours, TimeUnit.HOURS);
        long minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS);
        duration -= TimeUnit.MILLISECONDS.convert(minutes, TimeUnit.MINUTES);
        long seconds = TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS);
        duration -= TimeUnit.MILLISECONDS.convert(seconds, TimeUnit.SECONDS);

        return String.format("%s %,d%s %dh:%02d:%02d.%03d", key, amount, unit, hours, minutes, seconds, duration);
    }
    
    public static void main(String [] args) {
        Rate r = new Rate("rate", "kb", 1024);
        r.processed(1200, 140000);
        System.out.println(r);
        for (int i = 0; i < 20; i++) {
            r.processed(5600 * i, 140000);
            System.out.println(r);
        }
    }

    public long processed(long amount) {
        return sum.addAndGet(amount);
    }

    public long get() {
        return sum.get();
    }

    public Object getUnit() {
        return unit;
    }

    public long getDivisor() {
        return divisor;
    }
    
    
}
