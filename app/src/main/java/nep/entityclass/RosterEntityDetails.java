package nep.entityclass;

/**
 * Represents details of a Roster Entity, including file name, input directory,
 * and associated date and location metadata.
 */
@SuppressWarnings("unused")
public class RosterEntityDetails{
    private final String fileName;
    private String inputDirectory;
    private final String LOCATION;
    private final int DAY;
    private final String MONTH;
    private final int YEAR;
    
    /**
     * Constructs a new {@code RosterEntityDetails} object with the specified parameters.
     *
     * @param fileName     The name of the file.
     * @param directory    The input directory path.
     * @param location     The location associated with the roster.
     * @param day          The day component of the roster date.
     * @param month        The month component of the roster date.
     * @param year         The year component of the roster date.
     */
    public RosterEntityDetails(String fileName, String directory, String location, int day, String month, int year){
        this.fileName = fileName;
        this.inputDirectory = directory;
        this.LOCATION = location;
        this.DAY = day;
        this.MONTH = month;
        this.YEAR = year;
    }
    
    /**
     * Returns the location associated with the roster.
     *
     * @return The location as a {@code String}.
     */
    public String getLocation(){
        return LOCATION;
    }
    
    /**
     * Returns the day component of the roster date.
     *
     * @return The day as an {@code int}.
     */
    
    public int getDay(){
        return DAY;
    }
    
    /**
     * Returns the month component of the roster date.
     *
     * @return The month as a {@code String}.
     */
    public String getMonth(){
        return MONTH;
    }
    
    /**
     * Returns the year component of the roster date.
     *
     * @return The year as an {@code int}.
     */
    public int getYear(){
        return YEAR;
    }
    
    /**
     * Returns the input directory where the roster file is located.
     *
     * @return The input directory as a {@code String}.
     */
    public String getDirectory(){
        return inputDirectory;
    }
    
    /**
     * Returns the file name of the roster.
     *
     * @return The file name as a {@code String}.
     */
    public String getFileName(){
        return fileName;
    }
    
    /**
     * Sets a new input directory for the roster file.
     *
     * @param newDirectory The new directory path to set.
     */
    public void setDirectory(String newDirectory){
        inputDirectory = newDirectory;
    }
    
    /**
     * Returns a string representation of this Roster Entity,
     * concatenating the directory, location, year, month, and day.
     *
     * @return A formatted string representing the roster details.
     */
    @Override
    public String toString(){
        return this.inputDirectory + this.LOCATION + this.YEAR + this.MONTH + this.DAY;
    }
}
