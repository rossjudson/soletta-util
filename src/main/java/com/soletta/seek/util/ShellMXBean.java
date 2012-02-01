package com.soletta.seek.util;

import java.io.File;

public interface ShellMXBean extends LauncherMXBean {

	public abstract void listFiles(File location, boolean recursive, boolean onlyDirectories)
			throws Exception;

}
