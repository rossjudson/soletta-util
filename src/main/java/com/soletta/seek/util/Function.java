package com.soletta.seek.util;

public interface Function<FROM, TO> {
    public TO apply(FROM from);
}


