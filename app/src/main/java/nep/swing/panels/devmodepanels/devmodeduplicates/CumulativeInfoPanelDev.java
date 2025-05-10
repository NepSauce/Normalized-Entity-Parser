package nep.swing.panels.devmodepanels.devmodeduplicates;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

@SuppressWarnings("FieldMayBeFinal")
public class CumulativeInfoPanelDev {
    private JPanel cumulativePanel;
    private JPanel infoListPanel;
    
    private int panelWidth;
    private int panelHeight;
    private int labelYOffset = 15;
    
    private JLabel totalRostersLabel;
    private JLabel entriesFoundLabel;
    private JLabel coursesFoundLabel;
    private JLabel entriesCombinedLabel;
    private JLabel entriesRemovedLabel;
    
    private int totalRostersCount = 0;
    private int entriesFoundCount = 0;
    private int coursesFoundCount = 0;
    private int entriesCombinedCount = 0;
    private int entriesRemovedCount = 0;
    
    public CumulativeInfoPanelDev(ExamLocationPanelDev examLocationPanel, ExamAddedPanelDev examAddedPanel,
                                  RosterAddedPanelDev rosterAddedPanel){
        panelWidth = 225;
        panelHeight = 125;
        
        cumulativePanel = new JPanel();
        cumulativePanel.setLayout(null);
        cumulativePanel.setBackground(Color.WHITE);
        cumulativePanel.setBounds(300, 25, panelWidth, panelHeight);
        cumulativePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        
        infoListPanel = new JPanel();
        infoListPanel.setLayout(null);
        infoListPanel.setBounds(5, 5, panelWidth - 10, panelHeight - 10);
        infoListPanel.setBackground(Color.WHITE);
        
        Border lineBorder = BorderFactory.createLineBorder(new Color(80, 80, 80), 0);
        TitledBorder titledBorder = new TitledBorder(lineBorder, "Cumulative Info");
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 16));
        infoListPanel.setBorder(titledBorder);
        
        totalRostersLabel = new JLabel("Total Rosters:   0");
        entriesFoundLabel = new JLabel("Entries Found:   0");
        coursesFoundLabel = new JLabel("Courses Found:   0");
        entriesCombinedLabel = new JLabel("Entries Combined:   0");
        entriesRemovedLabel = new JLabel("Entries Removed:   0");
        
        totalRostersLabel.setBounds(7, 25, 200, 20);
        entriesFoundLabel.setBounds(7, 42, 200, 20);
        coursesFoundLabel.setBounds(7, 59, 200, 20);
        entriesCombinedLabel.setBounds(7, 76, 200, 20);
        entriesRemovedLabel.setBounds(7, 93, 200, 20);
        
        infoListPanel.add(totalRostersLabel);
        infoListPanel.add(entriesFoundLabel);
        infoListPanel.add(coursesFoundLabel);
        infoListPanel.add(entriesCombinedLabel);
        infoListPanel.add(entriesRemovedLabel);
        
        cumulativePanel.add(infoListPanel);
    }
    
    public JPanel getCumulativeInfoPanel() {
        return cumulativePanel;
    }
    
    private void updateLabels() {
        totalRostersLabel.setText("Total Rosters:   " + totalRostersCount);
        entriesFoundLabel.setText("Entries Found:   " + entriesFoundCount);
        coursesFoundLabel.setText("Courses Found:   " + coursesFoundCount);
        entriesCombinedLabel.setText("Entries Combined:   " + entriesCombinedCount);
        entriesRemovedLabel.setText("Entries Removed:   " + entriesRemovedCount);
    }
    
    public void setTotalRosters(int value) {
        totalRostersCount = value;
        updateLabels();
    }
    
    public void setEntriesFound(int value) {
        entriesFoundCount = value;
        updateLabels();
    }
    
    public void setCoursesFound(int value) {
        coursesFoundCount = value;
        updateLabels();
    }
    
    public void setEntriesCombined(int value) {
        entriesCombinedCount = value;
        updateLabels();
    }
    
    public void setEntriesRemoved(int value) {
        entriesRemovedCount = value;
        updateLabels();
    }
    
    public void incrementTotalRosters(int amount) {
        totalRostersCount += amount;
        updateLabels();
    }
    
    public void incrementEntriesFound(int amount) {
        entriesFoundCount += amount;
        updateLabels();
    }
    
    public void incrementCoursesFound(int amount) {
        coursesFoundCount += amount;
        updateLabels();
    }
    
    public void incrementEntriesCombined(int amount) {
        entriesCombinedCount += amount;
        updateLabels();
    }
    
    public void incrementEntriesRemoved(int amount) {
        entriesRemovedCount += amount;
        updateLabels();
    }
}
