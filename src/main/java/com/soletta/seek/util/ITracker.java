package com.soletta.seek.util;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public interface ITracker {

    /**
     * Method getTitle.
     * 
     * @return String
     */
    public String getTitle();

    /**
     * Method getDetail.
     * 
     * @return String
     */
    public String getDetail();

    /**
     * Method getProgress.
     * 
     * @return double
     */
    public double getProgress();

    /**
     * Method track.
     * 
     * @param subTracker
     *            T
     * @return T
     */
    public <T extends ITracker> T track(T subTracker);

    /**
     * Stops this process and its subprocesses.
     */
    public void stop();

    /**
     * If this process has been interrupted, throw a RuntimeInterruptedException (which is unchecked).
     */
    public void checkStop();

}
