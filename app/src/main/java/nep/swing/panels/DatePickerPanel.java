package nep.swing.panels;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    @SuppressWarnings({"OverridableMethodCallInConstructor", "Convert2Lambda"})
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

        Border emptyBorder = BorderFactory.createEmptyBorder(5, 0, 0, 0);

        Border compoundBorder = BorderFactory.createCompoundBorder(emptyBorder, titledBorder);
        Border finalBorder = BorderFactory.createCompoundBorder(bevelBorder, compoundBorder);

        datePickerPanel.setBorder(finalBorder);


        dayComboBox = new JComboBox<>();
        dayComboBox.setBounds(0,0,0,0);
        dayComboBox.setEnabled(false);
        monthComboBox = new JComboBox<>();
        monthComboBox.setBounds(50,10,0,0);
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

        monthComboBox.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent monthPicked){
                String selectedMonth = (String) monthComboBox.getSelectedItem();

                if (selectedMonth != null && !selectedMonth.equals("  Month")){
                    dayComboBox.setEnabled(true);
                    populateDayComboBox(selectedMonth);
                }
                else{
                    dayComboBox.setEnabled(false);
                    dayComboBox.removeAllItems();
                    dayComboBox.addItem("  Day  ");
                }
            }
        });

        yearComboBox.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent yearPicked){
                String selectedMonth = (String) monthComboBox.getSelectedItem();

                if (selectedMonth != null && !selectedMonth.equals("  Month  ")){
                    populateDayComboBox(selectedMonth);
                }
            }
        });

    }

    private void populateDayComboBox(String month){
        dayComboBox.removeAllItems();
        dayComboBox.addItem("  Day  ");
        int daysInMonth = getDaysInMonth(month);

        for (int i = 1; i <= daysInMonth; i++){
            dayComboBox.addItem(String.valueOf(i));
        }
    }

    private int getDaysInMonth(String month){
        return switch (month) {
            case "January", "March", "May", "July", "August", "October", "December" -> 31;
            case "April", "June", "September", "November" -> 30;
            case "February" -> isLeapYear() ? 29 : 28;
            default -> 0;
        };
    }

    private boolean isLeapYear(){
        String selectedYear = (String) yearComboBox.getSelectedItem();

        if (selectedYear == null || selectedYear.equals("  Year  ")){
            return false;
        }

        int year = Integer.parseInt(selectedYear);
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private void populateMonthComboBox(){
        monthArray = new ArrayList<>();

        Collections.addAll(monthArray, "January", "February", "March", "April", "May", "June", 
            "July", "August", "September", "October", "November", "December");
        
        for (String month : monthArray){
            monthComboBox.addItem(month);
        }
    }

    private void populateYearComboBox(){
        currentYear = Year.now().getValue();
        
        for (int i = currentYear - 5; i <= currentYear + 5; i++){
            yearComboBox.addItem(String.valueOf(i));
        }
    }

    public JPanel getDatePickerPanel(){
        return datePickerPanel; 
    }
}
