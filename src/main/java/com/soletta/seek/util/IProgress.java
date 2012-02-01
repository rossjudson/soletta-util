package com.soletta.seek.util;

import java.util.Date;

public interface IProgress {
    
    public String getTitle();
    public String getDetail();
    public Date getStarted();
    public Date getFinished();
    public double getProgress();
    public boolean isIndeterminate();
    public <T extends IProgress> T track(T subProgress);
    
    /** Stops this process and its subprocesses.
     * 
     */
    public void stop();
    
    /** If this process has been interrupted, throw a RuntimeInterruptedException (which is unchecked).
     * 
     */
    public void checkStop();

}
