package com.soletta.seek.util;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public class Tuple2<A, B> {

    private final A a;
    private final B b;

    /**
     * Constructor for Tuple2.
     * 
     * @param a
     *            A
     * @param b
     *            B
     */
    public Tuple2(A a, B b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Method getA.
     * 
     * @return A
     */
    public A getA() {
        return a;
    }

    /**
     * Method getB.
     * 
     * @return B
     */
    public B getB() {
        return b;
    }

    /**
     * Method extend.
     * 
     * @param c
     *            C
     * @return Tuple3<A,B,C>
     */
    public <C> Tuple3<A, B, C> extend(C c) {
        return Tuple.of(a, b, c);
    }

    /**
     * Method hashCode.
     * 
     * @return int
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((a == null) ? 0 : a.hashCode());
        result = prime * result + ((b == null) ? 0 : b.hashCode());
        return result;
    }

    /**
     * Method equals.
     * 
     * @param obj
     *            Object
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tuple2 other = (Tuple2) obj;
        if (a == null) {
            if (other.a != null)
                return false;
        } else if (!a.equals(other.a))
            return false;
        if (b == null) {
            if (other.b != null)
                return false;
        } else if (!b.equals(other.b))
            return false;
        return true;
    }

    /**
     * Method toString.
     * 
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Tuple2 [a=");
        builder.append(a);
        builder.append(", b=");
        builder.append(b);
        builder.append("]");
        return builder.toString();
    }

}
