/**
 * 
 */

package nep;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import nep.swing.NepFrame;

public class MainEntity{
    /**
     * Runs the program on the EDT (Event Dispatch Thread)
     * 
     * @param args
     * @return void
     */
    @SuppressWarnings("Convert2Lambda")
    public static void main(String[] args){
       SwingUtilities.invokeLater(new Runnable(){
        @Override
        public void run(){
            NepFrame frame = new NepFrame();
            frame.setVisible(true);

            ImageIcon logo = new ImageIcon("logo.png");
            frame.setIconImage(logo.getImage());
        }
       });
    }
}
