package nep.swing.panels.devmodepanels.devmodeduplicates;

import nep.entityclass.RosterEntityDetails;
import nep.swing.panels.rostertabpanels.SelectionButtonPalette;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

@SuppressWarnings("FieldMayBeFinal")
public class ExamAddedPanelDev {
    private JPanel selectedPanel;
    private JPanel examListPanel;
    private int selectedPanelWidth;
    private int selectedPanelHeight;
    private int currentXPosition = 25;
    private ArrayList<String> addedRosterArray;
    private ArrayList<JPanel> rosterPanels = new ArrayList<>();
    private LinkedList<RosterEntityDetails> rosterObjectEntityList = SelectionButtonPalette.getRosterEntityList();
    
    @SuppressWarnings("Convert2Lambda")
    public ExamAddedPanelDev(){
        selectedPanelHeight = 125;
        selectedPanelWidth = 250;
        
        selectedPanel = new JPanel();
        selectedPanel.setLayout(null);
        selectedPanel.setBackground(Color.WHITE);
        selectedPanel.setBounds(25, 25, selectedPanelWidth, selectedPanelHeight);
        selectedPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        
        examListPanel = new JPanel();
        examListPanel.setLayout(null);
        examListPanel.setBackground(Color.WHITE);
        examListPanel.setPreferredSize(new java.awt.Dimension(selectedPanelWidth - 25, selectedPanelHeight));
        
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(),
                "Added Rosters",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Arial", java.awt.Font.BOLD, 16)
        );
        examListPanel.setBorder(titledBorder);
        
        JScrollPane scrollPane = new JScrollPane(examListPanel);
        scrollPane.setBounds(5, 5, selectedPanelWidth - 10, selectedPanelHeight - 7);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        selectedPanel.add(scrollPane);
        
    }
    
    public JPanel getExamAddedPanel(){
        return selectedPanel;
    }
    
    public JPanel getExamListPanel(){
        return examListPanel;
    }

    /**
     *
     * @param rosterDetails
     */
    public void addRosterToPanel(String rosterDetails){
        JPanel newPanel = new JPanel();
        newPanel.setBounds(5, currentXPosition, selectedPanelWidth - 40, 25);
        newPanel.setBackground(new Color(238, 238, 238, 255));
        
        JLabel detailString = new JLabel(rosterDetails);
        newPanel.add(detailString);
        examListPanel.add(newPanel);
        
        rosterPanels.add(newPanel);
        
        currentXPosition += 30;
        examListPanel.setPreferredSize(new java.awt.Dimension(selectedPanelWidth - 25, currentXPosition + 30));
        
        // Force layout updates for both examListPanel and its scroll parent
        examListPanel.revalidate();
        examListPanel.repaint();
        
        JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, examListPanel);
        if (scrollPane != null) {
            scrollPane.revalidate();
            scrollPane.repaint();
        }
    }
    
    
    public void clearAllRostersFromPanel(){
        for (JPanel panel : rosterPanels){
            examListPanel.remove(panel);
        }
        rosterPanels.clear();
        rosterObjectEntityList.clear();
        currentXPosition = 25;
        selectedPanel.revalidate();
        selectedPanel.repaint();
    }
    
    public void undoLastRoster(){
        if (!rosterPanels.isEmpty()) {
            JPanel lastPanel = rosterPanels.removeLast();
            examListPanel.remove(lastPanel);
            rosterObjectEntityList.removeLast();
            
            currentXPosition -= 30;
            selectedPanel.revalidate();
            selectedPanel.repaint();
        }
    }
}
