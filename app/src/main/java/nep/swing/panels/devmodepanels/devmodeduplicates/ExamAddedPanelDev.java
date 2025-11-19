package nep.swing.panels.devmodepanels.devmodeduplicates;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import nep.entityclass.RosterEntityDetails;
import nep.swing.panels.devmodepanels.devmodeduplicates.rostertabpanelsdev.SelectionButtonPaletteDev;

@SuppressWarnings("FieldMayBeFinal")
public class ExamAddedPanelDev{
    private JPanel selectedPanel;
    private JPanel examListPanel;
    private int selectedPanelWidth;
    private int selectedPanelHeight;
    private int currentXPosition = 25;
    private ArrayList<String> addedRosterArray;
    private ArrayList<JPanel> rosterPanelsDev = new ArrayList<>();
    private LinkedList<RosterEntityDetails> rosterObjectEntityList = SelectionButtonPaletteDev.getRosterEntityList();
    
    @SuppressWarnings("Convert2Lambda")
    public ExamAddedPanelDev(LoggingPanelDev loggingPanel){
        selectedPanelHeight = 60;
        selectedPanelWidth = 250;
        
        selectedPanel = new JPanel();
        selectedPanel.setLayout(null);
        selectedPanel.setBackground(Color.WHITE);
        selectedPanel.setBounds(25, 25, selectedPanelWidth, selectedPanelHeight + 65);
        selectedPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        
        examListPanel = new JPanel();
        examListPanel.setLayout(null);
        examListPanel.setBounds(5, 5, selectedPanelWidth - 10, selectedPanelHeight + 55);
        examListPanel.setBackground(Color.WHITE);
        
        Border lineBorder = BorderFactory.createLineBorder(new Color(80, 80, 80), 0);
        
        TitledBorder titledBorder = new TitledBorder(lineBorder, "Selected Exams");
        titledBorder.setTitleFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        examListPanel.setBorder(titledBorder);
        
        selectedPanel.add(examListPanel);
    }
    
    public JPanel getExamAddedPanel(){
        return selectedPanel;
    }
    
    public JPanel getExamListPanel(){
        return examListPanel;
    }
    
    public void getRosterInformation(){
    
    }
    
    /**
     *
     * @param rosterDetails
     */
    public void addRosterToPanel(String rosterDetails){
        JPanel newPanel = new JPanel();
        newPanel.setBounds(5, currentXPosition, selectedPanelWidth - 15, 25);
        newPanel.setBackground(new Color(238,238,238,255));
        
        JLabel detailString = new JLabel(rosterDetails);
        detailString.setBounds(0, 0, 0, 20);
        
        newPanel.add(detailString);
        examListPanel.add(newPanel);
        
        rosterPanelsDev.add(newPanel);
        
        currentXPosition += 30;
        selectedPanel.revalidate();
        selectedPanel.repaint();
    }
    
    
    public void clearAllRostersFromPanel(){
        for (JPanel panel : rosterPanelsDev){
            examListPanel.remove(panel);
        }
        rosterPanelsDev.clear();
        rosterObjectEntityList.clear();
        currentXPosition = 25;
        selectedPanel.revalidate();
        selectedPanel.repaint();
    }
    
    public void undoLastRoster(){
        if (!rosterPanelsDev.isEmpty()) {
            JPanel lastPanel = rosterPanelsDev.removeLast();
            examListPanel.remove(lastPanel);
            rosterObjectEntityList.removeLast();
            
            currentXPosition -= 30;
            selectedPanel.revalidate();
            selectedPanel.repaint();
        }
    }
}
