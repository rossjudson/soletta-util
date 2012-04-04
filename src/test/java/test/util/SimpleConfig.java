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

/**
 */
@Args({ @Arg(name = "addr", parseClass = InetAddress.class, parseMethod = "getByName") })
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

    /**
     * Method getColor.
     * 
     * @return Color
     */
    @Arg(description = "A color to set")
    public Color getColor() {
        return color;
    }

    /**
     * Method setColor.
     * 
     * @param color
     *            Color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Method getName.
     * 
     * @return String
     */
    @Arg(description = "Put in a name")
    public String getName() {
        return name;
    }

    /**
     * Method setName.
     * 
     * @param name
     *            String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method getDepth.
     * 
     * @return int
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Method setDepth.
     * 
     * @param depth
     *            int
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    // switches are booleans; the presence of the switch results in the boolean
    // property being set to true.
    /**
     * Method isVerbose.
     * 
     * @return boolean
     */
    @Arg(description = "Talk a lot")
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * Method setVerbose.
     * 
     * @param verbose
     *            boolean
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    // Tells LibArgs which static method to use, to get an InetAddress from the
    // string.
    /**
     * Method getAddr.
     * 
     * @return InetAddress
     */
    public InetAddress getAddr() {
        return addr;
    }

    /**
     * Method setAddr.
     * 
     * @param addr
     *            InetAddress
     */
    public void setAddr(InetAddress addr) {
        this.addr = addr;
    }

    // Flags this argument as required, at position 0
    /**
     * Method getInput.
     * 
     * @return String
     */
    @Arg(pos = 0)
    public String getInput() {
        return input;
    }

    /**
     * Method setInput.
     * 
     * @param input
     *            String
     */
    public void setInput(String input) {
        this.input = input;
    }

    // Indicates this argument accepts the remaining unnamed parameters
    // Each parameter is put into the list.
    // Datatype conversion is done on the list, so it doesn't have to be
    // strings.
    /**
     * Method getFiles.
     * 
     * @return List<String>
     */
    @Arg(remaining = true)
    public List<String> getFiles() {
        return files;
    }

    /**
     * Method setFiles.
     * 
     * @param files
     *            List<String>
     */
    public void setFiles(List<String> files) {
        this.files = files;
    }

    /**
     * Method getToast.
     * 
     * @return String
     */
    public String getToast() {
        return toast;
    }

    /**
     * Method setToast.
     * 
     * @param toast
     *            String
     */
    public void setToast(String toast) {
        this.toast = toast;
    }

    /**
     * Method getBigDecimal.
     * 
     * @return BigDecimal
     */
    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    /**
     * Method setBigDecimal.
     * 
     * @param bigDecimal
     *            BigDecimal
     */
    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    /**
     * Method getBigInteger.
     * 
     * @return BigInteger
     */
    public BigInteger getBigInteger() {
        return bigInteger;
    }

    /**
     * Method setBigInteger.
     * 
     * @param bigInteger
     *            BigInteger
     */
    public void setBigInteger(BigInteger bigInteger) {
        this.bigInteger = bigInteger;
    }

    /**
     * Method getCnum.
     * 
     * @return CNum
     */
    public CNum getCnum() {
        return cnum;
    }

    /**
     * Method setCnum.
     * 
     * @param cnum
     *            CNum
     */
    public void setCnum(CNum cnum) {
        this.cnum = cnum;
    }

    /**
     * Method getFile.
     * 
     * @return File
     */
    public File getFile() {
        return file;
    }

    /**
     * Method setFile.
     * 
     * @param file
     *            File
     */
    public void setFile(File file) {
        this.file = file;
    }

}