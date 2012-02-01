package test.util;

import javax.swing.UIManager;

import com.soletta.seek.util.args.LibArgsGUI;

public class ArgsGUI {

    public static void main(String [] args) throws Exception {
        
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        SimpleConfig sc = new SimpleConfig();
        
        LibArgsGUI gui = new LibArgsGUI(sc);
        gui.show(true);
        
        
    }
}
