package nep.swing.panels.objecttabpanels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.*;
import java.util.List;

public class CombinedButtonPalette {
    private JPanel panel;
    
    public CombinedButtonPalette() {
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createBevelBorder(1));
        panel.setPreferredSize(new Dimension(100, 60));
        
        JPanel buttonContainer = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonContainer.setBounds(10, 10, 225, 35);
        buttonContainer.setBackground(Color.WHITE);
        
        JButton modify = new JButton("Open File");
        JButton delete = new JButton("Delete");
        
        modify.addActionListener((ActionEvent e) -> {
            openFileEditor();
        });
        
        delete.addActionListener((ActionEvent e) -> {
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete from Combined?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                System.out.println("Deleted from Combined");
            }
        });
        
        buttonContainer.add(modify);
        buttonContainer.add(delete);
        panel.add(buttonContainer);
    }
    
    public JPanel getPanel() {
        return panel;
    }
    
    private void openFileEditor() {
        File folder = new File("NormalizedEntityParser/CombinedObjects/");
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        
        if (files == null || files.length == 0) {
            JOptionPane.showMessageDialog(null, "No .txt file found in the folder!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        File dataFile = files[0];
        
        try {
            List<String> lines = Files.readAllLines(dataFile.toPath());
            JTextArea textArea = new JTextArea();
            textArea.setEditable(true);
            textArea.setText(String.join("\n", lines));
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.setMargin(new Insets(0, 10, 0, 10)); // Padding inside the text area
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            
            JPanel editorPanel = new JPanel();
            editorPanel.setLayout(new BorderLayout());
            editorPanel.add(scrollPane, BorderLayout.CENTER);
            
            // Save button only appears if text is modified
            JButton saveButton = new JButton("Save");
            saveButton.setEnabled(false); // Initially disabled
            
            textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    saveButton.setEnabled(true); // Enable save button when text is modified
                }
                
                @Override
                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    saveButton.setEnabled(true); // Enable save button when text is modified
                }
                
                @Override
                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    saveButton.setEnabled(true); // Enable save button when text is modified
                }
            });
            
            saveButton.addActionListener((ActionEvent saveEvent) -> {
                try {
                    Files.write(dataFile.toPath(), textArea.getText().getBytes());
                    JOptionPane.showMessageDialog(null, "File saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    saveButton.setEnabled(false); // Disable save after saving
                } catch (IOException ex) {
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
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
