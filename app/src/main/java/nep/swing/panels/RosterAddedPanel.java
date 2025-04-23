package nep.swing.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class RosterAddedPanel{
    private boolean checkFileExists;
    private File selectedFile;
    private JButton addRosterPDFButton;
    private JButton removeRosterPDFButton;
    private JLabel rosterPDFLabel;
    private JPanel rosterBrowsingPanel;
    private JPanel rosterButtonPanel;
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

        rosterButtonPanel = new JPanel();
        rosterButtonPanel.setLayout(null);
        rosterButtonPanel.setBounds(5, 5, browsingPanelWidth - 20, browsingPanelHeight - 10);
        rosterButtonPanel.setBackground(Color.WHITE);

        Border lineBorder = BorderFactory.createLineBorder(new Color(80, 80, 80), 0);
        
        TitledBorder titledBorder = new TitledBorder(lineBorder, "Select Roster PDF");
        titledBorder.setTitleFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        rosterButtonPanel.setBorder(titledBorder);

        rosterPDFLabel = new JLabel();
        rosterPDFLabel.setLayout(null);
        rosterPDFLabel.setBounds(10, 5, browsingPanelWidth - 20, browsingPanelHeight - 10);
        rosterPDFLabel.setBackground(Color.BLACK);


        rosterBrowsingPanel.add(rosterButtonPanel);

        addRosterPDFButton = new JButton("+");
        addRosterPDFButton.setBounds(200, 5, 30, 30);
        addRosterPDFButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                openFileExplorerAndValidate();
            }
        });

        removeRosterPDFButton = new JButton();
        removeRosterPDFButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                clearRosterInfo();
            }
        });

        rosterButtonPanel.add(addRosterPDFButton);
        rosterButtonPanel.add(rosterPDFLabel);
    }

    public void displayRosterInfo(){

    }

    public void clearRosterInfo(){
        
    }

    public void openFileExplorerAndValidate(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));
        int returnValue = fileChooser.showOpenDialog(null);
        
        if (returnValue == JFileChooser.APPROVE_OPTION){
            selectedFile = fileChooser.getSelectedFile();
            fileAbsolutePath = selectedFile.getAbsolutePath();
            checkFileExists = true;
            setRosterLabel(selectedFile.getName());
        } 
        else{
            checkFileExists = false;
        }
    }

    public JPanel getRosterBrowsingPanel(){
        return rosterBrowsingPanel;
    }

    public JPanel getRosterButtonPanel(){
        return rosterButtonPanel;
    }

    public String getAbsoluteFilePath(){
        return fileAbsolutePath;
    }

    public boolean getCheckFileExists(){
        return checkFileExists;
    }

    public void setRosterLabel(String newLabel){
        if (newLabel.length() > 20) {
            rosterPDFLabel.setText(newLabel.substring(0, 15) + "...pdf");
        }
        else{
            rosterPDFLabel.setText(newLabel);
        }
    }

}