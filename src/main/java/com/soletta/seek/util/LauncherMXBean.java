package com.soletta.seek.util;

import java.io.IOException;
import java.util.concurrent.Future;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public interface LauncherMXBean {

    /**
     * Method isDone.
     * 
     * @return boolean
     */
    public abstract boolean isDone();

    /**
     * Method launch.
     * 
     * @return Future<Integer> * @throws IOException
     */
    public abstract Future<Integer> launch() throws IOException;

    /**
     * Method setRedirectErrorStream.
     * 
     * @param redirectErrorStream
     *            boolean
     */
    public abstract void setRedirectErrorStream(boolean redirectErrorStream);

    /**
     * Method isRedirectErrorStream.
     * 
     * @return boolean
     */
    public abstract boolean isRedirectErrorStream();

    /**
     * Method getErrBytes.
     * 
     * @return long
     */
    public abstract long getErrBytes();

    /**
     * Method getOutBytes.
     * 
     * @return long
     */
    public abstract long getOutBytes();

    /**
     * Method getInBytes.
     * 
     * @return long
     */
    public abstract long getInBytes();

}