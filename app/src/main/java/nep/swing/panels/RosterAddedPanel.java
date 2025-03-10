package nep.swing.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class RosterAddedPanel{
    private boolean checkFileExists;
    private File selectedFile;
    private JButton addRosterPDFButton;
    private JButton removeRosterPDFButton;
    private JPanel rosterBrowsingPanel;
    private JPanel rosterTextPanel;
    private int browsingPanelHeight;
    private int browsingPanelWidth;
    private String fileAbsolutePath;

    public RosterAddedPanel(){
        this.checkFileExists = false;
        this.fileAbsolutePath = null;

        browsingPanelHeight = 50;
        browsingPanelWidth = 250;

        rosterBrowsingPanel = new JPanel();
        rosterBrowsingPanel.setLayout(null);
        rosterBrowsingPanel.setBackground(Color.WHITE);
        rosterBrowsingPanel.setBounds(25, 175, browsingPanelWidth, browsingPanelHeight);
        rosterBrowsingPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        rosterTextPanel = new JPanel();
        rosterTextPanel.setLayout(null);
        rosterTextPanel.setBounds(5, 5, browsingPanelWidth - 20, browsingPanelHeight - 10);
        rosterTextPanel.setBackground(Color.WHITE);

        Border lineBorder = BorderFactory.createLineBorder(new Color(80, 80, 80), 0);
        
        TitledBorder titledBorder = new TitledBorder(lineBorder, "Select Roster PDF");
        titledBorder.setTitleFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        rosterTextPanel.setBorder(titledBorder);

        rosterBrowsingPanel.add(rosterTextPanel);

        addRosterPDFButton = new JButton("+");
        addRosterPDFButton.setBounds(200, 5, 30, 30);
        addRosterPDFButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));
                int returnValue = fileChooser.showOpenDialog(null);
                
                if (returnValue == JFileChooser.APPROVE_OPTION){
                    selectedFile = fileChooser.getSelectedFile();
                    fileAbsolutePath = selectedFile.getAbsolutePath();
                    checkFileExists = true;
                } 
                else{
                    checkFileExists = false;
                }
            }
        });

        rosterTextPanel.add(addRosterPDFButton);
    }

    public void displayRosterInfo(){

    }

    public void clearRosterInfo(){
        
    }

    public JPanel getRosterBrowsingPanel(){
        return rosterBrowsingPanel;
    }

    public JPanel getRosterTextPanel(){
        return rosterTextPanel;
    }

    public String getAbsoluteFilePath(){
        return fileAbsolutePath;
    }

}