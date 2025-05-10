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
    
    private JLabel rostersAddedLabel;
    private JLabel coursesFoundLabel;
    private JLabel combinedEntriesLabel;
    private JLabel groupedEntriesLabel;
    private JLabel removedEntriesLabel;
    
    private int rostersAddedCount = 0;
    private int coursesFoundCount = 0;
    private int combinedEntriesCount = 0;
    private int groupedEntriesCount = 0;
    private int removedEntriesCount = 0;
    
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
        
        rostersAddedLabel = new JLabel("Rosters Added:   0");
        coursesFoundLabel = new JLabel("Courses Found:   0");
        combinedEntriesLabel = new JLabel("Combined Entries:   0");
        groupedEntriesLabel = new JLabel("Grouped Entries:   0");
        removedEntriesLabel = new JLabel("Removed Entries:   0");
        
        rostersAddedLabel.setBounds(7, 25, 200, 20);
        coursesFoundLabel.setBounds(7, 42, 200, 20);
        combinedEntriesLabel.setBounds(7, 59, 200, 20);
        groupedEntriesLabel.setBounds(7, 76, 200, 20);
        removedEntriesLabel.setBounds(7, 93, 200, 20);
        
        infoListPanel.add(rostersAddedLabel);
        infoListPanel.add(coursesFoundLabel);
        infoListPanel.add(combinedEntriesLabel);
        infoListPanel.add(groupedEntriesLabel);
        infoListPanel.add(removedEntriesLabel);
        
        cumulativePanel.add(infoListPanel);
    }
    
    public JPanel getCumulativeInfoPanel() {
        return cumulativePanel;
    }
    
    private void updateLabels() {
        rostersAddedLabel.setText("Rosters Added:   " + rostersAddedCount);
        coursesFoundLabel.setText("Courses Found:   " + coursesFoundCount);
        combinedEntriesLabel.setText("Combined Entries:   " + combinedEntriesCount);
        groupedEntriesLabel.setText("Grouped Entries:   " + groupedEntriesCount);
        removedEntriesLabel.setText("Removed Entries:   " + removedEntriesCount);
    }
    
    public void setRostersAdded(int value) {
        rostersAddedCount = value;
        updateLabels();
    }
    
    public void setCoursesFound(int value) {
        coursesFoundCount = value;
        updateLabels();
    }
    
    public void setCombinedEntries(int value) {
        combinedEntriesCount = value;
        updateLabels();
    }
    
    public void setGroupedEntries(int value) {
        groupedEntriesCount = value;
        updateLabels();
    }
    
    public void setRemovedEntries(int value) {
        removedEntriesCount = value;
        updateLabels();
    }
    
    public void incrementRostersAdded(int amount) {
        rostersAddedCount += amount;
        updateLabels();
    }
    
    public void incrementCoursesFound(int amount) {
        coursesFoundCount += amount;
        updateLabels();
    }
    
    public void incrementCombinedEntries(int amount) {
        combinedEntriesCount += amount;
        updateLabels();
    }
    
    public void incrementGroupedEntries(int amount) {
        groupedEntriesCount += amount;
        updateLabels();
    }
    
    public void incrementRemovedEntries(int amount) {
        removedEntriesCount += amount;
        updateLabels();
    }
}
