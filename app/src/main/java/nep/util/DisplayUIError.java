package nep.util;

import javax.swing.*;

public class DisplayUIError{
    private final String message;
    private final int errorCode;
    private ErrorLevel level = ErrorLevel.NORMAL;
    
    public enum ErrorLevel{
        NORMAL, CRITICAL
    }
    
    public DisplayUIError(String message, int errorCode){
        this.message = message;
        this.errorCode = errorCode;
    }
    
    public void displayNormalError(){
        this.level = ErrorLevel.NORMAL;
        showDialog();
    }
    
    public void displayCriticalError(){
        this.level = ErrorLevel.CRITICAL;
        showDialog();
    }
    
    private void showDialog(){
        String fullMessage = "<html><b>" + message + "</b><br><i>Code: " + errorCode + "</i></html>";
        String title = (level == ErrorLevel.CRITICAL ? "Critical Error" : "Warning");
        
        int messageType = (level == ErrorLevel.CRITICAL)
                ? JOptionPane.ERROR_MESSAGE
                : JOptionPane.WARNING_MESSAGE;
        

        JOptionPane.showMessageDialog(
                null,
                fullMessage,
                title,
                messageType
        );
    }
}
