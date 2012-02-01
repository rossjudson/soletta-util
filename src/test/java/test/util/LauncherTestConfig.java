package test.util;

import com.soletta.seek.util.args.Usage;

@Usage("Configures the test launcher")
public class LauncherTestConfig {
    
    private String hello, goodbye;
    private String err;
    private Integer exitCode;
    private int iterations = 1;
    private int delaySeconds = 0;
    private boolean printProperties;
    private boolean usage;

    public boolean isUsage() {
        return usage;
    }

    public void setUsage(boolean usage) {
        this.usage = usage;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public boolean isPrintProperties() {
        return printProperties;
    }

    public void setPrintProperties(boolean printProperties) {
        this.printProperties = printProperties;
    }

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

    public Integer getExitCode() {
        return exitCode;
    }

    public void setExitCode(Integer exitCode) {
        this.exitCode = exitCode;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getDelaySeconds() { 
        return delaySeconds;
    }

    public void setDelaySeconds(int delaySeconds) {
        this.delaySeconds = delaySeconds;
    }

    public String getGoodbye() {
        return goodbye;
    }

    public void setGoodbye(String goodbye) {
        this.goodbye = goodbye;
    }

    
}
