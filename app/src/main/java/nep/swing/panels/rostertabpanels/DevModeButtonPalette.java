package nep.swing.panels.rostertabpanels;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class DevModeButtonPalette {
    
    private JPanel devModePanel;
    
    public DevModeButtonPalette() {
        devModePanel = new JPanel();
        devModePanel.setLayout(null);
        devModePanel.setBackground(Color.WHITE);
        devModePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        devModePanel.setPreferredSize(new Dimension(100, 60));
        
        JPanel buttonContainer = new JPanel(new GridLayout(1, 3, 10, 0));
        buttonContainer.setBackground(Color.WHITE);
        buttonContainer.setBounds(10, 10, 225, 35);
        
        JButton logButton = new JButton("NLog");
        JButton bashButton = new JButton("SNep");
        JButton helpButton = new JButton("Docs");
        
        buttonContainer.add(logButton);
        buttonContainer.add(bashButton);
        buttonContainer.add(helpButton);
        
        devModePanel.add(buttonContainer);
    }
    
    public JPanel getDevModePanel() {
        return devModePanel;
    }
}
