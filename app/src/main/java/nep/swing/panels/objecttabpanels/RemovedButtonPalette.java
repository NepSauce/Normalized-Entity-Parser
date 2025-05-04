package nep.swing.panels.objecttabpanels;

import nep.rosterconversion.PDFConversion;
import nep.util.DisplayUIError;
import nep.util.DisplayUIPopup;
import nep.util.FileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class RemovedButtonPalette {
    private JPanel panel;
    
    public RemovedButtonPalette() {
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createBevelBorder(1));
        panel.setPreferredSize(new Dimension(100, 60));
        
        JPanel buttonContainer = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonContainer.setBounds(10, 10, 225, 35);
        buttonContainer.setBackground(Color.WHITE);
        
        JButton modify = new JButton("Edit Entry");
        JButton delete = new JButton("Delete");
        
        modify.addActionListener((ActionEvent e) -> {
            String folderPath = "NormalizedEntityParser/RemovedObjects/";
            try {
                File folder = new File(folderPath);
                File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
                
                if (files == null || files.length == 0) {
                    new DisplayUIPopup("Missing File", "No RemovedObject.txt Was Found.", 1003).showInfoPopup();
                    return;
                }
                
                File targetFile = files[0]; // Assuming only one file
                int lineCount = FileManager.countLinesInFile(targetFile.getAbsolutePath());
                
                if (lineCount == 0) {
                    new DisplayUIPopup("Empty File", "RemovedObject.txt is Empty.", 1004).showInfoPopup();
                    return;
                }
                
                // If file exists and has content, launch panel
                SwingUtilities.invokeLater(() -> {
                    JFrame frame = new JFrame("Removed Objects Viewer");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setSize(650, 475);
                    frame.setLocationRelativeTo(null);
                    frame.setContentPane(new nep.swing.panels.removedobjectpanels.RemovedObjectPanel());
                    frame.setVisible(true);
                });
                
            } catch (IOException ex) {
                new DisplayUIError("Error Accessing File: " + ex.getMessage(), 601).displayCriticalError();
            }
        });
        
        delete.addActionListener((ActionEvent e) -> {
            int result = JOptionPane.showConfirmDialog(null, "Are You Sure You Wish To Delete RemovedObject.txt?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                PDFConversion.deleteRemovedObjectFile();
            }
        });
        
        buttonContainer.add(modify);
        buttonContainer.add(delete);
        panel.add(buttonContainer);
    }
    
    public JPanel getPanel() {
        return panel;
    }
}