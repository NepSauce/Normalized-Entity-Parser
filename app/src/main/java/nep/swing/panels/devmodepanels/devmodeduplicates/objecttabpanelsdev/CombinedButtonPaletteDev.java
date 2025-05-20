package nep.swing.panels.devmodepanels.devmodeduplicates.objecttabpanelsdev;

import nep.swing.panels.devmodepanels.devmodeduplicates.LoggingPanelDev;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * This class provides a button palette with actions to open and delete files
 * within the CombinedObjects directory. It is part of the Object tab UI panel
 * in the Normalized Entity Parser Swing application.
 */
public class CombinedButtonPaletteDev{
    private static final Log log = LogFactory.getLog(CombinedButtonPaletteDev.class);
    private JPanel combinedButtonPanel;
    
    /**
     * Constructs the CombinedButtonPalette and initializes the buttons
     * for opening and deleting files in the CombinedObjects directory.
     */
    public CombinedButtonPaletteDev(){
        combinedButtonPanel = new JPanel();
        combinedButtonPanel.setLayout(null);
        combinedButtonPanel.setBackground(Color.WHITE);
        combinedButtonPanel.setBorder(BorderFactory.createBevelBorder(1));
        combinedButtonPanel.setPreferredSize(new Dimension(100, 60));
        
        JPanel buttonContainer = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonContainer.setBounds(10, 10, 225, 35);
        buttonContainer.setBackground(Color.WHITE);
        
        JButton openFile = new JButton("Open File");
        JButton deleteFile = new JButton("Delete");
        
        openFile.addActionListener((ActionEvent e) -> {
            openFileEditor();
        });
        
        deleteFile.addActionListener((ActionEvent e) -> {
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete from Combined?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION){
                System.out.println("Deleted from Combined");
            }
        });
        
        buttonContainer.add(openFile);
        buttonContainer.add(deleteFile);
        combinedButtonPanel.add(buttonContainer);
    }
    
    /**
     * Returns the JPanel containing the button palette.
     *
     * @return The JPanel with Open File and Delete buttons.
     */
    public JPanel getCombinedButtonPanel(){
        return combinedButtonPanel;
    }
    
    /**
     * Opens the first .txt file found in the CombinedObjects directory
     * in an editable text area within a new JFrame window.
     * Allows users to view and save modifications to the file.
     * If no .txt file is found, an error dialog is displayed.
     */
    private void openFileEditor(){
        File folder = new File("NormalizedEntityParser/CombinedObjects/");
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        
        if (files == null || files.length == 0){
            LoggingPanelDev loggingPanel = new LoggingPanelDev();
            loggingPanel.log("No .txt file found in the folder.");
            JOptionPane.showMessageDialog(null, "No .txt file found in the folder!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        File dataFile = files[0];
        
        try{
            List<String> lines = Files.readAllLines(dataFile.toPath());
            JTextArea textArea = new JTextArea();
            textArea.setEditable(true);
            textArea.setText(String.join("\n", lines));
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.setMargin(new Insets(0, 10, 0, 10));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            
            JPanel editorPanel = new JPanel();
            editorPanel.setLayout(new BorderLayout());
            editorPanel.add(scrollPane, BorderLayout.CENTER);

            JButton saveButton = new JButton("Save");
            saveButton.setEnabled(false);
            
            textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(javax.swing.event.DocumentEvent e){
                    saveButton.setEnabled(true);
                }
                
                @Override
                public void removeUpdate(javax.swing.event.DocumentEvent e){
                    saveButton.setEnabled(true);
                }
                
                @Override
                public void changedUpdate(javax.swing.event.DocumentEvent e){
                    saveButton.setEnabled(true);
                }
            });
            
            saveButton.addActionListener((ActionEvent saveEvent) -> {
                try{
                    Files.write(dataFile.toPath(), textArea.getText().getBytes());
                    JOptionPane.showMessageDialog(null, "File saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    saveButton.setEnabled(false);
                }
                catch (IOException ex){
                    JOptionPane.showMessageDialog(null, "Failed to save the file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(saveButton);
            editorPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            JFrame editorFrame = new JFrame("Edit File: " + dataFile.getName());
            editorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            editorFrame.add(editorPanel);
            editorFrame.pack();
            editorFrame.setLocationRelativeTo(null);
            editorFrame.setVisible(true);
            
        }
        catch (IOException e){
            JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
