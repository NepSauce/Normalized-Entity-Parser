package nep.rosterconversion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nep.util.CurrentTime;
import nep.util.DisplayUIPopup;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFConversion {
    static StringBuilder combinedNormalizedObject = new StringBuilder();
    
    /**
     * Creates the directory for storing removed objects if it does not already exist.
     *
     * @return The path to the removed objects' directory.
     */
    private static String newDirectoryForRemovedObject(){
        String baseFolderPath = "NormalizedEntityParser/RemovedObjects";
        File folder = new File(baseFolderPath);
        
        if (!folder.exists()){
            boolean directoryCreated = folder.mkdirs();
            
            if (directoryCreated){
                System.out.println("Created folder: " + baseFolderPath);
            }
            else{
                System.out.println("Failed to create folder: " + baseFolderPath);
            }
        }
        else{
            System.out.println("Folder already exists: " + baseFolderPath);
        }
        
        return baseFolderPath;
    }
    
    /**
     * Creates the directory for storing combined normalized objects if it does not already exist.
     *
     * @return The path to the combined objects' directory.
     */
    private static String newDirectoryForCombinedObject(){
        String baseFolderPath = "NormalizedEntityParser/CombinedObjects";
        File folder = new File(baseFolderPath);
        
        if (!folder.exists()){
            boolean directoryCreated = folder.mkdirs();
            
            if (directoryCreated){
                System.out.println("Created folder: " + baseFolderPath);
            }
            else{
                System.out.println("Failed to create folder: " + baseFolderPath);
            }
        }
        else{
            System.out.println("Folder already exists: " + baseFolderPath);
        }
        
        return baseFolderPath;
    }
    
    /**
     * Creates a directory structure for normalized objects based on the current year, month, and day.
     *
     * @param year  The current year.
     * @param month The current month.
     * @param day   The current day.
     * @return The path to the normalized objects' directory.
     */
    private static String newDirectoryForNormalizedObject(int year, int month, int day){
        String yearStr = String.valueOf(year);
        String monthStr = String.format("%02d", month);
        String dayStr = String.format("%02d", day);
        
        String baseFolderPath = "NormalizedEntityParser/" + yearStr + "/" + monthStr + "/" + dayStr + "/NormalizedObjects/";
        File folder = new File(baseFolderPath);
        
        if (!folder.exists()){
            boolean directoryCreated = folder.mkdirs();
            
            if (directoryCreated){
                System.out.println("Created folder: " + baseFolderPath);
            }
            else{
                System.out.println("Failed to create folder: " + baseFolderPath);
            }
        }
        else{
            System.out.println("Folder already exists: " + baseFolderPath);
        }
        
        return baseFolderPath;
    }
    
    /**
     * Generates a file that contains all successfully parsed and normalized entries combined into one.
     * The file is saved under the CombinedObjects directory with a timestamp.
     */
    public static void generateCombinedObject(){
        CurrentTime newTime = new CurrentTime();
        String currentTime = newTime.getCurrentTime();
        String safeTime = currentTime.replace(":", "-");
        
        String outputTextPath = "NormalizedEntityParser/"
                + "CombinedObjects/"
                + "CombinedObject(" + safeTime + ").txt";
        
        String combinedTextPath = newDirectoryForCombinedObject();
        
        try (FileWriter writer = new FileWriter(outputTextPath)){
            writer.write(combinedNormalizedObject.toString());
        }
        catch (IOException e){
            System.err.println("Error Making Combined Normalized Objects: " + e.getMessage());
        }
    }
    
    /**
     * Processes a PDF file to extract student roster data, normalizes the entries,
     * writes them into a dated folder, and stores invalid entries separately.
     *
     * @param pdfPath  Path to the input PDF file.
     * @param fileName Name of the input file, used in naming the output.
     * @param location Exam or appointment location information.
     */
    public static void generateNormalizedObject(String pdfPath, String fileName, String location){
        CurrentTime newTime = new CurrentTime();
        String currentTime = newTime.getCurrentTime();
        String safeTime = currentTime.replace(":", "-");
        int year = newTime.getCurrentYear();
        int month = newTime.getCurrentMonth();
        int day = newTime.getCurrentDay();
        
        String monthStr = String.format("%02d", month);
        String dayStr = String.format("%02d", day);
        
        
        String folderPath = newDirectoryForNormalizedObject(year, month, day);
        
        String outputTextPath = "NormalizedEntityParser/"
                + year + "/"
                + monthStr + "/"
                + dayStr + "/"
                + "NormalizedObjects/"
                + "NormalizedObject-"+ fileName + "(" + safeTime + ").txt";
        
        try{
            String pdfText = convertPdfToString(pdfPath);
            String locationType = extractLocationType(pdfText);
   
            List<String> formattedLines = processRosterText(pdfText, locationType);
            Files.write(Path.of(outputTextPath), formattedLines);
        }
        catch (IOException e){
            System.err.println("Error processing the PDF: " + e.getMessage());
        }
    }
    
    /**
     * Clears the contents of the combined normalized object buffer.
     */
    public static void emptyCombinedNormalizedObjectContents(){
        combinedNormalizedObject.setLength(0);
    }
    
    /**
     * Deletes all files in the RemovedObjects directory, used for storing invalid/removed entries.
     */
    public static void deleteRemovedObjectFile(){
        String outputTextPath = "NormalizedEntityParser/"
                + "RemovedObjects/";
        
        try{
            Files.walk(Paths.get(outputTextPath))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            System.out.println("Old removed object files deleted successfully.");
        }
        catch (IOException e){
            System.err.println("Error cleaning up old removed object files: " + e.getMessage());
        }
    }
    
    /**
     * Deletes all files in the CombinedObjects directory, which stores combined normalized entries.
     */
    public static void deleteCombinedObjectFile(){
        String outputTextPath = "NormalizedEntityParser/"
                + "CombinedObjects/";
        
        try{
            Files.walk(Paths.get(outputTextPath))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            System.out.println("Old combined object files deleted successfully.");
        }
        catch (IOException e){
            System.err.println("Error cleaning up old combined object files: " + e.getMessage());
        }
    }
    
    /**
     * Deletes all files and subdirectories inside the given folder path.
     *
     * @param folderPath The path to the folder to be cleaned.
     */
    public static void deleteFilesInFolder(String folderPath){
        try{
            Files.walk(Paths.get(folderPath))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            System.out.println("Old files deleted successfully.");
        }
        catch (IOException e){
            System.err.println("Error cleaning up old files: " + e.getMessage());
        }
    }
 
    /**
     * Converts a PDF file into a plain text string.
     *
     * @param pdfPath Path to the PDF file
     * @return Extracted text from the PDF
     * @throws IOException If reading the file fails
     */
    private static String convertPdfToString(String pdfPath) throws IOException{
        try (PDDocument document = PDDocument.load(new File(pdfPath))){
            if (document.isEncrypted()) {
                throw new IOException("The PDF is encrypted and cannot be processed.");
            }
            PDFTextStripper textStripper = new PDFTextStripper();
            return textStripper.getText(document);
        }
    }
    
    /**
     * Extracts the location type from the PDF text.
     *
     * @param text The full PDF content as text
     * @return Extracted location type or "UNKNOWN" if not found
     */
    private static String extractLocationType(String text){
        Matcher matcher = Pattern.compile("Location: EXAM-SCHED(?:ULING)?-([A-Z0-9]+)").matcher(text);
        return matcher.find() ? matcher.group(1) : "UNKNOWN";
    }
    
    /**
     * Processes the full roster text and extracts relevant student information in a structured format.
     *
     * @param text The full PDF content as text
     * @param locationType The location type extracted from the document
     * @return A list of formatted lines with student information
     */
    private static List<String> processRosterText(String text, String locationType){
        CurrentTime newTime = new CurrentTime();
        String currentTime = newTime.getCurrentTime();
        String safeTime = currentTime.replace(":", "-");
        
        newDirectoryForRemovedObject();
        
        List<String> formattedLines = new ArrayList<>();
        Path removedLinesPath = Path.of("NormalizedEntityParser/RemovedObjects/RemovedObject().txt");
        
        int removedCount = 0;
        
        try (BufferedWriter writer = Files.newBufferedWriter(removedLinesPath,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)){
            List<String> completeRecords = buildCompleteRecords(text.split("\\r?\\n"), writer);
            
            for (String record : completeRecords){
                String studentId = extractStudentId(record);
                String studentName = extractStudentName(record, studentId);
                String time = extractTime(record);
                String courseCode = extractCourseCode(record);
                String location;
                
                if (courseCode == null){
                    location = null;
                }
                else{
                    location = determineLocation(record, locationType);
                }
                
                if (studentId != null && studentName != null && time != null && courseCode != null && location != null) {
                    String format = String.format("[%s | %s | %s | %s | %s]",
                            studentId, studentName, courseCode, location, time);
                    formattedLines.add(format);
                    combinedNormalizedObject.append(format).append("\n");
                }
                else{
                    String removedFormat = String.format("[DalID: %s | Name: %s | Code: %s | Location: %s | Time: %s]",
                            studentId, studentName, courseCode, location, time);
                    writer.write(removedFormat);
                    writer.newLine();
                    removedCount++;
                }
            }
            
            if (removedCount > 0){
                new DisplayUIPopup("Removed Entries Saved",
                        removedCount + " Removed Line" + (removedCount == 1 ? " Was" : "s Were") + " Saved to RemovedObject.txt.", 1001)
                        .showInfoPopup();
            }
            else{
                new DisplayUIPopup("Success", "CombinedObject file was successfully generated", 1002).showInfoPopup();
            }
        }
        catch (IOException e){
            System.err.println("Error writing to RemovedObject.txt: " + e.getMessage());
        }
        
        return formattedLines;
    }
    
    /**
     * Builds complete student records from raw PDF lines.
     *
     * @param lines Array of text lines from the PDF
     * @param removedWriter Writer to log removed/invalid lines
     * @return A list of complete student records
     * @throws IOException If writing to the removed lines file fails
     */
    private static List<String> buildCompleteRecords(String[] lines, BufferedWriter removedWriter) throws IOException{
        List<String> completeRecords = new ArrayList<>();
        StringBuilder currentRecord = new StringBuilder();
        
        for (String line : lines){
            if (shouldSkipLine(line)){
                continue;
            }
            
            if (line.matches(".*B\\d{8}.*")){
                if (currentRecord.length() > 0) {
                    completeRecords.add(currentRecord.toString());
                    currentRecord = new StringBuilder();
                }
            }
            else if (line.matches(".*B0.*")){
                removedWriter.write(line.trim());
                removedWriter.newLine();
            }
            
            currentRecord.append(line).append(" ");
        }
        
        if (currentRecord.length() > 0){
            completeRecords.add(currentRecord.toString());
        }
        
        return completeRecords;
    }
    
    /**
     * Determines whether a line should be skipped from processing.
     *
     * @param line The line to evaluate
     * @return True if the line should be skipped; otherwise, false
     */
    private static boolean shouldSkipLine(String line){
        return line.contains("Phone NumberStudent ID") ||
                line.contains("All Appointments") ||
                line.contains("Printed:") ||
                line.contains("Location:") ||
                line.contains("Sorted by") ||
                line.contains("Do Not Call") ||
                line.trim().isEmpty();
    }
    
    /**
     * Extracts the student ID from a record.
     *
     * @param record The record to extract from
     * @return The student ID if found; otherwise, null
     */
    private static String extractStudentId(String record){
        Matcher idMatcher = Pattern.compile("B\\d{8}").matcher(record);
        return idMatcher.find() ? idMatcher.group() : null;
    }
    
    /**
     * Extracts the student name from a record.
     *
     * @param record The full record text
     * @param studentId The extracted student ID
     * @return The cleaned student name
     */
    private static String extractStudentName(String record, String studentId){
        int idPosition = record.indexOf(studentId);
        String namePart = record.substring(0, idPosition).trim();
        return namePart.replaceAll("[^a-zA-Z, ]", "").trim();
    }
    
    /**
     * Extracts the appointment time from a record.
     *
     * @param record The record to extract from
     * @return The time string if found; otherwise, null
     */
    private static String extractTime(String record){
        Matcher timeMatcher = Pattern.compile("\\d{1,2}:\\d{2}\\s[AP]M").matcher(record);
        return timeMatcher.find() ? timeMatcher.group() : null;
    }
    
    /**
     * Extracts the course code from a record.
     *
     * @param record The record to extract from
     * @return The course code if found; otherwise, null
     */
    private static String extractCourseCode(String record) {
        Matcher courseMatcher = Pattern.compile(
                "([A-Za-z]{2,6}/)?([A-Za-z]{2,6})[\\s-](\\d{4,5}(?:[\\s.-]\\d{1,2})?)"
        ).matcher(record);
        
        List<String> potentialCodes = new ArrayList<>();
        
        while (courseMatcher.find()) {
            String fullDept = courseMatcher.group(1) != null ? courseMatcher.group(1) : "";
            String mainDept = courseMatcher.group(2);
            String rawCode = courseMatcher.group(3).replaceAll("[\\s.-]", " ").trim();
            
            String[] parts = rawCode.split(" ");
            String courseNumber = parts[0];
            
            if (courseNumber.length() != 4){
                return null;
            }
            String section = (parts.length > 1) ? parts[1] : null;
            
            if (section != null && section.matches("0[1-9]")) {
                rawCode = courseNumber + " " + section;
            } else {
                rawCode = courseNumber;
            }
            
            String potentialCode = (fullDept + mainDept + " " + rawCode).toUpperCase();
            potentialCode = potentialCode.replaceAll("[\\s.-]", " ");
            
            if (!potentialCode.matches(".*(COMP|ROWE|MONA|DUNN|WELDON|MCCAIN|MCAIN|LOCATION|DALHOUSIE).*")) {
                potentialCodes.add(potentialCode.trim());
            }
        }

        if (potentialCodes.size() >= 2) {
            return null;
        }
        
        if (!potentialCodes.isEmpty()) {
            return potentialCodes.get(0);
        }
        
        Matcher deptMatcher = Pattern.compile(
                "\\s-\\s([A-Z]{2,6})\\s\\d{4,5}|" +
                        "\\s-\\s([A-Z]{2,6})$"
        ).matcher(record);
        
        if (deptMatcher.find()) {
            String dept = deptMatcher.group(1) != null ? deptMatcher.group(1) : deptMatcher.group(2);
            
            if (dept != null && !dept.isEmpty() &&
                    !dept.matches("(COMP|ROWE|MONA|DUNN|WELDON|MCCAIN|MCAIN|LOCATION|DALHOUSIE)")) {
                return dept;
            }
        }
        
        return null;
    }
    /**
     * Determines the appropriate location string based on the location type.
     *
     * @param record The record containing location details
     * @param locationType The extracted location type
     * @return The determined location name
     */
    private static String determineLocation(String record, String locationType){
        if (locationType.equals("ALTLOC")) {
            return extractAltLocLocation(record);
        }
        else{
            return locationType;
        }
    }
    
    /**
     * Extracts the location from a record in case of ALTLOC location type.
     *
     * @param record The record to extract location from
     * @return Cleaned location string, or placeholder if not found
     */
    private static String extractAltLocLocation(String record){
        Pattern locationPattern = Pattern.compile("\\d{1,2}:\\d{2}\\s[AP]M\\s\\d{3}\\s([^-]+?)(?:\\s-|\\s\\d{2}\\s|$)");
        Matcher locationMatcher = locationPattern.matcher(record);
        
        if (locationMatcher.find()){
            String location = locationMatcher.group(1).trim();
            return cleanLocation(location);
        }
        Pattern altPattern = Pattern.compile("\\d{1,2}:\\d{2}\\s[AP]M\\s\\d{3}\\s([A-Z].+?)(?:\\s\\(\\d+\\+?\\d*\\)|\\s-|$)");
        Matcher altMatcher = altPattern.matcher(record);
        
        if (altMatcher.find()) {
            String location = altMatcher.group(1).trim();
            return cleanLocation(location);
        }
        
        return null;
    }
    
    /**
     * Cleans up the extracted location string by removing unnecessary characters.
     *
     * @param location The raw location string
     * @return The cleaned location
     */
    private static String cleanLocation(String location){
        String retValue = location.replaceAll("\\s\\d+$", "")
                .replaceAll("[.-]", "")
                .replaceAll("COMP\\s*", "")
                .replaceAll("(?i)\\s*(BRIGHTSPACE|WP|READER|SCRIBE|brightspace|wp|reader|scribe|,)\\s*", "")
                .trim();
        
        String[] parts = retValue.split("\\s+");
        int len = parts.length;
        
        for (int i = 1; i <= len / 2; i++){
            boolean isDuplicate = true;
            
            for (int j = 0; j < i; j++){
                if (!parts[j].equals(parts[len - i + j])){
                    isDuplicate = false;
                    break;
                }
            }
            if (isDuplicate) {
                retValue = String.join(" ", Arrays.copyOfRange(parts, 0, len - i));
                break;
            }
        }
        
        return retValue;
    }
}
