package com.soletta.seek.util.args;

/** If LibArgs is unable to process the arguments, an ArgException will be thrown.
 * 
 * @author rjudson
 *
 */
@SuppressWarnings("serial")
public class ArgException extends Exception {

    public ArgException(String message) {
        super(message);
    }
    
}
