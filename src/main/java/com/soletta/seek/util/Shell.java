package com.soletta.seek.util;

import java.io.File;

import javax.management.NotificationBroadcasterSupport;
import javax.management.StandardEmitterMBean;

/** Shell extends launcher with simple-to-use methods for performing common 
 * shell tasks, on a cross-platform basis.
 * 
 * @author rjudson
 *
 */
public class Shell extends Launcher implements ShellMXBean {

	public enum Platform { WINDOWS, UNIX };
	Platform platform;

	/** Create a shell helper, determining the platform by looking at
	 * system properties.
	 */
	public Shell() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.startsWith("windows"))
			platform = Platform.WINDOWS;
		else
			platform = Platform.UNIX;
	}

	/** Create a shell helper, designating the platform.
	 * 
	 * @param platform
	 */
	public Shell(Platform platform) {
		this.platform = platform;
	}

	/** Requests that this Shell list directory contents, possibly recursively, by 
	 * using a shell command appropriate to the current OS. The results will be routed
	 * to whatever output is currently set.
	 * 
	 * @param location
	 * @param recursive
	 * @param onlyDirectories
	 * @throws Exception 
	 */
	@Override
	public void listFiles(File location, boolean recursive, boolean onlyDirectories) throws Exception {
		String path = location.getCanonicalPath();
		
		switch (platform) {
		case UNIX:
			/* directories only
			 * find . -type d
			 */
			args = new String[] { "find", quote(path), "-type", "d" };
			break;
		case WINDOWS:
			args = new String[] { "cmd", "/C", quote("dir /-C /s " + path) };
			break;
		}
		
		call();
	}
	
	private String quote(String s) {
		return "\"" + s + '"';
	}
	
	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	@Override
	protected StandardEmitterMBean createMBean() {
		return new StandardEmitterMBean(this, ShellMXBean.class,
				true, new NotificationBroadcasterSupport());
	}
	
}
