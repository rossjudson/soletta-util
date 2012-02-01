package com.soletta.seek.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public class Stats {

    ConcurrentMap<Object, StatObject> stats = new ConcurrentHashMap<Object, StatObject>();

    // <T extends StatObject> T statObject(Class<T> cls, Object key) {
    // T ret = (T) stats.get(key);
    // if (ret == null) {
    // ret = (T) ((cls == Stat.class) ? new Stat(key) : new Rate(key));
    // T prior = (T) stats.putIfAbsent(key, ret);
    // if (prior != null)
    // ret = prior;
    // }
    // return ret;
    // }
    //
    /**
     * Method stat.
     * 
     * @param key
     *            Object
     * @return Stat
     */
    public Stat stat(Object key) {
        Stat ret = (Stat) stats.get(key);
        if (ret == null) {
            ret = new Stat(key);
            Stat prior = (Stat) stats.putIfAbsent(key, ret);
            if (prior != null)
                ret = prior;
        }
        return ret;
    }

    /**
     * Method rate.
     * 
     * @param key
     *            Object
     * @param unit
     *            String
     * @param divisor
     *            int
     * @return Rate
     */
    public Rate rate(Object key, String unit, int divisor) {
        Rate ret = (Rate) stats.get(key);
        if (ret == null) {
            ret = new Rate(key, unit, divisor);
            Rate prior = (Rate) stats.putIfAbsent(key, ret);
            if (prior != null)
                ret = prior;
        }
        return ret;
    }

    public void begin() {
        for (StatObject so : stats.values()) {
            so.begin();
        }
    }

    public void end() {
        for (StatObject so : stats.values()) {
            so.end(0);
        }
    }

    /**
     * Method toString.
     * 
     * @return String
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (StatObject so : stats.values())
            sb.append(so.toString()).append('\n');
        return sb.toString();
    }

}
