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
        // Create the main frame
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setUndecorated(false);  // Use default border and title bar
        frame.setResizable(false);  // Prevent resizing the frame
        
        // Set the title to include error code and severity
        String title = "Error Code: " + errorCode + " - " +
                (level == ErrorLevel.CRITICAL ? "Critical Error" : "Warning");
        frame.setTitle(title);
        
        // Set frame icon (optional, this is optional based on your preference)
        ImageIcon logo = new ImageIcon("Media/logo.png");
        frame.setIconImage(logo.getImage());
        
        // Set the content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createLineBorder(
                level == ErrorLevel.CRITICAL ? Color.RED : new Color(180, 180, 180), 2
        ));
        contentPanel.setBackground(level == ErrorLevel.CRITICAL ? new Color(255, 235, 235) : new Color(245, 245, 245));
        
        // Main Content area with error message
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
        messagePanel.setBackground(contentPanel.getBackground());
        
        // Display error message with error code and description
        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<b>" + message + "</b><br><i>Error Code: " + errorCode + "</i></div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));  // Increased font size
        messageLabel.setForeground(Color.DARK_GRAY);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        
        // Add the content panel to the frame
        contentPanel.add(messagePanel, BorderLayout.CENTER);
        
        // Set frame size and location
        frame.setContentPane(contentPanel);
        
        // Set the frame's width slightly wider than the label text, and adjust height
        frame.setSize(new Dimension(messageLabel.getPreferredSize().width + 60, 100));  // Reduced height
        
        frame.setLocationRelativeTo(null);  // Center the frame on screen
        frame.setVisible(true);
    }
}
