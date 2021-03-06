package com.soletta.seek.util;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public class Counters {

    private static final String ELAPSE_FORMAT = "%1$tH:%1$tM:%1$tS";
    private ConcurrentMap<Key<?>, Object> map = new ConcurrentHashMap<Key<?>, Object>();
    private ThreadLocal<Calendar> calendars = new ThreadLocal<Calendar>();

    public Counters() {
        begin(TimeKeys.CREATED);
    }

    /**
     * Method getMap.
     * 
     * @return SortedMap<Key<?>,Object>
     */
    public SortedMap<Key<?>, Object> getMap() {
        TreeMap<Key<?>, Object> tm = new TreeMap<Counters.Key<?>, Object>(new Comparator<Key<?>>() {
            @Override
            public int compare(Key<?> o1, Key<?> o2) {
                int v = o1.toString().compareTo(o2.toString());
                if (v == 0) {
                    v = o1.getClass().getName().compareTo(o2.getClass().getName());
                }
                return v;
            }
        });
        tm.putAll(map);
        return tm;

    }

    /**
     * Mark the time we are beginning some process.
     * 
     * @param key
     *            NumericKey
     * @return long
     */
    public long begin(NumericKey key) {
        return set(key, System.currentTimeMillis());
    }

    /**
     * Method begin.
     * 
     * @param key
     *            TimeKey
     * @return Date
     */
    public Date begin(TimeKey key) {
        return set(key, new Date());
    }

    /**
     * Method elapsed.
     * 
     * @param key
     *            TimeKey
     * @return long
     */
    public long elapsed(TimeKey key) {
        Date al = get(key);
        if (al != null) {
            long elapsed = System.currentTimeMillis() - al.getTime();
            return elapsed;
        } else {
            throw new RuntimeException("Counter key not available: " + key);
        }
    }

    /**
     * Method elapsed.
     * 
     * @param from
     *            TimeKey
     * @param to
     *            TimeKey
     * @return long
     */
    public long elapsed(TimeKey from, TimeKey to) {
        Date start = get(from);
        if (start == null)
            start = new Date();
        Date finish = get(to);
        if (finish == null)
            finish = new Date();
        return finish.getTime() - start.getTime();
    }

    /**
     * Method elapsed.
     * 
     * @param key
     *            NumericKey
     * @return long
     */
    public long elapsed(NumericKey key) {
        AtomicLong al = get(key);
        if (al != null) {
            long elapsed = System.currentTimeMillis() - al.get();
            return elapsed;
        } else {
            throw new RuntimeException("Counter key not available: " + key);
        }
    }

    /**
     * Retrieve the value for a given key.
     * 
     * @param key
     * @return T
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Key<T> key) {
        return (T) map.get(key);
    }

    /**
     * Sets a value.
     * 
     * @param key
     * @param value
     * @return T
     */
    public <T> T set(Key<T> key, T value) {
        map.put(key, value);
        return value;
    }

    /**
     * Return the time difference between what's contained in the given mark, and the current time.
     * 
     * @param key
     * @return long
     */
    public long since(TimeKey key) {
        return elapsed(key);
    }

    /**
     * Return elapsed time for the given numeric key, formatted as hh:mm:ss.
     * 
     * @param key
     * @return String
     */
    public String elapsedString(NumericKey key) {
        return elapsed(key, ELAPSE_FORMAT);
    }

    /**
     * Method elapsedString.
     * 
     * @param key
     *            TimeKey
     * @return String
     */
    public String elapsedString(TimeKey key) {
        return elapsed(key, ELAPSE_FORMAT);
    }

    /**
     * Method elapsedString.
     * 
     * @param from
     *            TimeKey
     * @param to
     *            TimeKey
     * @return String
     */
    public String elapsedString(TimeKey from, TimeKey to) {
        return formatMillis(ELAPSE_FORMAT, elapsed(from, to));
    }

    /**
     * Returns elapsed time using the given formatting string; a calendar object is passed as parameter 1.
     * 
     * @param key
     * @param format
     * @return String
     */
    public String elapsed(NumericKey key, String format) {
        return formatMillis(format, elapsed(key));
    }

    /**
     * Method elapsed.
     * 
     * @param key
     *            TimeKey
     * @param format
     *            String
     * @return String
     */
    public String elapsed(TimeKey key, String format) {
        return formatMillis(format, elapsed(key));
    }

    /**
     * Method formatMillis.
     * 
     * @param format
     *            String
     * @param millis
     *            long
     * @return String
     */
    private String formatMillis(String format, long millis) {
        Calendar cal = calendars.get();
        if (cal == null) {
            cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            calendars.set(cal);
        }
        cal.setTimeInMillis(millis);
        return String.format(format, cal);
    }

    /**
     * Method set.
     * 
     * @param key
     *            NumericKey
     * @param value
     *            long
     * @return long
     */
    public long set(NumericKey key, long value) {
        AtomicLong al = getAtomicLong(key);
        al.set(value);
        return value;
    }

    /**
     * Method increment.
     * 
     * @param key
     *            NumericKey
     * @return long
     */
    public long increment(NumericKey key) {
        return increment(key, 1);
    }

    /**
     * Method increment.
     * 
     * @param key
     *            NumericKey
     * @param amount
     *            long
     * @return long
     */
    public long increment(NumericKey key, long amount) {
        return getAtomicLong(key).addAndGet(amount);
    }

    /**
     * Method increment.
     * 
     * @param key
     *            AggregateKey
     * @param amount
     *            long
     * @return long
     */
    public long increment(AggregateKey key, long amount) {
        Aggregate agg = getAggregate(key);
        return increment(key, amount, System.currentTimeMillis() - agg.last.get());

    }

    /**
     * Method increment.
     * 
     * @param key
     *            AggregateKey
     * @param amount
     *            long
     * @param duration
     *            long
     * @return long
     */
    public long increment(AggregateKey key, long amount, long duration) {
        Aggregate agg = getAggregate(key);
        agg.totalMillis.addAndGet(duration);
        agg.count.incrementAndGet();
        return agg.sum.addAndGet(amount);
    }

    /**
     * Method project.
     * 
     * @param key
     *            AggregateKey
     * @param projectionKey
     *            AggregateKey
     * @param count
     *            long
     * @param sum
     *            long
     */
    public void project(AggregateKey key, AggregateKey projectionKey, long count, long sum) {
        Aggregate p = getAggregate(projectionKey);
        p.count.set(count);
        p.sum.set(sum);
        getAggregate(key).projection = projectionKey;
    }

    /**
     * Method estimateCompletionTime.
     * 
     * @param key
     *            AggregateKey
     * @return long
     */
    public long estimateCompletionTime(AggregateKey key) {
        Aggregate agg = getAggregate(key);
        if (agg.projection == null)
            return 0;

        Aggregate proj = getAggregate(agg.projection);

        long now = System.currentTimeMillis();
        long elapsed = now - agg.begin;

        int av = 0;
        float averageRatio = 0.0f;
        if (proj.count.get() > 0) {
            av++;
            averageRatio += Math.min(1, 1.0f * agg.count.get() / proj.count.get());
        }
        if (proj.sum.get() > 0) {
            av++;
            averageRatio += Math.min(1, 1.0f * agg.sum.get() / proj.sum.get());
        }

        if (av == 0 || elapsed == 0) {
            // can't compute -- nothing to work from.
            return 0;
        } else {
            averageRatio /= av;
            if (averageRatio < 0.001f)
                return 0;
            else
                return now + (long) (elapsed / averageRatio);
        }
    }

    /**
     * Method getAggregate.
     * 
     * @param key
     *            AggregateKey
     * @return Aggregate
     */
    private Aggregate getAggregate(AggregateKey key) {
        Aggregate v = get(key);
        if (v == null) {
            v = new Aggregate();
            Aggregate prior = (Aggregate) map.putIfAbsent(key, v);
            if (prior != null)
                v = prior;
        }
        return v;
    }

    /**
     * Mark a time, in the given key, since this Counters object was set up.
     * 
     * @param key
     */
    public void mark(TimeKey key) {
        begin(key);
    }

    /**
     * Method getAtomicLong.
     * 
     * @param key
     *            NumericKey
     * @return AtomicLong
     */
    AtomicLong getAtomicLong(NumericKey key) {
        AtomicLong v = get(key);
        if (v == null) {
            v = new AtomicLong();
            AtomicLong prior = (AtomicLong) map.putIfAbsent(key, v);
            if (prior != null)
                v = prior;
        }
        return v;
    }

    /**
     * A key to a particular counter, parameterized by the type of the counter.
     * 
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    public interface Key<T> {

    }

    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    public interface NumericKey extends Key<AtomicLong> {

    }

    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    public interface AggregateKey extends Key<Aggregate> {

    }

    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    public interface StringKey extends Key<String> {

    }

    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    public interface TimeKey extends Key<Date> {

    }

    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    public enum NumericKeys implements NumericKey {
        UNITS, PROJECTED_UNITS, SIZE, PROJECTED_SIZE;
    }

    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    public enum TimeKeys implements TimeKey {
        CREATED, START
    }

    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    public enum AggregateKeys implements AggregateKey {
        ITEM, PROJECTED_ITEM
    }

    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    public class Aggregate {
        final long begin = System.currentTimeMillis();
        AtomicLong last = new AtomicLong(begin);
        AtomicLong totalMillis = new AtomicLong();
        AtomicLong count = new AtomicLong();
        AtomicLong sum = new AtomicLong();
        AggregateKey projection;

        /**
         * Method getCount.
         * 
         * @return long
         */
        public long getCount() {
            return count.get();
        }

        /**
         * Method getSum.
         * 
         * @return long
         */
        public long getSum() {
            return sum.get();
        }
    }

    public void reset() {
        for (Entry<Key<?>, Object> entry : map.entrySet()) {
            if (entry.getKey() instanceof NumericKey) {
                get((NumericKey) entry.getKey()).set(0);
            }
        }

    }

    /**
     * Method toString.
     * 
     * @return String
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Entry<Key<?>, Object> entry : map.entrySet()) {
            String subform;
            Key<?> key = entry.getKey();
            Object[] v = { key, entry.getValue() };
            if (key instanceof NumericKey) {
                subform = "%,d";
                v = new Object[] { key, ((AtomicLong) v[1]).get() };
            } else if (key instanceof TimeKey) {
                subform = "%tT";
            } else if (key instanceof AggregateKey) {
                Aggregate agg = (Aggregate) v[1];
                v = new Object[] { key, agg.begin, agg.count.get(), agg.last.get(), agg.sum.get(), agg.totalMillis.get() };
                subform = "begin: %tT count: %,d last: %tT sum: %,d duration: %tT";
            } else {
                subform = "%s";
            }
            sb.append(String.format("%20s " + subform, v));
        }

        return sb.toString();
    }

}
