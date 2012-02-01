package com.soletta.seek.util;

import java.io.IOException;
import java.util.concurrent.Future;

public interface LauncherMXBean {

	public abstract boolean isDone();

	public abstract Future<Integer> launch() throws IOException;

	public abstract void setRedirectErrorStream(boolean redirectErrorStream);

	public abstract boolean isRedirectErrorStream();

	public abstract long getErrBytes();

	public abstract long getOutBytes();

	public abstract long getInBytes();

}