package com.soletta.seek.util;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public class Tuple3<A, B, C> {

    private final A a;
    private final B b;
    private final C c;

    /**
     * Constructor for Tuple3.
     * 
     * @param a
     *            A
     * @param b
     *            B
     * @param c
     *            C
     */
    public Tuple3(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
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
     * Method getC.
     * 
     * @return C
     */
    public C getC() {
        return c;
    }

    /**
     * Method extend.
     * 
     * @param d
     *            D
     * @return Tuple4<A,B,C,D>
     */
    public <D> Tuple4<A, B, C, D> extend(D d) {
        return Tuple.of(a, b, c, d);
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
        result = prime * result + ((c == null) ? 0 : c.hashCode());
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
        Tuple3 other = (Tuple3) obj;
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
        if (c == null) {
            if (other.c != null)
                return false;
        } else if (!c.equals(other.c))
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
        builder.append("Tuple3 [a=");
        builder.append(a);
        builder.append(", b=");
        builder.append(b);
        builder.append(", c=");
        builder.append(c);
        builder.append("]");
        return builder.toString();
    }

}
