package nep.util;

public class DateValidator{
    private int day;
    private String month;
    private int year;
    private String errorMessage;

    public DateValidator(int day, String month, int year){
        this.day = day;
        this.month = month;
        this.year = year;
        this.errorMessage = null;
    }

    public void generateErrorMessage(int day, String month, int year){

    }

    public String getErrorMessage(){
        return errorMessage;
    }


}