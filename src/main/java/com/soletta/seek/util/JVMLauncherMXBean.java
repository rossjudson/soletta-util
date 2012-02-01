package com.soletta.seek.util;


public interface JVMLauncherMXBean extends LauncherMXBean {

    public void setJvmArgs(String[] jvmArgs);

    public String[] getJvmArgs();

    public void setClassName(String className);

    public String getClassName();

    public void setJavaExePath(String path);

    public String getJavaExePath();

    public void setClasspath(String classpath);

    public String getClasspath();

}
