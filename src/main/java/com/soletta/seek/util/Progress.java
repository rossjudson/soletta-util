package com.soletta.seek.util;

import java.util.Date;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
abstract public class Progress implements IProgress {

    private String title, detail;
    private double progress;
    private IProgress subProgress;
    private Date started = new Date(), finished;

    /**
     * Method getStarted.
     * 
     * @return Date
     * @see com.soletta.seek.util.IProgress#getStarted()
     */
    public Date getStarted() {
        return started;
    }

    /**
     * Method setStarted.
     * 
     * @param started
     *            Date
     */
    public void setStarted(Date started) {
        this.started = started;
    }

    /**
     * Method getFinished.
     * 
     * @return Date
     * @see com.soletta.seek.util.IProgress#getFinished()
     */
    public Date getFinished() {
        return finished;
    }

    /**
     * Method setFinished.
     * 
     * @param finished
     *            Date
     */
    public void setFinished(Date finished) {
        this.finished = finished;
    }

    /**
     * Method getSubProgress.
     * 
     * @return IProgress
     */
    public IProgress getSubProgress() {
        return subProgress;
    }

    /**
     * Method setTitle.
     * 
     * @param title
     *            String
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Method setDetail.
     * 
     * @param detail
     *            String
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * Method setProgress.
     * 
     * @param progress
     *            double
     */
    public void setProgress(double progress) {
        this.progress = progress;
    }

    /**
     * Method getTitle.
     * 
     * @return String
     * @see com.soletta.seek.util.IProgress#getTitle()
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * Method getDetail.
     * 
     * @return String
     * @see com.soletta.seek.util.IProgress#getDetail()
     */
    @Override
    public String getDetail() {
        return detail;
    }

    /**
     * Method getProgress.
     * 
     * @return double
     * @see com.soletta.seek.util.IProgress#getProgress()
     */
    @Override
    public double getProgress() {
        return progress;
    }

    /**
     * Method title.
     * 
     * @return String
     */
    public String title() {
        return "wait...";
    }

    /**
     * Method detail.
     * 
     * @return String
     */
    protected String detail() {
        return "working...";
    }

    /**
     * Method track.
     * 
     * @param subProgress
     *            T
     * @return T
     * @see com.soletta.seek.util.IProgress#track(T)
     */
    @Override
    public <T extends IProgress> T track(T subProgress) {
        this.subProgress = subProgress;
        return subProgress;
    }

    /**
     * Method checkStop.
     * 
     * @see com.soletta.seek.util.IProgress#checkStop()
     */
    @Override
    public void checkStop() {
        if (Thread.interrupted())
            throw new RuntimeInterruptedException();
    }

    /**
     * Method isIndeterminate.
     * 
     * @return boolean
     * @see com.soletta.seek.util.IProgress#isIndeterminate()
     */
    @Override
    public boolean isIndeterminate() {
        return false;
    }

}
