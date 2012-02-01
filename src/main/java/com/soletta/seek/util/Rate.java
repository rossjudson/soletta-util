package com.soletta.seek.util;

import java.util.concurrent.TimeUnit;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public class Rate extends StatObject {

    private final Object unit;
    private final long divisor;

    // private final String [] abbreviations = {
    // "ns", "us", "ms", "s", "m", "h", "d"
    // };
    // private final String [] formats = {
    // "ns", "us", ".%03dms", ":%02ds", ":%02dm", ":%02dh", ":%,dd"
    // };

    /**
     * Constructor for Rate.
     * 
     * @param key
     *            Object
     * @param unit
     *            Object
     */
    public Rate(Object key, Object unit) {
        this(key, unit, 1);
    }

    /**
     * Constructor for Rate.
     * 
     * @param key
     *            Object
     * @param unit
     *            Object
     * @param divisor
     *            long
     */
    public Rate(Object key, Object unit, long divisor) {
        super(key);
        this.unit = unit;
        this.divisor = divisor;
    }

    /**
     * Method end.
     * 
     * @param amount
     *            long
     */
    public void end(long amount) {
        super.end(amount);
    }

    /**
     * Method processed.
     * 
     * @param duration
     *            long
     * @param amount
     *            long
     */
    public void processed(long duration, long amount) {
        super.processed(duration, amount);
    }

    /**
     * Method toString.
     * 
     * @return String
     */
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

    /**
     * Method main.
     * 
     * @param args
     *            String[]
     */
    public static void main(String[] args) {
        Rate r = new Rate("rate", "kb", 1024);
        r.processed(1200, 140000);
        System.out.println(r);
        for (int i = 0; i < 20; i++) {
            r.processed(5600 * i, 140000);
            System.out.println(r);
        }
    }

    /**
     * Method processed.
     * 
     * @param amount
     *            long
     * @return long
     */
    public long processed(long amount) {
        return sum.addAndGet(amount);
    }

    /**
     * Method get.
     * 
     * @return long
     */
    public long get() {
        return sum.get();
    }

    /**
     * Method getUnit.
     * 
     * @return Object
     */
    public Object getUnit() {
        return unit;
    }

    /**
     * Method getDivisor.
     * 
     * @return long
     */
    public long getDivisor() {
        return divisor;
    }

}
