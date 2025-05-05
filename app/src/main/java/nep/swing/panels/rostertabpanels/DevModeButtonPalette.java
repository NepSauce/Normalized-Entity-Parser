package nep.swing.panels.rostertabpanels;

import nep.swing.panels.devmodepanels.TerminalFrame;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        
        debugButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDebugFrame();
            }
        });
        
        bashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openTerminal();
            }
        });
        
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
    
    /**
     * Opens the TerminalFrame when the NEPTer button is clicked.
     */
    private void openTerminal() {
        new TerminalFrame();  // Open the terminal window
    }
    
    /**
     * Opens a new frame when the "RootNEP" button is pressed.
     */
    private void openDebugFrame() {
        nep.swing.panels.rostertabpanels.DebugFrame debugFrame = new nep.swing.panels.rostertabpanels.DebugFrame();
        debugFrame.setVisible(true);
    }
}
