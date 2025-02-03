package nep.swing.panels;

import java.awt.Color;
import java.awt.FlowLayout;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

@SuppressWarnings("FieldMayBeFinal")
public class DatePickerPanel{
    private int datePanelWidth;
    private int datePanelHeight;
    private JComboBox<String> dayComboBox;
    private JComboBox<String> monthComboBox;
    private JComboBox<String> yearComboBox;
    private JPanel datePickerPanel;
    private ArrayList<String> monthArray;
    private int currentYear;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public DatePickerPanel(){
        datePanelHeight = 175;
        datePanelWidth = 250;

        datePickerPanel = new JPanel();
        datePickerPanel.setLayout(new FlowLayout());
        datePickerPanel.setBackground(Color.WHITE);
        datePickerPanel.setBounds(25, 105, datePanelWidth, datePanelHeight - 105);
        datePickerPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        Border bevelBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);

        Border lineBorder = BorderFactory.createLineBorder(new Color(80, 80, 80), 0);
        TitledBorder titledBorder = new TitledBorder(lineBorder, "Select a Date");
        titledBorder.setTitleFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));

        Border compoundBorder = BorderFactory.createCompoundBorder(bevelBorder, titledBorder);
        datePickerPanel.setBorder(compoundBorder);

        dayComboBox = new JComboBox<>();
        dayComboBox.setBounds(0,0,0,0);
        monthComboBox = new JComboBox<>();
        monthComboBox.setBounds(10,10,0,0);
        yearComboBox = new JComboBox<>();
        yearComboBox.setBounds(0,0,0,0);

        datePickerPanel.add(yearComboBox);   
        yearComboBox.addItem("  Year  ");
        datePickerPanel.add(monthComboBox);
        monthComboBox.addItem("  Month  ");
        datePickerPanel.add(dayComboBox);
        dayComboBox.addItem("  Day  ");

        populateYearComboBox();
        populateMonthComboBox();

    }

    public void populateDayComboBox(String month){
        
    }

    public void populateMonthComboBox(){
        monthArray = new ArrayList<>();

        Collections.addAll(monthArray, "January", "February", "March", "April", "May", "June", 
            "July", "August", "September", "October", "November", "December");
        
        for (String month : monthArray){
            monthComboBox.addItem(month);
        }
    }

    public void populateYearComboBox(){
        currentYear = Year.now().getValue();
        
        for (int i = currentYear - 5; i <= currentYear + 5; i++){
            yearComboBox.addItem(String.valueOf(i));
        }
    }

    public JPanel getDatePickerPanel(){
        return datePickerPanel; 
    }
}
