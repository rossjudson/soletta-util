package com.soletta.seek.util;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public interface JVMLauncherMXBean extends LauncherMXBean {

    /**
     * Method setJvmArgs.
     * 
     * @param jvmArgs
     *            String[]
     */
    public void setJvmArgs(String[] jvmArgs);

    /**
     * Method getJvmArgs.
     * 
     * @return String[]
     */
    public String[] getJvmArgs();

    /**
     * Method setClassName.
     * 
     * @param className
     *            String
     */
    public void setClassName(String className);

    /**
     * Method getClassName.
     * 
     * @return String
     */
    public String getClassName();

    /**
     * Method setJavaExePath.
     * 
     * @param path
     *            String
     */
    public void setJavaExePath(String path);

    /**
     * Method getJavaExePath.
     * 
     * @return String
     */
    public String getJavaExePath();

    /**
     * Method setClasspath.
     * 
     * @param classpath
     *            String
     */
    public void setClasspath(String classpath);

    /**
     * Method getClasspath.
     * 
     * @return String
     */
    public String getClasspath();

}
