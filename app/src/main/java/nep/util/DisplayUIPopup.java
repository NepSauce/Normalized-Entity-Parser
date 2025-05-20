package nep.util;

import javax.swing.*;

public class DisplayUIPopup{
    private final String message;
    private final String title;
    private final int popupCode;
    private PopupType type = PopupType.INFO;
    
    public enum PopupType{
        INFO, CONFIRM
    }
    
    public DisplayUIPopup(String title, String message, int popupCode){
        this.title = title;
        this.message = message;
        this.popupCode = popupCode;
    }
    
    public void showInfoPopup(){
        this.type = PopupType.INFO;
        showDialog();
    }
    
    public boolean showConfirmationPopup(){
        this.type = PopupType.CONFIRM;
        return showDialog();
    }
    
    private boolean showDialog(){
        String fullMessage = "<html><b>" + message + "</b><br><i>Code: " + popupCode + "</i></html>";
        
        if (type == PopupType.INFO){
            JOptionPane.showMessageDialog(
                    null,
                    fullMessage,
                    title,
                    JOptionPane.INFORMATION_MESSAGE
            );
            return true;
        }
        else{
            int result = JOptionPane.showConfirmDialog(
                    null,
                    fullMessage,
                    title,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            return result == JOptionPane.YES_OPTION;
        }
    }
}
