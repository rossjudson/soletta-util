package com.soletta.seek.util;

/**
 * Static methods to help out with Strings.
 * 
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public class Strings {
    private Strings() {
    }

    /**
     * Return true if the given subject string starts with any of the provided prefixes.
     * 
     * @param subject
     * @param prefixes
     * @return boolean
     */
    public static boolean startsWith(String subject, String... prefixes) {
        for (String p : prefixes)
            if (subject.startsWith(p))
                return true;
        return false;
    }

    /**
     * Find the first string in tests that matches subject, then return the corresponding item. If not found, return
     * null.
     * 
     * @param subject
     * @param tests
     * @param items
     * @return T
     */
    public static <T> T select(String subject, String[] tests, T[] items) {
        for (int i = 0; i < tests.length; i++) {
            if (subject.equals(tests[i]))
                return items[i];
        }
        return null;
    }

    /**
     * Select an item based on the ending of the subject String.
     * 
     * @param subject
     * @param tests
     * @param items
     * @return T
     */
    public static <T> T selectEnding(String subject, String[] tests, T[] items) {
        for (int i = 0; i < tests.length; i++) {
            if (subject.endsWith(tests[i]))
                return items[i];
        }
        return null;
    }

}
