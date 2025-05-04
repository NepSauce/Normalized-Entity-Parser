package nep.swing.panels;

import javax.swing.JTabbedPane;

public class DevTab {
    JTabbedPane devPane;

    public DevTab(ExamLocationPanel examLocationPanel, ExamAddedPanel examAddedPanel, 
        DatePickerPanel datePickerPanel, RosterAddedPanel rosterAddedPanel){
        devPane = new JTabbedPane();
        devPane.setBounds(300 , 240, 250, 85);
    }

    public JTabbedPane getDevTab(){
        return devPane;
    }
}
