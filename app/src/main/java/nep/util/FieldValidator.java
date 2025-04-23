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
        if (fileAbsolutePath == null){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean validateLocationSelection(){
        if (location == null){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean validateDaySelection(){
        if (day == 0){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean validateMonthSelection(){
        if (month == null){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean validateYearSelection(){
        if (year == 0){
            return true;
        }
        else{
            return false;
        }
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
    
        System.out.println(this.errorMessage);
    }
    
    public String getErrorMessage(){
        return errorMessage;
    }
}