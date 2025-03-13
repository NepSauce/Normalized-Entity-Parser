package nep.entityclass;

@SuppressWarnings("unused")
public class RosterEntityDetails{
    private String INPUT_DIRECTORY;
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
        this.INPUT_DIRECTORY = directory;
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
        return INPUT_DIRECTORY;
    }

    public void setDirectory(String newDirectory){
        INPUT_DIRECTORY = newDirectory;
    }
}
