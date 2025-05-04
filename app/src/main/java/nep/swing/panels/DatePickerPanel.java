package nep.swing.panels;

import java.awt.Color;
import java.awt.Font;
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
public class DatePickerPanel {
    private int datePanelWidth;
    private int datePanelHeight;
    private JComboBox<String> dayComboBox;
    private JComboBox<String> monthComboBox;
    private JComboBox<String> yearComboBox;
    private JPanel datePickerPanel;
    private ArrayList<String> monthArray;
    private int currentYear;

    public DatePickerPanel() {
        datePanelHeight = 175;
        datePanelWidth = 250;

        datePickerPanel = new JPanel();
        datePickerPanel.setLayout(null);
        datePickerPanel.setBackground(Color.WHITE);
        datePickerPanel.setBounds(25, 105, datePanelWidth, datePanelHeight - 105);
        datePickerPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        Border bevelBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        Border lineBorder = BorderFactory.createLineBorder(new Color(80, 80, 80), 0);
        TitledBorder titledBorder = new TitledBorder(lineBorder, "Select a Date");
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));

        Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 0, 0);
        Border compoundBorder = BorderFactory.createCompoundBorder(emptyBorder, titledBorder);
        Border finalBorder = BorderFactory.createCompoundBorder(bevelBorder, compoundBorder);
        datePickerPanel.setBorder(finalBorder);

        dayComboBox = new JComboBox<>();
        dayComboBox.setEnabled(false);
        monthComboBox = new JComboBox<>();
        yearComboBox = new JComboBox<>();

        yearComboBox.addItem("  Year  ");
        monthComboBox.addItem("  Month  ");
        dayComboBox.addItem("  Day  ");

        yearComboBox.setBounds(10, 30, 70, 25);
        monthComboBox.setBounds(90, 30, 70, 25);
        dayComboBox.setBounds(170, 30, 70, 25);

        datePickerPanel.add(yearComboBox);
        datePickerPanel.add(monthComboBox);
        datePickerPanel.add(dayComboBox);

        populateYearComboBox();
        populateMonthComboBox();

        monthComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent monthPicked) {
                String selectedMonth = (String) monthComboBox.getSelectedItem();

                if (selectedMonth != null && !selectedMonth.equals("  Month")) {
                    dayComboBox.setEnabled(true);
                    populateDayComboBox(selectedMonth);
                } else {
                    dayComboBox.setEnabled(false);
                    dayComboBox.removeAllItems();
                    dayComboBox.addItem("  Day  ");
                }
            }
        });

        yearComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent yearPicked) {
                String selectedMonth = (String) monthComboBox.getSelectedItem();

                if (selectedMonth != null && !selectedMonth.equals("  Month")) {
                    populateDayComboBox(selectedMonth);
                }
            }
        });
    }

    private void populateDayComboBox(String month) {
        dayComboBox.removeAllItems();
        dayComboBox.addItem("  Day  ");
        int daysInMonth = getDaysInMonth(month);

        for (int i = 1; i <= daysInMonth; i++) {
            dayComboBox.addItem(String.valueOf(i));
        }
    }

    private int getDaysInMonth(String month) {
        switch (month) {
            case "January":
            case "March":
            case "May":
            case "July":
            case "August":
            case "October":
            case "December":
                return 31;
            case "April":
            case "June":
            case "September":
            case "November":
                return 30;
            case "February":
                return isLeapYear() ? 29 : 28;
            default:
                return 0;
        }
    }

    private boolean isLeapYear() {
        String selectedYear = (String) yearComboBox.getSelectedItem();

        if (selectedYear == null || selectedYear.equals("  Year  ")) {
            return false;
        }
        int year = Integer.parseInt(selectedYear);

        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private void populateMonthComboBox() {
        monthArray = new ArrayList<>();
        Collections.addAll(monthArray, "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December");

        for (String month : monthArray) {
            monthComboBox.addItem(month);
        }
    }

    private void populateYearComboBox() {
        currentYear = Year.now().getValue();

        for (int i = currentYear - 5; i <= currentYear + 5; i++) {
            yearComboBox.addItem(String.valueOf(i));
        }
    }

    public JPanel getDatePickerPanel() {
        return datePickerPanel;
    }

    public int getDayInt() {
        String dayString = (String) dayComboBox.getSelectedItem();

        if (dayString.equals("  Day  ")) {
            return 0;
        }
        return Integer.parseInt(dayString);
    }

    public String getMonthString() {
        String monthString = (String) monthComboBox.getSelectedItem();

        if (monthString.equals("  Month  ")) {
            return null;
        }
        return monthString;
    }

    public int getYearInt(){
        String yearString = (String) yearComboBox.getSelectedItem();

        if (yearString.equals("  Year  ")) {
            return 0;
        }
        return Integer.parseInt(yearString);
    }

    public void resetDate(){
        yearComboBox.removeAllItems();
        yearComboBox.addItem("  Year  ");
        populateYearComboBox();

        monthComboBox.removeAllItems();
        monthComboBox.addItem("  Month  ");
        populateMonthComboBox();

        dayComboBox.removeAllItems();
        dayComboBox.addItem("  Day  ");
        dayComboBox.setEnabled(false);     
    }
}
