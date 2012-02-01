package com.soletta.seek.util;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public class Tuple5<A, B, C, D, E> {

    private final A a;
    private final B b;
    private final C c;
    private final D d;
    private final E e;

    /**
     * Constructor for Tuple5.
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
     */
    public Tuple5(A a, B b, C c, D d, E e) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
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
     * Method getD.
     * 
     * @return D
     */
    public D getD() {
        return d;
    }

    /**
     * Method getE.
     * 
     * @return E
     */
    public E getE() {
        return e;
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
        result = prime * result + ((d == null) ? 0 : d.hashCode());
        result = prime * result + ((e == null) ? 0 : e.hashCode());
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
        Tuple5 other = (Tuple5) obj;
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
        if (d == null) {
            if (other.d != null)
                return false;
        } else if (!d.equals(other.d))
            return false;
        if (e == null) {
            if (other.e != null)
                return false;
        } else if (!e.equals(other.e))
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
        builder.append("Tuple5 [a=");
        builder.append(a);
        builder.append(", b=");
        builder.append(b);
        builder.append(", c=");
        builder.append(c);
        builder.append(", d=");
        builder.append(d);
        builder.append(", e=");
        builder.append(e);
        builder.append("]");
        return builder.toString();
    }

}
