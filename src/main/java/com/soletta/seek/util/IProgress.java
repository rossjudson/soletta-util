package com.soletta.seek.util;

import java.util.Date;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public interface IProgress {

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
     * Method getStarted.
     * 
     * @return Date
     */
    public Date getStarted();

    /**
     * Method getFinished.
     * 
     * @return Date
     */
    public Date getFinished();

    /**
     * Method getProgress.
     * 
     * @return double
     */
    public double getProgress();

    /**
     * Method isIndeterminate.
     * 
     * @return boolean
     */
    public boolean isIndeterminate();

    /**
     * Method track.
     * 
     * @param subProgress
     *            T
     * @return T
     */
    public <T extends IProgress> T track(T subProgress);

    /**
     * Stops this process and its subprocesses.
     */
    public void stop();

    /**
     * If this process has been interrupted, throw a RuntimeInterruptedException (which is unchecked).
     */
    public void checkStop();

}
