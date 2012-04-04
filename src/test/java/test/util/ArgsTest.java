package test.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

import com.soletta.seek.util.args.ArgException;
import com.soletta.seek.util.args.LibArgs;

/**
 */
public class ArgsTest {

    /**
     * Method simpleConfig.
     * 
     * @throws ArgException
     * @throws UnknownHostException
     */
    @Test
    public void simpleConfig() throws ArgException, UnknownHostException {
        SimpleConfig sc = new SimpleConfig();
        Set<String> processed = LibArgs.process(sc, "-name", "ross", "-depth", "12", "-v", "-addr", "192.168.1.1",
                "-toast=bread", "-cnum=bread", "-file=somefile.txt", "-bigDecimal=44.2", "-color=#343434", "file1.txt",
                "file2.txt");

        assertEquals("ross", sc.getName());
        assertEquals(12, sc.getDepth());
        assertEquals(true, sc.isVerbose());
        assertEquals("bread", sc.getToast());
        assertEquals(InetAddress.getByName("192.168.1.1"), sc.getAddr());
        assertEquals(Arrays.asList("file1.txt", "file2.txt"), sc.getFiles());
        assertEquals(CNum.BREAD, sc.getCnum());
        assertEquals(new File("somefile.txt"), sc.getFile());

        assertEquals(10, processed.size());
    }
}
