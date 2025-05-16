package nep.util;

import javax.swing.*;
import java.awt.*;

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
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setUndecorated(false);
        frame.setResizable(false);
        frame.setTitle(title);
        
        ImageIcon logo = new ImageIcon("Media/logo.png");
        frame.setIconImage(logo.getImage());
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createLineBorder(
                type == PopupType.CONFIRM ? new Color(70, 130, 180) : new Color(150, 150, 150), 2
        ));
        contentPanel.setBackground(type == PopupType.CONFIRM ? new Color(235, 245, 255) : new Color(245, 245, 245));
        
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
        messagePanel.setBackground(contentPanel.getBackground());
        
        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<b>" + message + "</b><br><i>Popup Code: " + popupCode + "</i></div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        messageLabel.setForeground(Color.DARK_GRAY);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        contentPanel.add(messagePanel, BorderLayout.CENTER);
        
        boolean[] confirmed = {false};
        
        if (type == PopupType.CONFIRM){
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton yesButton = new JButton("Yes");
            JButton noButton = new JButton("No");
            
            yesButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            noButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            
            yesButton.addActionListener(e -> {
                confirmed[0] = true;
                frame.dispose();
            });
            
            noButton.addActionListener(e -> {
                confirmed[0] = false;
                frame.dispose();
            });
            
            buttonPanel.add(yesButton);
            buttonPanel.add(noButton);
            contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        }
        
        frame.setContentPane(contentPanel);
        frame.setSize(new Dimension(messageLabel.getPreferredSize().width + 60, 100));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        if (type == PopupType.CONFIRM){
            while (frame.isVisible()){
                try {
                    Thread.sleep(50);
                }
                catch (InterruptedException ignored){
                
                }
            }
        }
        
        return confirmed[0];
    }
}
