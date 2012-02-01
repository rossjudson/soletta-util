package com.soletta.seek.util;

/** Simplified event bus handling.
 * 
 * @author rjudson
 *
 */
public class LibEvents {
    
    public static <T> void subscribe(Class<T> listenerClass, T listener) {
        
    }

    public static void subscribe(Object listener) {
        
    }

    /** Publishers call this to send out events; keys will be extracted from all
     * three objects -- publisher, keys, and event. 
     * 
     * @param <T>
     * @param publisher The publisher, or an identifier for the publisher.
     * @param key An object that is a key, and will be examined for additional key annotations.
     * @param event The event that will be sent.
     */
    public static <T> void publish(Object publisher, Object key, Object event) {
        
    }
    
}
