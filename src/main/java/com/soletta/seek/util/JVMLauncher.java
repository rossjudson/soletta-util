package com.soletta.seek.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.management.NotificationBroadcasterSupport;
import javax.management.StandardEmitterMBean;

/**
 * Assists with running a JVM class in a child process.
 * 
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public class JVMLauncher extends Launcher implements JVMLauncherMXBean {

    private String separator = System.getProperty("file.separator");
    protected String classpath = System.getProperty("java.class.path");
    protected String javaExePath = System.getProperty("java.home") + separator + "bin" + separator + "java";
    protected File securityPolicyFile;
    protected boolean noFork;
    protected Class<?> clazz;
    protected String className;
    protected String[] jvmArgs;
    protected LinkedHashMap<String, String> flags = new LinkedHashMap<String, String>();

    /**
     * Builds a launcher for the given class, given some arguments.
     * 
     * @param clazz
     * @param args
     */
    public JVMLauncher(Class<?> clazz, String... args) {
        this.clazz = clazz;
        this.args = args;
    }

    /**
     * Creates a launcher for the given class name and arguments.
     * 
     * @param className
     * @param args
     */
    public JVMLauncher(String className, String... args) {
        this.className = className;
        this.args = args;
    }

    /**
     * Builds a launcher for the given class, using the specfied jvm arguments first.
     * 
     * @param jvmArgs
     * @param clazz
     * @param args
     */
    public JVMLauncher(String[] jvmArgs, Class<?> clazz, String... args) {
        this.jvmArgs = jvmArgs;
        this.clazz = clazz;
        this.args = args;
    }

    /**
     * Launch the configured process.
     * 
     * @return Future<Integer> * @throws IOException * @see com.soletta.seek.util.LauncherMXBean#launch() * @see
     *         com.soletta.seek.util.LauncherMXBean#launch()
     */
    @Override
    synchronized public Future<Integer> launch() throws IOException {
        if (process.get() == null) {

            // The class to be run can be set directly, or by providing its
            // name.
            String canonicalName = clazz != null ? clazz.getCanonicalName() : className;
            assert canonicalName != null;

            if (isNoFork()) {
                throw new UnsupportedOperationException("noFork is not yet implemented.");
            } else {
                List<String> command = new ArrayList<String>();
                command.add(javaExePath);

                addArgs(command, jvmArgs);
                addJvmProperties(command);
                addClassPath(command);
                command.add(canonicalName);
                addFlags(command);

                addArgsAndExecute(command);
            }
        }

        return this;
    }

    /**
     * Method assembleCommand.
     * 
     * @param command
     *            List<String>
     */
    @Override
    protected void assembleCommand(List<String> command) {
        // The class to be run can be set directly, or by providing its
        // name.
        String canonicalName = clazz != null ? clazz.getCanonicalName() : className;
        assert canonicalName != null;

        if (isNoFork()) {
            throw new UnsupportedOperationException("noFork is not yet implemented.");
        } else {
            command.add(javaExePath);

            addArgs(command, jvmArgs);
            addJvmProperties(command);
            addClassPath(command);
            command.add(canonicalName);
            addFlags(command);
        }
    }

    /**
     * Method addFlags.
     * 
     * @param command
     *            List<String>
     */
    private void addFlags(List<String> command) {
        for (Map.Entry<String, String> f : flags.entrySet()) {
            command.add("-" + f.getKey());
            if (f.getValue() != null) {
                command.add(f.getValue());
            }
        }
    }

    /**
     * Method addClassPath.
     * 
     * @param command
     *            List<String>
     */
    private void addClassPath(List<String> command) {
        command.add("-cp");
        command.add(classpath);
    }

    /**
     * Method addJvmProperties.
     * 
     * @param command
     *            List<String>
     */
    private void addJvmProperties(List<String> command) {
        if (securityPolicyFile != null)
            properties.put("java.security.policy", securityPolicyFile.getPath());

        if (!properties.isEmpty())
            for (Map.Entry<String, String> entry : properties.entrySet())
                command.add("-D" + entry.getKey() + '=' + entry.getValue());
    }

    /**
     * Method getClasspath.
     * 
     * @return String * @see com.soletta.seek.util.JVMLauncherMXBean#getClasspath()
     */
    @Override
    public String getClasspath() {
        return classpath;
    }

    /**
     * Method setClasspath.
     * 
     * @param classpath
     *            String
     * 
     @see com.soletta.seek.util.JVMLauncherMXBean#setClasspath(String)
     */
    @Override
    public void setClasspath(String classpath) {
        this.classpath = classpath;
    }

    /**
     * Retrieves the Logger currently in use, or null if there isn't one.
     * 
     * @return Logger
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Tells the launcher to use the Java environment configured in the environment variable.
     * 
     * @param environmentVariable
     *            .
     * @return boolean
     */
    public boolean useJavaHome(String environmentVariable) {
        String javaHome = System.getenv().get(environmentVariable);
        if (javaHome != null) {
            File f = new File(javaHome);
            if (f.exists() && f.isDirectory() && new File(f, "bin").exists()) {
                setJavaExePath(f + "/bin/java");
                return true;
            }
        }
        return false;
    }

    /**
     * Use JAVA_HOME to configure the java executable.
     * 
     * @return boolean
     */
    public boolean useJavaHome() {
        return useJavaHome("JAVA_HOME");
    }

    /**
     * If you set a logger, status messages will be routed to it.
     * 
     * @param logger
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * Add a flagged parameter to the execution, like "-outfile somewhere.txt". The flagName is prefixed with -, and the
     * value is passed as the next argument.
     * 
     * @param flagName
     * @param flagValue
     *            If null, the flagName alone will be passed.
     */
    public void addFlag(String flagName, Object flagValue) {
        if (flagName == null)
            throw new IllegalArgumentException("Can't pass null flagName");
        // The flags are stored in a LinkedHashMap, so we are retaining
        // the order they are added.
        flags.put(flagName, flagValue.toString());
    }

    /**
     * Add a flag without a value to the command line, like "-debug".
     * 
     * @param flagName
     */
    public void addFlag(String flagName) {
        addFlag(flagName, null);
    }

    /**
     * Add an equals-style flag parameter to the execution, like "-outfile=somewhere.txt".
     * 
     * @param flagName
     *            String
     * @param flagValue
     *            Object
     */
    public void addEquals(String flagName, Object flagValue) {
        flags.put(flagName + '=' + flagValue, null);
    }

    /**
     * The properties listed in this map will be added to the command line, in the -Dname=value form.
     * 
     * @return Map<String,String>
     */
    public Map<String, String> getProperties() {
        return properties;
    }

    /**
     * Method getSecurityPolicyFile.
     * 
     * @return File
     */
    public File getSecurityPolicyFile() {
        return securityPolicyFile;
    }

    /**
     * Method setSecurityPolicyFile.
     * 
     * @param securityPolicyFile
     *            File
     */
    public void setSecurityPolicyFile(File securityPolicyFile) {
        this.securityPolicyFile = securityPolicyFile;
    }

    /**
     * Method getJavaExePath.
     * 
     * @return String * @see com.soletta.seek.util.JVMLauncherMXBean#getJavaExePath()
     */
    @Override
    public String getJavaExePath() {
        return javaExePath;
    }

    /**
     * Method setJavaExePath.
     * 
     * @param path
     *            String
     * 
     @see com.soletta.seek.util.JVMLauncherMXBean#setJavaExePath(String)
     */
    @Override
    public void setJavaExePath(String path) {
        this.javaExePath = path;
    }

    /**
     * Method getClazz.
     * 
     * @return Class<?>
     */
    public Class<?> getClazz() {
        return clazz;
    }

    /**
     * Method setClazz.
     * 
     * @param clazz
     *            Class<?>
     */
    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * Method getClassName.
     * 
     * @return String * @see com.soletta.seek.util.JVMLauncherMXBean#getClassName()
     */
    @Override
    public String getClassName() {
        return className;
    }

    /**
     * Method setClassName.
     * 
     * @param className
     *            String
     * 
     @see com.soletta.seek.util.JVMLauncherMXBean#setClassName(String)
     */
    @Override
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Method getJvmArgs.
     * 
     * @return String[] * @see com.soletta.seek.util.JVMLauncherMXBean#getJvmArgs()
     */
    @Override
    public String[] getJvmArgs() {
        return jvmArgs;
    }

    /**
     * Method setJvmArgs.
     * 
     * @param jvmArgs
     *            String[]
     * 
     @see com.soletta.seek.util.JVMLauncherMXBean#setJvmArgs(String[])
     */
    @Override
    public void setJvmArgs(String[] jvmArgs) {
        this.jvmArgs = jvmArgs;
    }

    /**
     * Method setNoFork.
     * 
     * @param noFork
     *            boolean
     */
    public void setNoFork(boolean noFork) {
        this.noFork = noFork;
    }

    /**
     * Method isNoFork.
     * 
     * @return boolean
     */
    public boolean isNoFork() {
        return noFork;
    }

    /**
     * Method createMBean.
     * 
     * @return StandardEmitterMBean
     */
    protected StandardEmitterMBean createMBean() {
        return new StandardEmitterMBean(this, JVMLauncherMXBean.class, true, new NotificationBroadcasterSupport());
    }

}
