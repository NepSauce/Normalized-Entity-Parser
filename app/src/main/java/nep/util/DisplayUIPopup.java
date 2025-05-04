package nep.util;

import javax.swing.*;
import java.awt.*;

public class DisplayUIPopup {
    private final String message;
    private final String title;
    private PopupType type = PopupType.INFO;
    
    public enum PopupType {
        INFO, CONFIRM
    }
    
    public DisplayUIPopup(String title, String message) {
        this.title = title;
        this.message = message;
    }
    
    public void showInfoPopup() {
        this.type = PopupType.INFO;
        showDialog();
    }
    
    public boolean showConfirmationPopup() {
        this.type = PopupType.CONFIRM;
        return showDialog();
    }
    
    private boolean showDialog() {
        ImageIcon logo = new ImageIcon("Media/logo.png");
        
        JLabel label = new JLabel("<html><div style='text-align: center;'>" + message + "</div></html>");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.PLAIN, 13));
        
        if (type == PopupType.INFO) {
            JOptionPane.showMessageDialog(null, label, title, JOptionPane.INFORMATION_MESSAGE, logo);
            return false;
        }
        else {
            int result = JOptionPane.showConfirmDialog(null, label, title,
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, logo);
            return result == JOptionPane.YES_OPTION;
        }
    }
}
