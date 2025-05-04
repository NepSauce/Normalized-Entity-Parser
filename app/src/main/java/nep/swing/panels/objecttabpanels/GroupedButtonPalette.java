package nep.swing.panels.objecttabpanels;

import nep.rosterconversion.PDFCleaner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.print.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;

public class GroupedButtonPalette {
    private final JPanel panel;
    
    public GroupedButtonPalette() {
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createBevelBorder(1));
        panel.setPreferredSize(new Dimension(100, 60));
        
        JPanel buttonContainer = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonContainer.setBounds(10, 10, 225, 35);
        buttonContainer.setBackground(Color.WHITE);
        
        JButton generate = new JButton("Create");
        JButton open = new JButton("Open");
        
        generate.addActionListener((ActionEvent e) -> {
            PDFCleaner.generateGroupedAppointments();
        });
        
        open.addActionListener((ActionEvent e) -> {
            openMostRecentGroupedFile();
        });
        
        buttonContainer.add(generate);
        buttonContainer.add(open);
        
        panel.add(buttonContainer);
    }
    
    public JPanel getPanel() {
        return panel;
    }
    
    private void openMostRecentGroupedFile() {
        File mostRecentFile = getMostRecentGroupedFile();
        if (mostRecentFile != null) {
            openFileEditor(mostRecentFile);
        }
    }
    
    private File getMostRecentGroupedFile() {
        File folder = new File("NormalizedEntityParser/GroupedObjects/");
        File[] files = folder.listFiles((dir, name) ->
                name.matches("GroupedObject\\(\\d{4}-\\d{1,2}-\\d{1,2}\\)\\(\\d{1,2}-\\d{1,2}-\\d{1,2}\\)\\.txt"));
                
        if (files == null || files.length == 0) {
            JOptionPane.showMessageDialog(null, "No grouped files found in the folder!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        Arrays.sort(files, (f1, f2) -> {
            try {
                BasicFileAttributes attr1 = Files.readAttributes(f1.toPath(), BasicFileAttributes.class);
                BasicFileAttributes attr2 = Files.readAttributes(f2.toPath(), BasicFileAttributes.class);
                return attr2.creationTime().compareTo(attr1.creationTime());
            } catch (IOException e) {
                return 0;
            }
        });
        
        return files[0];
    }
    
    private void openFileEditor(File dataFile) {
        try {
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
            
            textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    saveButton.setEnabled(true);
                }
                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    saveButton.setEnabled(true);
                }
                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    saveButton.setEnabled(true);
                }
            });
            
            saveButton.addActionListener((ActionEvent saveEvent) -> {
                try {
                    Files.write(dataFile.toPath(), textArea.getText().getBytes());
                    JOptionPane.showMessageDialog(null, "File saved successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    saveButton.setEnabled(false);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Failed to save the file: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            JButton printButton = new JButton("Print");
            printButton.addActionListener((ActionEvent printEvent) -> {
                printTextContent(textArea.getText(), dataFile.getName());
            });
            
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(saveButton);
            buttonPanel.add(printButton);
            editorPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            JFrame editorFrame = new JFrame("Edit File: " + dataFile.getName());
            editorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            editorFrame.add(editorPanel);
            editorFrame.pack();
            editorFrame.setLocationRelativeTo(null);
            editorFrame.setVisible(true);
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void printTextContent(String text, String jobName) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName(jobName);

        Printable printable = (graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }
            
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

            g2d.setFont(new Font("Monospaced", Font.PLAIN, 10));
            g2d.setColor(Color.BLACK);

            String[] lines = text.split("\n");
            int y = 15;
            for (String line : lines) {
                g2d.drawString(line, 10, y);
                y += 15;
                if (y > pageFormat.getImageableHeight() - 15) {
                    break;
                }
            }
            
            return Printable.PAGE_EXISTS;
        };
        
        job.setPrintable(printable);

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(null, "Printing failed: " + e.getMessage(),
                        "Print Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}