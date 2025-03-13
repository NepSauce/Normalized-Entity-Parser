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

    public String getLOCATION(){
        return LOCATION;
    }

    public int getDAY(){
        return DAY;
    }

    public String getMONTH(){
        return MONTH;
    }

    public int getYEAR(){
        return YEAR;
    }

    public String getDirectory(){
        return inputDirectory;
    }

    public void setDirectory(String newDirectory){
        inputDirectory = newDirectory;
    }
}
