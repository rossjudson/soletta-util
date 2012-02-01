package com.soletta.seek.util;

import java.util.concurrent.FutureTask;

public class SettableFuture<V> extends FutureTask<V> {

    public SettableFuture() {
        super(new Runnable(){
            @Override
            public void run() {
            }}, null);
    }

    @Override
    public void set(V v) {
        super.set(v);
    }

    @Override
    public void setException(Throwable t) {
        super.setException(t);
    }
    
}
