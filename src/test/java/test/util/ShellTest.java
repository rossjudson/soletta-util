package test.util;

import java.io.File;

import org.junit.Test;

import com.soletta.seek.util.Shell;
import com.soletta.seek.util.ShellMXBean;

public class ShellTest {

	@Test
	public void checkPlatform() {
		ShellMXBean s = new Shell();
		System.out.println(s);
	}
	
	@Test
	public void listFiles() throws Exception {
		Shell s = new Shell();
		s.setStdout(System.out);
		s.setRedirectErrorStream(true);
		s.listFiles(new File("."), true, false);
	}
}
