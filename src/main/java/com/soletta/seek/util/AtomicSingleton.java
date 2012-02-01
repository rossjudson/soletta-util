package com.soletta.seek.util;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public class AtomicSingleton<T> {

    private final Class<T> clazz;
    private final AtomicReference<T> instance = new AtomicReference<T>();

    /**
     * Method singleton.
     * 
     * @param clazz
     *            Class<T>
     * @return AtomicSingleton<T>
     */
    public static <T> AtomicSingleton<T> singleton(Class<T> clazz) {
        return new AtomicSingleton<T>(clazz);
    }

    /**
     * Constructor for AtomicSingleton.
     * 
     * @param clazz
     *            Class<T>
     */
    public AtomicSingleton(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Method get.
     * 
     * @return T
     */
    public T get() {
        T ret = instance.get();
        if (ret == null) {
            try {
                ret = clazz.newInstance();
                if (!instance.compareAndSet(null, ret))
                    ret = instance.get();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return ret;
    }
}
