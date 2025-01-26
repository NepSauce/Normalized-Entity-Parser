/**
 * Entry point for the Nep application.
 * 
 * @author Zawad Atif
 * @author Nafisah Nubah
 * 
 */

package nep;

import javax.swing.SwingUtilities;

import nep.swing.NepFrame;

public class MainEntity{
    /**
     * Runs the program on the EDT (Event Dispatch Thread)
     * 
     * @param args Command-line arguments 
     */
    @SuppressWarnings({"Convert2Lambda", "unused"})
    public static void main(String[] args){
       SwingUtilities.invokeLater(new Runnable() {
        @Override
           public void run() {
               NepFrame nepFrame = new NepFrame();
           }
       });
    }
}
