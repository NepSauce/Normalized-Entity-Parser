package nep.swing.panels.devmodepanels.devmodeduplicates.rostertabpanelsdev;

import nep.swing.panels.devmodepanels.devmodeduplicates.*;

import javax.swing.*;

public class EncompassingTabDev {
    JTabbedPane tabbedPane;
    
    public EncompassingTabDev(ExamLocationPanelDev examLocationPanel, ExamAddedPanelDev examAddedPanel,
                          RosterAddedPanelDev rosterAddedPanel){
        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(25, 300, 250, 108);
        
        SettingsTabDev settingsTab = new SettingsTabDev(examLocationPanel, examAddedPanel, rosterAddedPanel);
        tabbedPane.add("Exam", settingsTab.getSettingsTab());
        
        ObjectTabDev objectTab = new ObjectTabDev(examLocationPanel, examAddedPanel, rosterAddedPanel);
        tabbedPane.add("Files", objectTab.getObjectTab());
        
    }
    
    public JTabbedPane getEncompassingTab(){
        return tabbedPane;
    }
    
}
