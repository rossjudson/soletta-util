package com.soletta.seek.util;

abstract public class Procedure<T> implements Function<T, Void> {

    @Override
    public Void apply(T input) {
        run(input);
        return null;
    }

    abstract public void run(T input);

}
