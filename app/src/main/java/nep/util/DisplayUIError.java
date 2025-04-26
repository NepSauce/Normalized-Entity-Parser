package nep.util;

import javax.swing.*;
import java.awt.*;

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
        this.length = 5000;
        this.errorHeight = 120;
        this.errorWidth = 200;
    }
    
    public void displayNormalError(){
        JFrame normalError = new JFrame();
        normalError.setSize(500, 75);
        normalError.setTitle("Non Vital Error Occurred");
        normalError.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon logo = new ImageIcon("Media/logo.png");
        normalError.setIconImage(logo.getImage());
        
        JLabel interiorTextLabel = new JLabel();
        interiorTextLabel.setBackground(Color.WHITE);
        interiorTextLabel.setOpaque(true);
        interiorTextLabel.setBounds(10, 10, 580, 80);
        interiorTextLabel.setText(errorMessage);
        interiorTextLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        interiorTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        normalError.add(interiorTextLabel);
        normalError.setLocationRelativeTo(null);
        normalError.setVisible(true);
        
        new Timer(length, e -> normalError.dispose()).start();
    }
    
    public void displayCriticalError(String errorMessage, int errorType, int length){
        JFrame criticalError = new JFrame();
        criticalError.setTitle("Critical Error Occurred");
        criticalError.setSize(600, 100);
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
