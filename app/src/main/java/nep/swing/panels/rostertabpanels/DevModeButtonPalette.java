package nep.swing.panels.rostertabpanels;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * The DevModeButtonPalette class is a Swing panel that provides a user interface with buttons for
 * logging, bash commands, and accessing documentation. It is designed for development mode purposes.
 */
public class DevModeButtonPalette{
    private JPanel devModePanel;
    
    /**
     * Constructs the DevModeButtonPalette, initializes the UI components, and sets up the buttons for logging,
     * bash commands, and documentation access.
     */
    public DevModeButtonPalette(){
        devModePanel = new JPanel();
        devModePanel.setLayout(null);
        devModePanel.setBackground(Color.WHITE);
        devModePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        devModePanel.setPreferredSize(new Dimension(100, 60));
        
        JPanel buttonContainer = new JPanel(new GridLayout(1, 3, 10, 0));
        buttonContainer.setBackground(Color.WHITE);
        buttonContainer.setBounds(10, 10, 225, 35);
        
        JButton debugButton = new JButton("RootNEP");
        JButton bashButton = new JButton("NEPTer");
        JButton helpButton = new JButton("Docs");
        
        buttonContainer.add(debugButton);
        buttonContainer.add(bashButton);
        buttonContainer.add(helpButton);
        
        devModePanel.add(buttonContainer);
    }
    
    /**
     * Returns the main panel containing the development mode buttons.
     *
     * @return the JPanel containing the development mode buttons.
     */
    public JPanel getDevModePanel(){
        return devModePanel;
    }
}
