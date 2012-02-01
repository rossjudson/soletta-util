package com.soletta.seek.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
import javax.management.StandardEmitterMBean;
import javax.management.StandardMBean;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public class Launcher implements Future<Integer>, LauncherMXBean, Callable<Integer> {

    protected Logger logger;
    protected boolean redirectErrorStream;
    protected String[] args;
    protected Map<String, String> properties = new HashMap<String, String>();
    protected InputStream stdin;
    protected OutputStream stdout;
    protected OutputStream stderr;
    protected ThreadFactory threadFactory = Threads.groupedThreadFactory("JVMLauncher");
    protected AtomicReference<Process> process = new AtomicReference<Process>();
    protected Pump stdinPump;
    protected Pump stdoutPump;
    protected Pump stderrPump;
    protected AtomicLong inBytes = new AtomicLong();
    protected AtomicLong outBytes = new AtomicLong();
    protected AtomicLong errBytes = new AtomicLong();
    private ObjectName objectName;
    private transient StandardMBean mbean;

    public Launcher() {

    }

    /**
     * Constructor for Launcher.
     * 
     * @param args
     *            String[]
     */
    public Launcher(String... args) {
        this.args = args;
    }

    /**
     * Method toString.
     * 
     * @return String
     */
    public String toString() {
        return "Launcher " + args;
    }

    /**
     * Method getInBytes.
     * 
     * @return long
     * @see com.soletta.seek.util.LauncherMXBean#getInBytes()
     */
    @Override
    public long getInBytes() {
        return inBytes.get();
    }

    /**
     * Method getOutBytes.
     * 
     * @return long
     * @see com.soletta.seek.util.LauncherMXBean#getOutBytes()
     */
    @Override
    public long getOutBytes() {
        return outBytes.get();
    }

    /**
     * Method getErrBytes.
     * 
     * @return long
     * @see com.soletta.seek.util.LauncherMXBean#getErrBytes()
     */
    @Override
    public long getErrBytes() {
        return errBytes.get();
    }

    /**
     * Method getStdout.
     * 
     * @return OutputStream
     */
    public OutputStream getStdout() {
        return stdout;
    }

    /**
     * Method setStdout.
     * 
     * @param stdout
     *            OutputStream
     */
    public void setStdout(OutputStream stdout) {
        this.stdout = stdout;
    }

    /**
     * Method getStderr.
     * 
     * @return OutputStream
     */
    public OutputStream getStderr() {
        return stderr;
    }

    /**
     * Method setStderr.
     * 
     * @param stderr
     *            OutputStream
     */
    public void setStderr(OutputStream stderr) {
        this.stderr = stderr;
    }

    /**
     * Method getStdin.
     * 
     * @return InputStream
     */
    public InputStream getStdin() {
        return stdin;
    }

    /**
     * Method setStdin.
     * 
     * @param stdin
     *            InputStream
     */
    public void setStdin(InputStream stdin) {
        this.stdin = stdin;
    }

    /**
     * Method getThreadFactory.
     * 
     * @return ThreadFactory
     */
    public ThreadFactory getThreadFactory() {
        return threadFactory;
    }

    /**
     * Method setThreadFactory.
     * 
     * @param threadFactory
     *            ThreadFactory
     */
    public void setThreadFactory(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
    }

    /**
     * Method isRedirectErrorStream.
     * 
     * @return boolean * @see com.soletta.seek.util.LauncherMXBean#isRedirectErrorStream()
     */
    @Override
    public boolean isRedirectErrorStream() {
        return redirectErrorStream;
    }

    /**
     * Method setRedirectErrorStream.
     * 
     * @param redirectErrorStream
     *            boolean
     * 
     @see com.soletta.seek.util.LauncherMXBean#setRedirectErrorStream(boolean)
     */
    @Override
    public void setRedirectErrorStream(boolean redirectErrorStream) {
        this.redirectErrorStream = redirectErrorStream;
    }

    /**
     * Method addArgs.
     * 
     * @param command
     *            List<String>
     * @param argArray
     *            String[]
     */
    protected void addArgs(List<String> command, String... argArray) {
        if (argArray != null)
            for (String arg : argArray)
                command.add(arg);
    }

    // @Override
    // public Integer call() throws InterruptedException, ExecutionException {
    // if (process.get() == null)
    // try {
    // launch();
    // } catch (IOException ioe) {
    // throw new ExecutionException(ioe);
    // }
    // try {
    // return result.get();
    // } catch (InterruptedException e) {
    // if (logger != null)
    // logger.log(Level.FINE, "Exception from launched process", e);
    // throw e;
    // } finally {
    // // cleanup of the process, etc?
    // }
    // }

    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    protected class Pump implements Runnable {

        private final InputStream in;
        private final OutputStream out;
        private final boolean monitor;
        private final AtomicLong counter;

        /**
         * Constructor for Pump.
         * 
         * @param monitor
         *            boolean
         * @param in
         *            InputStream
         * @param counter
         *            AtomicLong
         */
        Pump(boolean monitor, InputStream in, AtomicLong counter) {
            this(monitor, in, null, counter);
        }

        /**
         * Constructor for Pump.
         * 
         * @param monitor
         *            boolean
         * @param in
         *            InputStream
         * @param out
         *            OutputStream
         * @param counter
         *            AtomicLong
         */
        Pump(boolean monitor, InputStream in, OutputStream out, AtomicLong counter) {
            assert in != null;
            this.monitor = monitor;
            this.in = in;
            this.out = out;
            this.counter = counter;
        }

        /**
         * Method run.
         * 
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {

            if (logger != null)
                logger.fine("Pump startup");

            try {
                byte[] buffer = new byte[16000];

                try {
                    int read = in.read(buffer);
                    while (read >= 0) {
                        if (out != null) {
                            counter.addAndGet(read);
                            out.write(buffer, 0, read);
                        }
                        read = in.read(buffer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (out != null) {
                    try {
                        out.flush();
                        // out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (monitor) {
                    try {
                        int exitCode = process.get().waitFor();
                        if (logger != null)
                            logger.fine("Received status code " + exitCode);
                    } catch (InterruptedException e) {
                        setException(e);
                    }

                }
                if (logger != null)
                    logger.fine("Pump exit");
            }
        }

    }

    /**
     * Method launch.
     * 
     * @return Future<Integer> * @throws IOException * @see com.soletta.seek.util.LauncherMXBean#launch()
     */
    @Override
    synchronized public Future<Integer> launch() throws IOException {
        if (process.get() == null) {
            List<String> command = new ArrayList<String>();
            assembleCommand(command);
            addArgsAndExecute(command);
        }
        return this;
    }

    /**
     * Method assembleCommand.
     * 
     * @param command
     *            List<String>
     */
    protected void assembleCommand(List<String> command) {
    }

    /**
     * Method addArgsAndExecute.
     * 
     * @param command
     *            List<String>
     * @throws IOException
     */
    protected void addArgsAndExecute(List<String> command) throws IOException {
        addArgs(command, args);
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(redirectErrorStream);
        Process started = processBuilder.start();
        process.set(started);

        stdoutPump = new Pump(true, started.getInputStream(), stdout, outBytes);
        threadFactory.newThread(stdoutPump).start();
        if (stdin != null) {
            // If we were provide an input stream for this process,
            // connect
            // it and
            // start a pump.
            stdinPump = new Pump(false, stdin, started.getOutputStream(), inBytes);
            threadFactory.newThread(stdinPump).start();
        }
        if (!redirectErrorStream) {
            stderrPump = new Pump(false, started.getErrorStream(), stderr, errBytes);
            threadFactory.newThread(stderrPump).start();
        }
    }

    /**
     * Method getObjectName.
     * 
     * @return ObjectName
     */
    public ObjectName getObjectName() {
        return objectName;
    }

    /**
     * If you set an ObjectName, the JVMLauncher will register itself under that ObjectName with the platform JMX
     * server. You'll be able to see some statistics about the launcher.
     * 
     * @param objectName
     */
    public void setObjectName(ObjectName objectName) {
        this.objectName = objectName;

        if (objectName != null) {
            mbean = createMBean();
            try {
                ManagementFactory.getPlatformMBeanServer().registerMBean(mbean, objectName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Method createMBean.
     * 
     * @return StandardEmitterMBean
     */
    protected StandardEmitterMBean createMBean() {
        return new StandardEmitterMBean(this, LauncherMXBean.class, true, new NotificationBroadcasterSupport());
    }

    AtomicBoolean cancelled = new AtomicBoolean();
    AtomicReference<GetThread> getThread = new AtomicReference<GetThread>();
    AtomicReference<InterruptedException> exception = new AtomicReference<InterruptedException>();

    /**
     * Method cancel.
     * 
     * @param mayInterruptIfRunning
     *            boolean
     * @return boolean
     * @see java.util.concurrent.Future#cancel(boolean)
     */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        Process p = process.get();
        if (p != null) {
            try {
                p.exitValue();
                return false;
            } catch (IllegalThreadStateException state) {
                if (cancelled.compareAndSet(false, true)) {
                    exception.set(new InterruptedException());
                    // means it hasn't been terminated.
                    process.get().destroy();
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    /**
     * Method isCancelled.
     * 
     * @return boolean
     * @see java.util.concurrent.Future#isCancelled()
     */
    @Override
    public boolean isCancelled() {
        return cancelled.get();
    }

    /**
     * Method isDone.
     * 
     * @return boolean
     * @see java.util.concurrent.Future#isDone()
     */
    @Override
    public boolean isDone() {
        try {
            Process p = process.get();
            p.exitValue();
            return true;
        } catch (IllegalThreadStateException state) {
            return false;
        }
    }

    /**
     * Method setException.
     * 
     * @param ie
     *            InterruptedException
     */
    public void setException(InterruptedException ie) {
        exception.set(ie);
    }

    /**
     * Method get.
     * 
     * @return Integer * @throws InterruptedException * @throws ExecutionException
     * @see java.util.concurrent.Future#get()
     */
    @Override
    public Integer get() throws InterruptedException, ExecutionException {
        InterruptedException ex = exception.get();
        if (ex != null)
            throw ex;
        try {
            launch();
        } catch (IOException e) {
            throw new ExecutionException(e);
        }
        Process p = process.get();
        return p.waitFor();
    }

    /**
     * Method get.
     * 
     * @param timeout
     *            long
     * @param unit
     *            TimeUnit
     * @return Integer * @throws InterruptedException * @throws TimeoutException * @throws ExecutionException * @see
     *         java.util.concurrent.Future#get(long, TimeUnit)
     */
    @Override
    public Integer get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
        GetThread gt = getThread();
        gt.join(unit.toMillis(timeout));
        return get();
    }

    /**
     * Method getThread.
     * 
     * @return GetThread
     */
    private GetThread getThread() {
        GetThread gt = getThread.get();
        if (gt == null) {
            gt = new GetThread(process.get());
            if (!getThread.compareAndSet(null, gt))
                gt = getThread.get();
        }
        return gt;
    }

    /**
     * GetThread is started if someone calls get(timeout). Since we can't do a timed-out waitFor on a process, we can
     * start a second thread that will do the waitFor, for us. Rather than attempt to cancel this if the original
     * get(timeout) request lapses, we'll just let it sit there and lapse on its own.
     * 
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    static class GetThread extends Thread {
        private final Process p;

        /**
         * Constructor for GetThread.
         * 
         * @param p
         *            Process
         */
        GetThread(Process p) {
            super("Launcher.GetThread");
            this.p = p;
            setDaemon(true);
        }

        /**
         * Method run.
         * 
         * @see java.lang.Runnable#run()
         */
        public void run() {
            try {
                p.waitFor();
            } catch (InterruptedException e) {

            }
        }
    }

    /**
     * Method call.
     * 
     * @return Integer * @throws InterruptedException * @throws ExecutionException
     * @see java.util.concurrent.Callable#call()
     */
    @Override
    public Integer call() throws InterruptedException, ExecutionException {
        return get();
    }

}
