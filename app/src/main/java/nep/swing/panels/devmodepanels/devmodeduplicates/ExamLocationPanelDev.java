package nep.swing.panels.devmodepanels.devmodeduplicates;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

@SuppressWarnings("FieldMayBeFinal")
public class ExamLocationPanelDev {
    private JPanel examPanel;
    private JComboBox<String> locationDropdown;
    private int examPanelWidth, examPanelHeight;
    private ArrayList<String> locationArray;
    
    public ExamLocationPanelDev(){
        examPanelHeight = 175;
        examPanelWidth = 250;
        locationArray = new ArrayList<>();
        
        locationArray.add("Select a Location");
        locationArray.add("Mark A Hill");
        locationArray.add("G28");
        locationArray.add("Alternate Location");
        locationArray.add("Sexton");
        locationArray.add("Laws");
        
        examPanel = new JPanel();
        examPanel.setLayout(null);
        examPanel.setBackground(Color.WHITE);
        examPanel.setBounds(25, 25, examPanelWidth , examPanelHeight - 105);
        examPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        
        JPanel locationPanel = getjPanel();
        
        locationDropdown = new JComboBox<>();
        locationDropdown.setBounds(5, 25, examPanelWidth - 20, 30);
        
        for (String location : locationArray){
            locationDropdown.addItem(location);
        }
        
        examPanel.add(locationPanel);
        locationPanel.add(locationDropdown);
    }
    
    private JPanel getjPanel() {
        JPanel locationPanel = new JPanel();
        locationPanel.setLayout(null);
        locationPanel.setBounds(5, 5, examPanelWidth - 10, examPanelHeight - 10);
        locationPanel.setBackground(Color.WHITE);
        
        Border lineBorder = BorderFactory.createLineBorder(new Color(76, 74, 72, 255), 0);
        
        TitledBorder titledBorder = new TitledBorder(lineBorder, "Exam Location");
        titledBorder.setTitleFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        locationPanel.setBorder(titledBorder);
        return locationPanel;
    }
    
    /**
     * Retrieves the outermost container for the exam location panel.
     *
     * @return examPanel - the JPanel representing the exam location panel
     */
    public JPanel getExamLocationPanel(){
        return examPanel;
    }
    
    /** Developer Mode New Exam Location Adder
     *
     * @param location The location added to the ArrayList
     */
    public void addExamLocation(String location){
        locationArray.add(location);
        locationDropdown.addItem(location);
    }
    
    /**
     * Returns Null for debugging if no location is selected
     * @return selectedLocation
     */
    public String getExamLocation(){
        String selectedLocation = (String) locationDropdown.getSelectedItem();
        
        if(selectedLocation.equals("Select a Location")){
            return null;
        }
        else{
            return selectedLocation;
        }
    }
}
