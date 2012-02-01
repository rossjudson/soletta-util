package com.soletta.seek.util;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
abstract public class Procedure<T> implements Function<T, Void> {

    /**
     * Method apply.
     * 
     * @param input
     *            T
     * @return Void
     */
    @Override
    public Void apply(T input) {
        run(input);
        return null;
    }

    /**
     * Method run.
     * 
     * @param input
     *            T
     */
    abstract public void run(T input);

}
