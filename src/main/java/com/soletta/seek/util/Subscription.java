package com.soletta.seek.util;

/** Represents a subscription to events. It can be cancelled.
 * 
 * @author rjudson
 *
 */
public interface Subscription {
    public void cancel();
}
