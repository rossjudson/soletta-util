package com.soletta.seek.util;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicSingleton<T> {
    
    private final Class<T> clazz;
    private final AtomicReference<T> instance = new AtomicReference<T>();
    
    public static <T> AtomicSingleton<T> singleton(Class<T> clazz) {
        return new AtomicSingleton<T>(clazz);
    }
    
    public AtomicSingleton(Class<T> clazz) {
        this.clazz = clazz;
    }

    
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
