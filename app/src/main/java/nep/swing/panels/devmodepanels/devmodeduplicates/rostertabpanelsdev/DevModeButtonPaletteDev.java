package nep.swing.panels.devmodepanels.devmodeduplicates.rostertabpanelsdev;

import nep.swing.panels.devmodepanels.TerminalFrame;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import nep.swing.NepFrame;

/**
 * The DevModeButtonPalette class is a Swing panel that provides a user interface with buttons for
 * logging, bash commands, and accessing documentation. It is designed for development mode purposes.
 */
public class DevModeButtonPaletteDev{
    private JPanel devModePanel;
    
    /**
     * Constructs the DevModeButtonPalette, initializes the UI components, and sets up the buttons for logging,
     * bash commands, and documentation access.
     */
    public DevModeButtonPaletteDev(){
        devModePanel = new JPanel();
        devModePanel.setLayout(null);
        devModePanel.setBackground(Color.WHITE);
        devModePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        devModePanel.setPreferredSize(new Dimension(100, 60));
        
        JPanel buttonContainer = new JPanel(new GridLayout(1, 3, 10, 0));
        buttonContainer.setBackground(Color.WHITE);
        buttonContainer.setBounds(10, 10, 225, 35);
        
        JButton debugButton = new JButton("SUI");
        JButton bashButton = new JButton("Node");
        JButton helpButton = new JButton("Docs");
        
        debugButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                openNepFrame();
            }
        });
        
        bashButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                openTerminal();
            }
        });
        
        helpButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                openGuidePdf();
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
     * Opens the guide.pdf file using the system's default PDF viewer.
     */
    private void openGuidePdf(){
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("guide.pdf");
        
        if (inputStream == null){
            JOptionPane.showMessageDialog(devModePanel,
                    "The embedded PDF guide was not found in the application resources.",
                    "File Not Found",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            File tempFile = File.createTempFile("guide", ".pdf");
            tempFile.deleteOnExit();
            
            OutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            
            while ((bytesRead = inputStream.read(buffer)) != -1){
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();
            
            if (Desktop.isDesktopSupported()){
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.OPEN)){
                    desktop.open(tempFile);
                }
                else{
                    JOptionPane.showMessageDialog(devModePanel,
                            "Opening files is not supported on this system.",
                            "Unsupported Operation",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            else{
                JOptionPane.showMessageDialog(devModePanel,
                        "Desktop is not supported on this system.",
                        "Unsupported Platform",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (Exception ex){
            JOptionPane.showMessageDialog(devModePanel,
                    "An error occurred while opening the PDF:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    /**
     * Opens the TerminalFrame when the NEPTer button is clicked.
     */
    private void openTerminal(){
        new TerminalFrame();  // Open the terminal window
    }
    
    /**
     * Opens a new frame when the "RootNEP" button is pressed.
     */
    private void openNepFrame(){
        NepFrame nepFrame = new NepFrame();
        nepFrame.setVisible(true);
    }
}
