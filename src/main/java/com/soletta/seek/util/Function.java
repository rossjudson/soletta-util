package com.soletta.seek.util;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public interface Function<FROM, TO> {
    /**
     * Method apply.
     * 
     * @param from
     *            FROM
     * @return TO
     */
    public TO apply(FROM from);
}
