package com.soletta.seek.util;

import java.util.Date;

abstract public class Progress implements IProgress {

    private String title, detail;
    private double progress;
    private IProgress subProgress;
    private Date started = new Date(), finished;
    
    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public Date getFinished() {
        return finished;
    }

    public void setFinished(Date finished) {
        this.finished = finished;
    }

    public IProgress getSubProgress() {
        return subProgress;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDetail() {
        return detail;
    }

    @Override
    public double getProgress() {
        return progress;
    }

    public String title() {
        return "wait...";
    }

    protected String detail() {
        return "working...";
    }

    @Override
    public <T extends IProgress> T track(T subProgress) {
        this.subProgress = subProgress;
        return subProgress;
    }

    @Override
    public void checkStop() {
        if (Thread.interrupted())
            throw new RuntimeInterruptedException();
    }
    
    @Override
    public boolean isIndeterminate() {
        return false;
    }


}
