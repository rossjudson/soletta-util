package com.soletta.seek.util;

/**
 * Represents a subscription to events. It can be cancelled.
 * 
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public interface Subscription {
    public void cancel();
}
