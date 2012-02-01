package test.util;

import java.awt.Color;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import com.soletta.seek.util.args.Arg;
import com.soletta.seek.util.args.Args;

@Args({
    @Arg(name="addr", parseClass=InetAddress.class, parseMethod="getByName")
    })
public class SimpleConfig {
    
    private String name;
    private int depth;
    private boolean verbose;
    private String input;
    private InetAddress addr;
    private String toast;
    private BigDecimal bigDecimal;
    private BigInteger bigInteger;
    private CNum cnum;
    private File file;
    private Color color;
    
    private List<String> files = new ArrayList<String>();
    
    @Arg(description="A color to set")
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    @Arg(description="Put in a name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getDepth() {
        return depth;
    }
    public void setDepth(int depth) {
        this.depth = depth;
    }
    
    // switches are booleans; the presence of the switch results in the boolean 
    // property being set to true.
    @Arg(description="Talk a lot")
    public boolean isVerbose() {
        return verbose;
    }
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    // Tells LibArgs which static method to use, to get an InetAddress from the string.
    public InetAddress getAddr() {
        return addr;
    }
    public void setAddr(InetAddress addr) {
        this.addr = addr;
    }
    
    // Flags this argument as required, at position 0
    @Arg(pos=0)
    public String getInput() {
        return input;
    }
    public void setInput(String input) {
        this.input = input;
    }
    // Indicates this argument accepts the remaining unnamed parameters
    // Each parameter is put into the list.
    // Datatype conversion is done on the list, so it doesn't have to be
    // strings.
    @Arg(remaining = true)
    public List<String> getFiles() {
        return files;
    }
    public void setFiles(List<String> files) {
        this.files = files;
    }
    public String getToast() {
        return toast;
    }
    public void setToast(String toast) {
        this.toast = toast;
    }
    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }
    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }
    public BigInteger getBigInteger() {
        return bigInteger;
    }
    public void setBigInteger(BigInteger bigInteger) {
        this.bigInteger = bigInteger;
    }
    public CNum getCnum() {
        return cnum;
    }
    public void setCnum(CNum cnum) {
        this.cnum = cnum;
    }
    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }
    
}