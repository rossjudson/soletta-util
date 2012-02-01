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
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
import javax.management.StandardEmitterMBean;
import javax.management.StandardMBean;

import org.slf4j.Logger;

public class Launcher implements Callable<Integer>, LauncherMXBean {

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
	protected SettableFuture<Integer> result = new SettableFuture<Integer>();
	protected AtomicLong inBytes = new AtomicLong();
	protected AtomicLong outBytes = new AtomicLong();
	protected AtomicLong errBytes = new AtomicLong();
	private ObjectName objectName;
	private transient StandardMBean mbean;


	public Launcher() {
		
	}
	
	public Launcher(String...args) {
		this.args = args;
	}
	
	public String toString() {
		return "Launcher " + args;
	}
	
	@Override
	public long getInBytes() {
	    return inBytes.get();
	}

	@Override
	public long getOutBytes() {
	    return outBytes.get();
	}

	@Override
	public long getErrBytes() {
	    return errBytes.get();
	}

	public OutputStream getStdout() {
	    return stdout;
	}

	public void setStdout(OutputStream stdout) {
	    this.stdout = stdout;
	}

	public OutputStream getStderr() {
	    return stderr;
	}

	public void setStderr(OutputStream stderr) {
	    this.stderr = stderr;
	}

	public InputStream getStdin() {
	    return stdin;
	}

	public void setStdin(InputStream stdin) {
	    this.stdin = stdin;
	}

	public ThreadFactory getThreadFactory() {
	    return threadFactory;
	}

	public void setThreadFactory(ThreadFactory threadFactory) {
	    this.threadFactory = threadFactory;
	}

	@Override
	public boolean isRedirectErrorStream() {
	    return redirectErrorStream;
	}

	@Override
	public void setRedirectErrorStream(boolean redirectErrorStream) {
	    this.redirectErrorStream = redirectErrorStream;
	}

	/**
	 * Attempts to kill the child process, if it is running.
	 * 
	 */
	@Override
	public void destroy() {
	    Process p = process.get();
	    if (p != null) {
	        p.destroy();
	        // Let anything waiting for us know that we're done.
	        result.setException(new InterruptedException("Child process destroyed"));
	    }
	}

	protected void addArgs(List<String> command, String... argArray) {
	    if (argArray != null)
	        for (String arg : argArray)
	            command.add(arg);
	}

	@Override
	public Integer call() throws Exception {
	    if (process.get() == null)
	        launch();
	
	    try {
	        return result.get();
	    } catch (Exception e) {
	        if (logger != null)
	            logger.warn("Exception from launched process", e);
	        throw e;
	    } finally {
	        // cleanup of the process, etc?
	    }
	}

	@Override
	public boolean isDone() {
	    return result != null && result.isDone();
	}

    protected class Pump implements Runnable {

        private final InputStream in;
        private final OutputStream out;
        private final boolean monitor;
        private final AtomicLong counter;

        Pump(boolean monitor, InputStream in, AtomicLong counter) {
            this(monitor, in, null, counter);
        }

        Pump(boolean monitor, InputStream in, OutputStream out, AtomicLong counter) {
            assert in != null;
            this.monitor = monitor;
            this.in = in;
            this.out = out;
            this.counter = counter;
        }

        @Override
        public void run() {

            if (logger != null)
                logger.debug("Pump startup");

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
                        result.set(exitCode);
                        if (logger != null)
                            logger.debug("Received status code " + exitCode);
                    } catch (InterruptedException e) {
                        result.setException(e);
                    }

                }
                if (logger != null)
                    logger.debug("Pump exit");
            }
        }

    }

	@Override
	public Future<Integer> launch() throws IOException {
		if (process.get() == null) {
			List<String> command = new ArrayList<String>();
			assembleCommand(command);
			addArgsAndExecute(command);
		}
		return result;
	}
	
	protected void assembleCommand(List<String> command) {
	}

	protected void addArgsAndExecute(List<String> command) throws IOException {
		addArgs(command, args);
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		processBuilder.redirectErrorStream(redirectErrorStream);
		Process started = processBuilder.start();
		process.set(started);

		stdoutPump = new Pump(true, started.getInputStream(), stdout,
				outBytes);
		threadFactory.newThread(stdoutPump).start();
		if (stdin != null) {
			// If we were provide an input stream for this process,
			// connect
			// it and
			// start a pump.
			stdinPump = new Pump(false, stdin,
					started.getOutputStream(), inBytes);
			threadFactory.newThread(stdinPump).start();
		}
		if (!redirectErrorStream) {
			stderrPump = new Pump(false, started.getErrorStream(),
					stderr, errBytes);
			threadFactory.newThread(stderrPump).start();
		}
	}

	public ObjectName getObjectName() {
		return objectName;
	}

	/**
	 * If you set an ObjectName, the JVMLauncher will register itself under that
	 * ObjectName with the platform JMX server. You'll be able to see some
	 * statistics about the launcher.
	 * 
	 * @param objectName
	 */
	public void setObjectName(ObjectName objectName) {
		this.objectName = objectName;

		if (objectName != null) {
			mbean = createMBean();
			try {
				ManagementFactory.getPlatformMBeanServer().registerMBean(mbean,
						objectName);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	protected StandardEmitterMBean createMBean() {
		return new StandardEmitterMBean(this, LauncherMXBean.class,
				true, new NotificationBroadcasterSupport());
	}

	
}
