package nep.util;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("unused")
public class DisplayUIError {
    private String errorMessage;
    private int errorType;
    private int length;
    private int errorHeight;
    private int errorWidth;


    public DisplayUIError(String errorMessage, int errorType){
        this.errorMessage = errorMessage;
        this.errorType = errorType;
        this.length = 3;
        this.errorHeight = 120;
        this.errorWidth = 200;
    }

    public void displayNormalError(String errorMessage, int errorType, int length){
        JFrame normalError = new JFrame();
        normalError.setTitle("Non Vital Error Occured");
        normalError.setSize(errorWidth, errorHeight);
    }

    public void displayCriticalError(String errorMessage, int errorType, int length){
        JFrame CriticalError = new JFrame();
        CriticalError.setTitle("Non Vital Error Occured");
        CriticalError.setSize(errorWidth, errorHeight);
    }

    public void setErrorLength(int newLength){
        this.length = newLength;
    }
}
