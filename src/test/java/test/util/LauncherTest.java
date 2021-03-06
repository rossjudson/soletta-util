package test.util;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.soletta.seek.util.JVMLauncher;
import com.soletta.seek.util.Launcher;
import com.soletta.seek.util.args.ArgException;
import com.soletta.seek.util.args.LibArgs;

/**
 */
public class LauncherTest {

    /**
     * Method main.
     * 
     * @param args
     *            String[]
     * @throws ArgException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws ArgException, InterruptedException {
        LauncherTestConfig config = new LauncherTestConfig();

        try {
            LibArgs.process(config, args);
        } catch (ArgException e) {
            System.err.println(LibArgs.usage(config));
            System.exit(-1);
        }

        try {

            if (config.getHello() != null)
                System.out.println(config.getHello());
            if (config.getErr() != null)
                System.err.println(config.getErr());

            if (config.isPrintProperties()) {
                TreeMap<Object, Object> props = new TreeMap<Object, Object>(System.getProperties());
                for (Map.Entry<Object, Object> entry : props.entrySet()) {
                    System.out.format("%s %s\n", entry.getKey(), entry.getValue());
                }
            }

            Thread.sleep(config.getDelaySeconds() * 1000L);

        } finally {

            if (config.getGoodbye() != null)
                System.out.println(config.getGoodbye());
            System.out.flush();
            System.err.flush();

            if (config.getExitCode() != null)
                System.exit(config.hashCode());
            else
                System.exit(0);
        }
    }

    @Before
    public void before() {
    }

    @After
    public void after() {
        System.out.println("---------------------------------------------------");
    }

    /**
     * Method checkUsage.
     * 
     * @throws Exception
     */
    @Test
    public void checkUsage() throws Exception {
        JVMLauncher launch = new JVMLauncher(LauncherTest.class);
        launch.setStdout(System.out);
        launch.setStderr(System.err);
        launch.addFlag("wont", "exist");
        Integer result = launch.call();
        System.out.println("CheckUsage exit code is " + result);
        assertTrue(result != 0);
    }

    /**
     * Method redirect.
     * 
     * @throws Exception
     */
    @Test
    public void redirect() throws Exception {

        JVMLauncher launch = new JVMLauncher(LauncherTest.class);
        launch.addFlag("hello", "redirect");
        launch.addFlag("err", "I am printed on the error stream");
        launch.addFlag("printProperties", true);
        launch.setStdout(System.out);
        launch.setStderr(System.err);
        Integer result = launch.call();
        System.out.println("Launch result: " + result);
    }

    /**
     * Method delayFlags.
     * 
     * @throws Exception
     */
    @Test
    public void delayFlags() throws Exception {
        JVMLauncher launch = new JVMLauncher(LauncherTest.class);
        launch.addFlag("hello", "delayFlags");
        launch.addFlag("goodbye", "Goodbye");
        launch.setStdout(System.out);
        launch.setStderr(System.err);
        final int delay = 4;
        launch.addFlag("delaySeconds", delay);

        long now = System.currentTimeMillis();
        Integer result = launch.call();
        assertTrue(System.currentTimeMillis() - now > delay * 1000);
        assertTrue(result == 0);
    }

    /**
     * Method delayThenDestroy.
     * 
     * @throws Exception
     */
    @Test(expected = InterruptedException.class)
    public void delayThenDestroy() throws Exception {
        JVMLauncher launch = new JVMLauncher(LauncherTest.class);
        launch.setStdout(System.out);
        launch.setStderr(System.err);
        launch.addFlag("delaySeconds", 10);
        launch.addFlag("hello", "Launched process waits 10 seconds to be destroyed...");
        launch.addFlag("goodbye", "Goodbye");
        launch.launch();

        try {
            System.out.println("Test waits 2 seconds.");
            Thread.sleep(2000);
        } catch (Exception ex) {
            throw new RuntimeException();
        }
        System.out.println("Test is destroying child process");
        launch.cancel(true);
        System.out.println("Test is waiting for exit code; should not receive it. InterruptedException should be thrown.");
        Integer exitCode = launch.call();

        Assert.fail("Shouldn't reach this point -- process wasn't interrupted. Exit code is " + exitCode);
    }

    /**
     * Method launchBadClass.
     * 
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void launchBadClass() throws InterruptedException, ExecutionException {
        // We can't really tell the difference between a bad class name and a
        // process
        // that returns a non-zero exit code, so we don't try too hard.
        JVMLauncher launch = new JVMLauncher("class.not.present");
        launch.setStdout(System.out);
        launch.setStderr(System.err);
        Assert.assertTrue("Exit code should be non-zero", !launch.get().equals(0));
    }

    /**
     * Method runThis.
     * 
     * @throws Exception
     */
    @Test
    public void runThis() throws Exception {

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();

        JVMLauncher launch = new JVMLauncher(LauncherTest.class);
        launch.setStdout(stdout);
        launch.setStderr(stderr);
        Integer result = launch.call();

        System.out.println("Launch result: " + result);
        System.out.println("Stdout:");
        System.out.write(stdout.toByteArray());
        System.out.println("\nStderr:");
        System.out.write(stderr.toByteArray());
    }

    /**
     * Method runDirectory.
     * 
     * @throws Exception
     */
    @Test()
    public void runDirectory() throws Exception {

        String os = System.getProperty("os.name");
        if (os.equals("windows")) {
            ByteArrayOutputStream stdout = new ByteArrayOutputStream();
            ByteArrayOutputStream stderr = new ByteArrayOutputStream();

            Launcher launch = new Launcher("cmd", "/C", "\"dir /-C /s\"");

            launch.setStdout(stdout);
            launch.setStderr(stderr);
            Integer result = launch.call();

            System.out.println("Launch result: " + result);
            System.out.println("Stdout:");
            System.out.write(stdout.toByteArray());
            System.out.println("\nStderr:");
            System.out.write(stderr.toByteArray());
        } else {
            System.out.println("Skipping directory test when not on windows.");
        }
    }
}
