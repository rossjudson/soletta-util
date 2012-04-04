package com.soletta.seek.util;

import java.util.HashMap;
import java.util.Map;

public class LibMaps {

    public static <T> Map<T, T> quickMap(T...keysAndValues) {
        assert keysAndValues.length % 2 == 0;
        Map<T,T> ret = new HashMap<T,T>();
        for (int i = 0; i < keysAndValues.length; i+=2)
            ret.put(keysAndValues[i], keysAndValues[i+1]);
        return ret;
    }
}
