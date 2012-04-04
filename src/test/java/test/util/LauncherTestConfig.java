package test.util;

import com.soletta.seek.util.args.Usage;

/**
 */
@Usage("Configures the test launcher")
public class LauncherTestConfig {

    private String hello, goodbye;
    private String err;
    private Integer exitCode;
    private int iterations = 1;
    private int delaySeconds = 0;
    private boolean printProperties;
    private boolean usage;

    /**
     * Method isUsage.
     * 
     * @return boolean
     */
    public boolean isUsage() {
        return usage;
    }

    /**
     * Method setUsage.
     * 
     * @param usage
     *            boolean
     */
    public void setUsage(boolean usage) {
        this.usage = usage;
    }

    /**
     * Method getErr.
     * 
     * @return String
     */
    public String getErr() {
        return err;
    }

    /**
     * Method setErr.
     * 
     * @param err
     *            String
     */
    public void setErr(String err) {
        this.err = err;
    }

    /**
     * Method isPrintProperties.
     * 
     * @return boolean
     */
    public boolean isPrintProperties() {
        return printProperties;
    }

    /**
     * Method setPrintProperties.
     * 
     * @param printProperties
     *            boolean
     */
    public void setPrintProperties(boolean printProperties) {
        this.printProperties = printProperties;
    }

    /**
     * Method getHello.
     * 
     * @return String
     */
    public String getHello() {
        return hello;
    }

    /**
     * Method setHello.
     * 
     * @param hello
     *            String
     */
    public void setHello(String hello) {
        this.hello = hello;
    }

    /**
     * Method getExitCode.
     * 
     * @return Integer
     */
    public Integer getExitCode() {
        return exitCode;
    }

    /**
     * Method setExitCode.
     * 
     * @param exitCode
     *            Integer
     */
    public void setExitCode(Integer exitCode) {
        this.exitCode = exitCode;
    }

    /**
     * Method getIterations.
     * 
     * @return int
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * Method setIterations.
     * 
     * @param iterations
     *            int
     */
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    /**
     * Method getDelaySeconds.
     * 
     * @return int
     */
    public int getDelaySeconds() {
        return delaySeconds;
    }

    /**
     * Method setDelaySeconds.
     * 
     * @param delaySeconds
     *            int
     */
    public void setDelaySeconds(int delaySeconds) {
        this.delaySeconds = delaySeconds;
    }

    /**
     * Method getGoodbye.
     * 
     * @return String
     */
    public String getGoodbye() {
        return goodbye;
    }

    /**
     * Method setGoodbye.
     * 
     * @param goodbye
     *            String
     */
    public void setGoodbye(String goodbye) {
        this.goodbye = goodbye;
    }

}
