package com.soletta.seek.util;

/**
 * Simplified event bus handling.
 * 
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public class LibEvents {

    /**
     * Method subscribe.
     * 
     * @param listenerClass
     *            Class<T>
     * @param listener
     *            T
     */
    public static <T> void subscribe(Class<T> listenerClass, T listener) {

    }

    /**
     * Method subscribe.
     * 
     * @param listener
     *            Object
     */
    public static void subscribe(Object listener) {

    }

    /**
     * Publishers call this to send out events; keys will be extracted from all three objects -- publisher, keys, and
     * event.
     * 
     * @param publisher
     *            The publisher, or an identifier for the publisher.
     * @param key
     *            An object that is a key, and will be examined for additional key annotations.
     * @param event
     *            The event that will be sent.
     */
    public static <T> void publish(Object publisher, Object key, Object event) {

    }

}
