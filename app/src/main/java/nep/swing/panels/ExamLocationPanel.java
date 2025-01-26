package nep.swing.panels;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

@SuppressWarnings("FieldMayBeFinal")
public class ExamLocationPanel {
    private JPanel examPanel;
    private JComboBox<String> locationDropdown;
    private int examPanelWidth, examPanelHeight;
    private ArrayList<String> locationArray;
    
    public ExamLocationPanel(){
        examPanelHeight = 175;
        examPanelWidth = 250;
        locationArray = new ArrayList<>();

        locationArray.add("Select a Location");
        locationArray.add("Mark A Hill");
        locationArray.add("G28");
        locationArray.add("Alternate Location");
        locationArray.add("Sexton");

        examPanel = new JPanel();
        examPanel.setLayout(null);
        examPanel.setBackground(Color.WHITE);
        examPanel.setBounds(25, 35, examPanelWidth , examPanelHeight - 50);
        examPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        JPanel locationPanel = new JPanel();
        locationPanel.setLayout(null);
        locationPanel.setBounds(5, 5, examPanelWidth - 10, examPanelHeight - 10);
        locationPanel.setBackground(Color.WHITE);

        Border lineBorder = BorderFactory.createLineBorder(new Color(76, 74, 72, 255), 0);

        TitledBorder titledBorder = new TitledBorder(lineBorder, "Exam Location");
        titledBorder.setTitleFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        locationPanel.setBorder(titledBorder);

        locationDropdown = new JComboBox<>();
        locationDropdown.setBounds(5, 20, examPanelWidth - 20, 30);

        for (String location : locationArray){
            locationDropdown.addItem(location);
        }

        examPanel.add(locationPanel);
        locationPanel.add(locationDropdown);
    }

    /**
     * Retrieves the outermost container for the exam location panel.
     *
     * @return examPanel - the JPanel representing the exam location panel
     */
    public JPanel getExamLocationPanel(){
        return examPanel;
    }

    /**
     * 
     * @param location The location added to the ArrayList
     */
    public void addExamLocation(String location){
        locationArray.add(location);
    }
}
