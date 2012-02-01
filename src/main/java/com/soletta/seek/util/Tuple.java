package com.soletta.seek.util;

public class Tuple {
    
    private Tuple() {}
    
    public static <A,B> Tuple2<A,B> of(A a, B b) {
        return new Tuple2<A,B>(a,b);
    }
    
    public static <A,B,C> Tuple3<A,B,C> of(A a, B b, C c) {
        return new Tuple3<A,B,C>(a,b,c);
    }
    public static <A,B,C,D> Tuple4<A,B,C,D> of(A a, B b, C c, D d) {
        return new Tuple4<A,B,C,D>(a,b,c,d);
    }
    public static <A,B,C,D,E> Tuple5<A,B,C,D,E> of(A a, B b, C c, D d, E e) {
        return new Tuple5<A,B,C,D,E>(a,b,c,d,e);
    }
}
