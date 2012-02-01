package com.soletta.seek.util;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("serial")
public class RuntimeInterruptedException extends RuntimeException {

    public RuntimeInterruptedException() {
        super();
    }

    /**
     * Constructor for RuntimeInterruptedException.
     * 
     * @param message
     *            String
     * @param cause
     *            Throwable
     */
    public RuntimeInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for RuntimeInterruptedException.
     * 
     * @param message
     *            String
     */
    public RuntimeInterruptedException(String message) {
        super(message);
    }

    /**
     * Constructor for RuntimeInterruptedException.
     * 
     * @param cause
     *            Throwable
     */
    public RuntimeInterruptedException(Throwable cause) {
        super(cause);
    }

}
