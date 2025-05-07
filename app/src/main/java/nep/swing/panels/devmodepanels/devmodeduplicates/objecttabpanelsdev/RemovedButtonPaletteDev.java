package nep.swing.panels.devmodepanels.devmodeduplicates.objecttabpanelsdev;

import nep.rosterconversion.PDFConversion;
import nep.util.DisplayUIError;
import nep.util.DisplayUIPopup;
import nep.util.FileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

/**
 * The RemovedButtonPalette class provides a Swing panel with buttons
 * for editing or deleting the `RemovedObject.txt` file, which contains
 * records that have been removed during PDF parsing.
 * <p>
 * It includes logic to validate file presence and contents before allowing edits,
 * and confirms deletion to prevent accidental file loss.
 */
public class RemovedButtonPaletteDev {
    private JPanel removedButtonPanel;
    
    /**
     * Constructs the RemovedButtonPalette panel and initializes the "Edit Entry"
     * and "Delete" buttons with their respective functionalities.
     */
    public RemovedButtonPaletteDev(){
        removedButtonPanel = new JPanel();
        removedButtonPanel.setLayout(null);
        removedButtonPanel.setBackground(Color.WHITE);
        removedButtonPanel.setBorder(BorderFactory.createBevelBorder(1));
        removedButtonPanel.setPreferredSize(new Dimension(100, 60));
        
        JPanel buttonContainer = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonContainer.setBounds(10, 10, 225, 35);
        buttonContainer.setBackground(Color.WHITE);
        
        JButton modify = new JButton("Edit Entry");
        JButton delete = new JButton("Delete");
        
        modify.addActionListener((ActionEvent e) -> {
            String folderPath = "NormalizedEntityParser/RemovedObjects/";
            
            try{
                File folder = new File(folderPath);
                File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
                
                if (files == null || files.length == 0){
                    new DisplayUIPopup("Missing File", "No RemovedObject.txt Was Found.", 1003).showInfoPopup();
                    return;
                }
                
                File targetFile = files[0];
                int lineCount = FileManager.countLinesInFile(targetFile.getAbsolutePath());
                
                if (lineCount == 0){
                    new DisplayUIPopup("Empty File", "RemovedObject.txt is Empty.", 1004).showInfoPopup();
                    return;
                }

                SwingUtilities.invokeLater(() -> {
                    JFrame frame = new JFrame("Removed Objects Viewer");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setSize(640, 475);
                    frame.setLocationRelativeTo(null);
                    frame.setContentPane(new nep.swing.panels.removedobjectpanels.RemovedObjectPanel());
                    frame.setVisible(true);
                });
                
            }
            catch (IOException ex){
                new DisplayUIError("Error Accessing File: " + ex.getMessage(), 601).displayCriticalError();
            }
        });
        
        delete.addActionListener((ActionEvent e) -> {
            int result = JOptionPane.showConfirmDialog(null, "Are You Sure You Wish To Delete RemovedObject.txt?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION){
                PDFConversion.deleteRemovedObjectFile();
            }
        });
        
        buttonContainer.add(modify);
        buttonContainer.add(delete);
        removedButtonPanel.add(buttonContainer);
    }
    
    /**
     * Returns the JPanel containing the "Edit Entry" and "Delete" buttons.
     *
     * @return the removed button panel UI component.
     */
    public JPanel getRemovedButtonPanel(){
        return removedButtonPanel;
    }
}