package com.soletta.seek.util;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public class Tuple {

    private Tuple() {
    }

    /**
     * Method of.
     * 
     * @param a
     *            A
     * @param b
     *            B
     * @return Tuple2<A,B>
     */
    public static <A, B> Tuple2<A, B> of(A a, B b) {
        return new Tuple2<A, B>(a, b);
    }

    /**
     * Method of.
     * 
     * @param a
     *            A
     * @param b
     *            B
     * @param c
     *            C
     * @return Tuple3<A,B,C>
     */
    public static <A, B, C> Tuple3<A, B, C> of(A a, B b, C c) {
        return new Tuple3<A, B, C>(a, b, c);
    }

    /**
     * Method of.
     * 
     * @param a
     *            A
     * @param b
     *            B
     * @param c
     *            C
     * @param d
     *            D
     * @return Tuple4<A,B,C,D>
     */
    public static <A, B, C, D> Tuple4<A, B, C, D> of(A a, B b, C c, D d) {
        return new Tuple4<A, B, C, D>(a, b, c, d);
    }

    /**
     * Method of.
     * 
     * @param a
     *            A
     * @param b
     *            B
     * @param c
     *            C
     * @param d
     *            D
     * @param e
     *            E
     * @return Tuple5<A,B,C,D,E>
     */
    public static <A, B, C, D, E> Tuple5<A, B, C, D, E> of(A a, B b, C c, D d, E e) {
        return new Tuple5<A, B, C, D, E>(a, b, c, d, e);
    }
}
