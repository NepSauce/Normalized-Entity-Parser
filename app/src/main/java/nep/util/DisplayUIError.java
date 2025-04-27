package nep.util;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("unused")
public class DisplayUIError {
    private final String errorMessage;
    private int length;
    private int errorType;
    
    
    public DisplayUIError(String errorMessage, int errorType){
        this.errorMessage = errorMessage;
        this.errorType = errorType;
        this.length = 5000;
        int errorHeight = 120;
        int errorWidth = 200;
    }
    
    public void displayNormalError(){
        JFrame normalError = new JFrame();
        normalError.setSize(600, 75);
        normalError.setTitle("Error Code: " + errorType + " - Non Vital Error Occurred");
        normalError.getContentPane().setBackground(new Color(238,238,238,255));
        normalError.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon logo = new ImageIcon("Media/logo.png");
        normalError.setIconImage(logo.getImage());
        
        JLabel interiorTextLabel = new JLabel();
        interiorTextLabel.setBounds(10, 10, 10, 0);
        interiorTextLabel.setBackground(Color.WHITE);
        interiorTextLabel.setOpaque(true);
        interiorTextLabel.setText(errorMessage + " (Error Type: " + errorType + ")");
        interiorTextLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        interiorTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        normalError.add(interiorTextLabel);
        normalError.setLocationRelativeTo(null);
        normalError.setVisible(true);
        
        new Timer(length, e -> normalError.dispose()).start();
    }
    
    public void displayCriticalError(){
        JFrame criticalError = new JFrame();
        criticalError.setTitle("Critical Error Occurred");
        criticalError.setSize(475, 75);
        criticalError.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JLabel interiorTextLabel = new JLabel();
        interiorTextLabel.setBackground(Color.RED);
        interiorTextLabel.setOpaque(true);
        interiorTextLabel.setBounds(10, 10, 580, 80);
        interiorTextLabel.setText(errorMessage);
        interiorTextLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        interiorTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        criticalError.add(interiorTextLabel);
        criticalError.setLocationRelativeTo(null);
        criticalError.setVisible(true);
        
        new Timer(length, e -> criticalError.dispose()).start();
    }
    
    public void setErrorLength(int newLength){
        this.length = newLength;
    }
}
