package nep.swing;

import javax.swing.JFrame;

public class NepFrame extends JFrame{
    /*
     * Default Constructor for NepFrame
     * EXIT_ON_CLOSE
     */
    public NepFrame(){
        setTitle("NEP");
        setSize(800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
