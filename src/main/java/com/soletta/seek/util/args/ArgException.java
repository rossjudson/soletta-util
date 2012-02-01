package com.soletta.seek.util.args;

/**
 * If LibArgs is unable to process the arguments, an ArgException will be thrown.
 * 
 * @author rjudson
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("serial")
public class ArgException extends Exception {

    /**
     * Constructor for ArgException.
     * 
     * @param message
     *            String
     */
    public ArgException(String message) {
        super(message);
    }

}
