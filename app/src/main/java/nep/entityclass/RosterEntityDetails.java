package nep.entityclass;

public class RosterEntityDetails {
    private int day;
    private String month;
    private int year;
    private String location;

    public RosterEntityDetails(String location, int day, String month, int year){
        this.location = location;
        this.day = day;
        this.month = month;
        this.year = year;
    }
}
