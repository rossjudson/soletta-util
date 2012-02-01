package test.util;

import com.soletta.seek.util.args.ArgException;
import com.soletta.seek.util.args.LibArgsGUI;

public class ArgsGUI {

    public static void main(String [] args) throws ArgException {
        
        SimpleConfig sc = new SimpleConfig();
        
        LibArgsGUI gui = new LibArgsGUI(sc);
        gui.show(true);
        
        
    }
}
