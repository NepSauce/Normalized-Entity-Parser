package nep.util;

import javax.swing.*;
import java.awt.*;

public class DisplayUIError {
    private final String message;
    private final int errorCode;
    private ErrorLevel level = ErrorLevel.NORMAL;
    
    public enum ErrorLevel {
        NORMAL, CRITICAL
    }
    
    public DisplayUIError(String message, int errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }
    
    public void displayNormalError() {
        this.level = ErrorLevel.NORMAL;
        showDialog();
    }
    
    public void displayCriticalError() {
        this.level = ErrorLevel.CRITICAL;
        showDialog();
    }
    
    private void showDialog() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setUndecorated(false);
        frame.setResizable(false);

        String title = "Error Code: " + errorCode + " - " +
                (level == ErrorLevel.CRITICAL ? "Critical Error" : "Warning");
        frame.setTitle(title);

        ImageIcon logo = new ImageIcon("Media/logo.png");
        frame.setIconImage(logo.getImage());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createLineBorder(
                level == ErrorLevel.CRITICAL ? Color.RED : new Color(180, 180, 180), 2
        ));
        contentPanel.setBackground(level == ErrorLevel.CRITICAL ? new Color(255, 235, 235) : new Color(245, 245, 245));

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
        messagePanel.setBackground(contentPanel.getBackground());

        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<b>" + message + "</b><br><i>Error Code: " + errorCode + "</i></div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        messageLabel.setForeground(Color.DARK_GRAY);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        
        contentPanel.add(messagePanel, BorderLayout.CENTER);

        frame.setContentPane(contentPanel);

        frame.setSize(new Dimension(messageLabel.getPreferredSize().width + 60, 100));
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
