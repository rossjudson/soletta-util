package com.soletta.seek.util;

import java.io.File;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public interface ShellMXBean extends LauncherMXBean {

    /**
     * Method listFiles.
     * 
     * @param location
     *            File
     * @param recursive
     *            boolean
     * @param onlyDirectories
     *            boolean
     * @throws Exception
     */
    public abstract void listFiles(File location, boolean recursive, boolean onlyDirectories) throws Exception;

}
