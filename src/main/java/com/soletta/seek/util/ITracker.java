package com.soletta.seek.util;

public interface ITracker {
    
    public String getTitle();
    public String getDetail();
    public double getProgress();
    public <T extends ITracker> T track(T subTracker);
    
    /** Stops this process and its subprocesses.
     * 
     */
    public void stop();
    
    /** If this process has been interrupted, throw a RuntimeInterruptedException (which is unchecked).
     * 
     */
    public void checkStop();
    
    
}
