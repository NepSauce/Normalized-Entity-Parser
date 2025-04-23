package nep.entityclass;

@SuppressWarnings("unused")
public class RosterEntityDetails{
    private String inputDirectory;
    private final String LOCATION;
    private final int DAY;
    private final String MONTH;
    private final int YEAR;

    public RosterEntityDetails(String location, int day, String month, int year){
        this.LOCATION = location;
        this.DAY = day;
        this.MONTH = month;
        this.YEAR = year;
    }

    public RosterEntityDetails(String directory, String location, int day, String month, int year){
        this.inputDirectory = directory;
        this.LOCATION = location;
        this.DAY = day;
        this.MONTH = month;
        this.YEAR = year;
    }

    public String getLocation(){
        return LOCATION;
    }

    public int getDay(){
        return DAY;
    }

    public String getMonth(){
        return MONTH;
    }

    public int getYear(){
        return YEAR;
    }

    public String getDirectory(){
        return inputDirectory;
    }

    public void setDirectory(String newDirectory){
        inputDirectory = newDirectory;
    }

    @Override
    public String toString(){
        return this.inputDirectory + this.LOCATION + this.YEAR + this.MONTH + this.DAY;
    }
}
