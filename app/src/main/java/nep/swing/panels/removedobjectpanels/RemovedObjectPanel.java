package nep.swing.panels.removedobjectpanels;

import nep.util.DisplayUIError;
import nep.util.ObjectManager;
import nep.util.RemovedObjectValidator;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;

/**
 * The RemovedObjectPanel class is a Swing panel that displays and allows the editing of removed object entries.
 * It loads the data from a .txt file containing the removed objects, displays them as clickable buttons,
 * and enables users to modify or restore the entries.
 */
public class RemovedObjectPanel extends JPanel{
    private File dataFile;
    private JPanel contentPanel;
    
    /**
     * Constructs the RemovedObjectPanel, initializes the UI components, and loads the data file containing the removed objects.
     */
    public RemovedObjectPanel(){
        setLayout(null);
        setBackground(new Color(238, 238, 238));
        
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(null);
        containerPanel.setBackground(Color.WHITE);
        containerPanel.setBounds(10, 10, 600, 400);
        containerPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBounds(5, 5, 590, 390);
        
        TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Removed Objects");
        border.setTitleFont(new Font("Arial", Font.BOLD, 16));
        contentPanel.setBorder(border);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBounds(5, 5, 590, 390);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        containerPanel.add(scrollPane);
        add(containerPanel);
        
        loadDataFile();
    }
    
    /**
     * Loads the data from the file containing removed object entries. It checks for the existence of the file and
     * populates the panel with buttons corresponding to each entry.
     * Each button allows editing of the corresponding object.
     */
    private void loadDataFile(){
        File folder = new File("NormalizedEntityParser/RemovedObjects/");
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        
        if (files == null || files.length == 0){
            new DisplayUIError("No .txt file found in the folder!", 101).displayNormalError();
            return;
        }
        else if (files.length > 1){
            new DisplayUIError("Multiple files found. Using: " + files[0].getName(), 102).displayNormalError();
            dataFile = files[0];
        }
        else{
            dataFile = files[0];
        }
        
        if (dataFile != null){
            try{
                List<String> lines = Files.readAllLines(dataFile.toPath());
                int yOffset = 30;
                
                for (int i = 0, count = 1; i < lines.size(); i++) {
                    String line = lines.get(i).trim();
                    
                    if (!line.isEmpty()) {
                        int lineIndex = i;
                        String numberedLine = count + ". " + line;
                        
                        JPanel entryPanel = new JPanel(null);
                        entryPanel.setBackground(new Color(238, 238, 238));
                        entryPanel.setBounds(10, yOffset, 570, 30);
                        
                        JButton editButton = new JButton(numberedLine);
                        editButton.setBounds(0, 0, 570, 30);
                        editButton.setHorizontalAlignment(SwingConstants.LEFT);
                        editButton.addActionListener(e -> openEditDialog(lineIndex, line));
                        
                        entryPanel.add(editButton);
                        contentPanel.add(entryPanel);
                        
                        yOffset += 40;
                        count++;
                    }
                }
            }
            catch (IOException ex){
                new DisplayUIError("Error reading file: " + ex.getMessage(), 103).displayCriticalError();
            }
        }
        
        revalidate();
        repaint();
    }
    
    /**
     * Opens a dialog that allows the user to edit the details of a specific removed object entry.
     * The modified entry is validated and restored in the original file.
     *
     * @param lineIndex the index of the line to be edited in the data file.
     * @param rawData the raw data of the entry to be edited.
     */
    private void openEditDialog(int lineIndex, String rawData){
        ObjectManager manager = new ObjectManager(rawData);
        
        JTextField dalIdField = new JTextField(getValue(rawData, "DalID"));
        JTextField nameField = new JTextField(getValue(rawData, "Name"));
        JTextField codeField = new JTextField(getValue(rawData, "Code"));
        JTextField locationField = new JTextField(getValue(rawData, "Location"));
        JTextField timeField = new JTextField(getValue(rawData, "Time"));
        
        JPanel panel = new JPanel(new GridLayout(0, 2, 7, 10));
        panel.add(new JLabel("DalID (e.g. B00947033):")); panel.add(dalIdField);
        panel.add(new JLabel("Name (e.g. Doe, John):")); panel.add(nameField);
        panel.add(new JLabel("Code (e.g. CSCI 2110 01):")); panel.add(codeField);
        panel.add(new JLabel("Location (e.g. 3080 ROWE):")); panel.add(locationField);
        panel.add(new JLabel("Time (e.g. 6:00 PM):")); panel.add(timeField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Entry", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION){
            manager.modifyObjectKeyValue("DalID", dalIdField.getText());
            manager.modifyObjectKeyValue("Name", nameField.getText());
            manager.modifyObjectKeyValue("Code", codeField.getText());
            manager.modifyObjectKeyValue("Location", locationField.getText());
            manager.modifyObjectKeyValue("Time", timeField.getText());
            
            String updatedLine = manager.getObject();
            boolean restored = RemovedObjectValidator.validateAndRestore(updatedLine, dataFile.toPath(), lineIndex);
            
            if (restored){
                refreshPanel();
            }
        }
    }
    
    /**
     * Extracts the value associated with a given key from the raw data of a removed object.
     *
     * @param data the raw data string representing the removed object.
     * @param key the key whose associated value is to be extracted.
     * @return the value associated with the key, or an empty string if not found.
     */
    private String getValue(String data, String key){
        String pattern = key + ": ";
        int start = data.indexOf(pattern);
        if (start == -1) return "";
        int valueStart = start + pattern.length();
        int valueEnd = data.indexOf(" |", valueStart);
        if (valueEnd == -1) valueEnd = data.indexOf("]", valueStart);
        if (valueEnd == -1) return "";
        return data.substring(valueStart, valueEnd).trim();
    }
    
    /**
     * Updates a specific line in the data file with the new content provided.
     *
     * @param lineIndex the index of the line to be updated.
     * @param newContent the new content to write to the file.
     */
    private void updateLineInFile(int lineIndex, String newContent){
        try{
            List<String> lines = Files.readAllLines(dataFile.toPath());
            lines.set(lineIndex, newContent);
            Files.write(dataFile.toPath(), lines);
        }
        catch (IOException e) {
            new DisplayUIError("Failed to update file: " + e.getMessage(), 104).displayCriticalError();
        }
    }
    
    /**
     * Refreshes the panel by removing all current entries and reloading the data from the file.
     */
    private void refreshPanel(){
        contentPanel.removeAll();
        loadDataFile();
        revalidate();
        repaint();
    }
}
