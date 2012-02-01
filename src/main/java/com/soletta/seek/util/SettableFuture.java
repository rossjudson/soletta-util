package com.soletta.seek.util;

import java.util.concurrent.FutureTask;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public class SettableFuture<V> extends FutureTask<V> {

    public SettableFuture() {
        super(new Runnable() {
            @Override
            public void run() {
            }
        }, null);
    }

    /**
     * Method set.
     * 
     * @param v
     *            V
     */
    @Override
    public void set(V v) {
        super.set(v);
    }

    /**
     * Method setException.
     * 
     * @param t
     *            Throwable
     */
    @Override
    public void setException(Throwable t) {
        super.setException(t);
    }

}
