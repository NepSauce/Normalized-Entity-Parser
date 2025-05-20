package nep.util;

import java.util.ArrayList;

public class FieldValidator{
    private String fileAbsolutePath;
    private String location;
    private int day;
    private String month;
    private int year;
    private String errorMessage;

    public FieldValidator(String fileAbsolutePath, String location, int day, String month, int year){
        this.fileAbsolutePath = fileAbsolutePath;
        this.location = location;
        this.day = day;
        this.month = month;
        this.year = year;
        this.errorMessage = null;
    }

    private boolean validatePDFSelection(){
        return fileAbsolutePath == null;
    }

    private boolean validateLocationSelection(){
        return location == null;
    }

    private boolean validateDaySelection(){
        return day == 0;
    }

    private boolean validateMonthSelection(){
        return month == null;
    }

    private boolean validateYearSelection(){
        return year == 0;
    }


    public void generateErrorMessage() {
        StringBuilder errorMessageBuilder = new StringBuilder(); 
    
        if (validateLocationSelection() || validateDaySelection() 
            || validateMonthSelection() || validateYearSelection()
            || validatePDFSelection()) {
            errorMessageBuilder.append("Please Insert a Valid ");
            
            ArrayList<String> missingFields = new ArrayList<>();
            if (validatePDFSelection()){
                missingFields.add("PDF");
            }
            if (validateLocationSelection()){
                missingFields.add("Location");
            }
            if (validateDaySelection()){
                missingFields.add("Day");
            }
            if (validateMonthSelection()){
                missingFields.add("Month");
            }
            if (validateYearSelection()){
                missingFields.add("Year");
            }
    
            errorMessageBuilder.append(String.join(", ", missingFields));
            this.errorMessage = errorMessageBuilder.toString();
        } 
        else{
            this.errorMessage = null;
        }
    }
    
    public String getErrorMessage(){
        return errorMessage;
    }
}