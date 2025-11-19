package nep.swing.panels.devmodepanels.devmodeduplicates.objecttabpanelsdev;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import nep.rosterconversion.PDFCleaner;
import nep.swing.panels.devmodepanels.devmodeduplicates.CumulativeInfoPanelDev;
import nep.swing.panels.devmodepanels.devmodeduplicates.ExamAddedPanelDev;
import nep.swing.panels.devmodepanels.devmodeduplicates.ExamLocationPanelDev;
import nep.swing.panels.devmodepanels.devmodeduplicates.LoggingPanelDev;
import nep.swing.panels.devmodepanels.devmodeduplicates.RosterAddedPanelDev;
import nep.util.GroupedObjectParser;

/**
 * The GroupedButtonPalette class provides a UI panel with buttons
 * to generate grouped appointment files and open the most recently
 * generated grouped file. It includes file editing and printing capabilities.
 */
public class GroupedButtonPaletteDev{
    private final JPanel groupedButtonPanel;
    private CumulativeInfoPanelDev cumulativeInfoPanel;
    private LoggingPanelDev loggingPanel;
    
    /**
     * Constructs a GroupedButtonPalette and initializes buttons for
     * creating and opening grouped appointment files.
     */
    public GroupedButtonPaletteDev(ExamLocationPanelDev examLocationPanel, ExamAddedPanelDev examAddedPanel,
                                   RosterAddedPanelDev rosterAddedPanel, CumulativeInfoPanelDev cumulativeInfoPanel, LoggingPanelDev loggingPanel){
        this.cumulativeInfoPanel = cumulativeInfoPanel;
        this.loggingPanel = loggingPanel;
        
        groupedButtonPanel = new JPanel();
        groupedButtonPanel.setLayout(null);
        groupedButtonPanel.setBackground(Color.WHITE);
        groupedButtonPanel.setBorder(BorderFactory.createBevelBorder(1));
        groupedButtonPanel.setPreferredSize(new Dimension(100, 60));
        
        JPanel buttonContainer = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonContainer.setBounds(10, 10, 225, 35);
        buttonContainer.setBackground(Color.WHITE);
        
        JButton generate = new JButton("Create");
        JButton open = new JButton("Open");
        
        generate.addActionListener((ActionEvent e) -> {
            JDialog loadingDialog = new JDialog((Frame) null, "Processing...", true);
            loadingDialog.setSize(300, 100);
            loadingDialog.setLayout(new BorderLayout());
            loadingDialog.setLocationRelativeTo(null);

            JLabel statusLabel = new JLabel("Initiated Grouping", SwingConstants.CENTER);
            statusLabel.setFont(new Font("Arial", Font.BOLD, 14));

            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);

            loadingDialog.add(statusLabel, BorderLayout.CENTER);
            loadingDialog.add(progressBar, BorderLayout.SOUTH);

            // Messages to cycle through
            String[] messages = {
                    "Initiated Grouping",
                    "Initiated Grouping",
                    "Initiated Grouping",
                    "Initiated Grouping"
            };

            Timer messageTimer = new Timer(1500, null); // switch every 1.5 sec
            final int[] index = {0};

            messageTimer.addActionListener(ev -> {
                statusLabel.setText(messages[index[0]]);
                index[0]++;
                if (index[0] >= messages.length) {
                    messageTimer.stop();
                    loadingDialog.dispose();

                    PDFCleaner.generateGroupedAppointments();
                    try {
                        updateCumulativeList();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            messageTimer.setInitialDelay(0);
            messageTimer.start();

            loadingDialog.setVisible(true);
        });

        
        open.addActionListener((ActionEvent e) -> {
            openMostRecentGroupedFile();
        });
        
        buttonContainer.add(generate);
        buttonContainer.add(open);
        
        groupedButtonPanel.add(buttonContainer);
    }
    
    public void updateCumulativeList() throws IOException {
        String directoryPath = "NormalizedEntityParser/GroupedObjects/"; // change this
        File latestFile = GroupedObjectParser.getMostRecentFile(directoryPath);
        
        if (latestFile == null){
            System.out.println("No Grouped Object files found.");
            return;
        }
        
        GroupedObjectParser.ParseResult result = GroupedObjectParser.parseFile(latestFile);

        int totalSum = result.totalSum;
        int courseCount = result.uniqueCourses;

        System.out.println("Total numeric sum: " + totalSum);
        System.out.println("Total unique courses: " + courseCount);
        
       
        
        cumulativeInfoPanel.setGroupedEntries(totalSum);
        cumulativeInfoPanel.setCoursesFound(courseCount);
        
        cumulativeInfoPanel.getCumulativeInfoPanel().revalidate();
        cumulativeInfoPanel.getCumulativeInfoPanel().repaint();
        cumulativeInfoPanel.refresh();
    }
    
    /**
     * Returns the JPanel containing the grouped button palette.
     *
     * @return the panel containing Create and Open buttons.
     */
    public JPanel getGroupedButtonPanel(){
        return groupedButtonPanel;
    }
    
    /**
     * Opens the most recently created grouped appointment file in a text editor
     * window if available.
     */
    private void openMostRecentGroupedFile(){
        File mostRecentFile = getMostRecentGroupedFile();
        
        if (mostRecentFile != null){
            openFileEditor(mostRecentFile);
        }
    }
    
    /**
     * Retrieves the most recently created grouped appointment file
     * from the GroupedObjects directory, sorted by creation time.
     *
     * @return the most recent grouped file, or null if none found.
     */
    private File getMostRecentGroupedFile(){
        File folder = new File("NormalizedEntityParser/GroupedObjects/");
        File[] files = folder.listFiles((dir, name) ->
                name.matches("GroupedObject\\(\\d{4}-\\d{1,2}-\\d{1,2}\\)\\(\\d{1,2}-\\d{1,2}-\\d{1,2}\\)\\.txt"));
                
        if (files == null || files.length == 0){
            JOptionPane.showMessageDialog(null, "No Grouped Object files found in the folder!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        Arrays.sort(files, (f1, f2) -> {
            try{
                BasicFileAttributes attr1 = Files.readAttributes(f1.toPath(), BasicFileAttributes.class);
                BasicFileAttributes attr2 = Files.readAttributes(f2.toPath(), BasicFileAttributes.class);
                return attr2.creationTime().compareTo(attr1.creationTime());
            }
            catch (IOException e){
                return 0;
            }
        });
        
        return files[0];
    }
    
    /**
     * Opens a given text file in an editable JTextArea inside a JFrame.
     * Provides options to save changes and print the content.
     *
     * @param dataFile the file to be opened and edited.
     */
    private void openFileEditor(File dataFile){
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
                public void insertUpdate(javax.swing.event.DocumentEvent e){
                    saveButton.setEnabled(true);
                }
                public void removeUpdate(javax.swing.event.DocumentEvent e){
                    saveButton.setEnabled(true);
                }
                public void changedUpdate(javax.swing.event.DocumentEvent e){
                    saveButton.setEnabled(true);
                }
            });
            
            saveButton.addActionListener((ActionEvent saveEvent) -> {
                try{
                    Files.write(dataFile.toPath(), textArea.getText().getBytes());
                    JOptionPane.showMessageDialog(null, "File saved successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    saveButton.setEnabled(false);
                }
                catch (IOException ex){
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
            editorPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            JFrame editorFrame = new JFrame("Edit File: " + dataFile.getName());
            editorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            editorFrame.add(editorPanel);
            editorFrame.pack();
            editorFrame.setLocationRelativeTo(null);
            editorFrame.setVisible(true);
            
        }
        catch (IOException e){
            JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Sends the given text content to the printer using Java's printing API.
     *
     * @param text the text content to print.
     * @param jobName the name of the print job (typically the filename).
     */
    private void printTextContent(String text, String jobName){
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName(jobName);

        Printable printable = (graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0){
                return Printable.NO_SUCH_PAGE;
            }
            
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

            g2d.setFont(new Font("Monospaced", Font.PLAIN, 10));
            g2d.setColor(Color.BLACK);

            String[] lines = text.split("\n");
            int y = 15;
            
            for (String line : lines){
                g2d.drawString(line, 10, y);
                y += 15;
                
                if (y > pageFormat.getImageableHeight() - 15){
                    break;
                }
            }
            
            return Printable.PAGE_EXISTS;
        };
        
        job.setPrintable(printable);

        if (job.printDialog()){
            try{
                job.print();
            }
            catch (PrinterException e){
                JOptionPane.showMessageDialog(null, "Printing failed: " + e.getMessage(),
                        "Print Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}