package nep.swing.panels.devmodepanels.devmodeduplicates;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

@SuppressWarnings("FieldMayBeFinal")
public class RosterInfoPanelDev {
    private JPanel mainPanel;
    private JPanel rosterListPanel;
    private int panelWidth;
    private int panelHeight;
    private int currentYPosition = 25;
    private ArrayList<JPanel> rosterInfoSections;
    
    public RosterInfoPanelDev(ExamLocationPanelDev examLocationPanel, ExamAddedPanelDev examAddedPanel,
                              RosterAddedPanelDev rosterAddedPanel){
        panelWidth = 225;
        panelHeight = 245;
        rosterInfoSections = new ArrayList<>();
        
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBounds(300, 155, panelWidth, panelHeight);
        mainPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        
        rosterListPanel = new JPanel();
        rosterListPanel.setLayout(null);
        rosterListPanel.setBackground(Color.WHITE);
        rosterListPanel.setPreferredSize(new Dimension(panelWidth - 30, panelHeight));
        
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(),
                "Roster Info (WIP)",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 16)
        );
        rosterListPanel.setBorder(titledBorder);
        
        JScrollPane scrollPane = new JScrollPane(rosterListPanel);
        scrollPane.setBounds(5, 5, panelWidth - 10, panelHeight - 10);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(scrollPane);
    }
    
    public JPanel getRosterInfoPanel() {
        return mainPanel;
    }
    
    public void addRosterInfo(String fileName, int entriesFound, int coursesFound, int entriesRemoved) {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(4, 1));
        infoPanel.setBounds(5, currentYPosition, panelWidth - 50, 75);
        infoPanel.setBackground(new Color(240, 240, 240));
        
        Font font = new Font("Monospaced", Font.PLAIN, 12);
        
        JLabel fileLabel = new JLabel("File Name:         " + fileName);
        JLabel entriesLabel = new JLabel("Entries Found:     " + entriesFound);
        JLabel coursesLabel = new JLabel("Courses Found:     " + coursesFound);
        JLabel removedLabel = new JLabel("Entries Removed:   " + entriesRemoved);
        
        fileLabel.setFont(font);
        entriesLabel.setFont(font);
        coursesLabel.setFont(font);
        removedLabel.setFont(font);
        
        infoPanel.add(fileLabel);
        infoPanel.add(entriesLabel);
        infoPanel.add(coursesLabel);
        infoPanel.add(removedLabel);
        
        rosterListPanel.add(infoPanel);
        rosterInfoSections.add(infoPanel);
        
        currentYPosition += 85;
        rosterListPanel.setPreferredSize(new Dimension(panelWidth - 30, currentYPosition + 30));
        rosterListPanel.revalidate();
        rosterListPanel.repaint();
    }
    
    public void clearRosterInfo() {
        for (JPanel panel : rosterInfoSections) {
            rosterListPanel.remove(panel);
        }
        rosterInfoSections.clear();
        currentYPosition = 25;
        rosterListPanel.setPreferredSize(new Dimension(panelWidth - 30, panelHeight));
        rosterListPanel.revalidate();
        rosterListPanel.repaint();
    }
    
    public void undoLastRosterInfo() {
        if (!rosterInfoSections.isEmpty()) {
            JPanel last = rosterInfoSections.remove(rosterInfoSections.size() - 1);
            rosterListPanel.remove(last);
            currentYPosition -= 85;
            rosterListPanel.revalidate();
            rosterListPanel.repaint();
        }
    }
}
