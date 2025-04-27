package nep.rosterconversion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nep.util.CurrentTime;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFConversion {
    
    private static String newDirectoryForNormalizedObject(int year, int month, int day){
        String yearStr = String.valueOf(year);
        String monthStr = String.format("%02d", month);
        String dayStr = String.format("%02d", day);
        
        String baseFolderPath = "NormalizedEntityParser/" + yearStr + "/" + monthStr + "/" + dayStr + "/NormalizedObjects/";
        
        File folder = new File(baseFolderPath);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (created) {
                System.out.println("Created folder: " + baseFolderPath);
            }
            else {
                System.out.println("Failed to create folder: " + baseFolderPath);
            }
        }
        else {
            System.out.println("Folder already exists: " + baseFolderPath);
        }
        
        return baseFolderPath;
    }
    
    public static void generateNormalizedObject(String pdfPath, String fileName, String location){
        CurrentTime newTime = new CurrentTime();
        String currentTime = newTime.getCurrentTime();
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
                + "NormalizedObject(" + fileName + ").txt";
        
        try {
            String pdfText = convertPdfToString(pdfPath);
            String locationType = extractLocationType(pdfText);
   
            List<String> formattedLines = processRosterText(pdfText, locationType);
            Files.write(Path.of(outputTextPath), formattedLines);
        }
        catch (IOException e) {
            System.err.println("Error processing the PDF: " + e.getMessage());
        }
    }
    
    /**
     * Converts a PDF file into a plain text string.
     *
     * @param pdfPath Path to the PDF file
     * @return Extracted text from the PDF
     * @throws IOException If reading the file fails
     */
    private static String convertPdfToString(String pdfPath) throws IOException {
        try (PDDocument document = PDDocument.load(new File(pdfPath))) {
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
    private static String extractLocationType(String text) {
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
    private static List<String> processRosterText(String text, String locationType) {
        List<String> formattedLines = new ArrayList<>();
        Path removedLinesPath = Path.of("Media/removedLines.txt");
        
        try (BufferedWriter writer = Files.newBufferedWriter(removedLinesPath)) {
            List<String> completeRecords = buildCompleteRecords(text.split("\\r?\\n"), writer);
            
            for (String record : completeRecords) {
                String studentId = extractStudentId(record);
                String studentName = extractStudentName(record, studentId);
                String time = extractTime(record);
                String courseCode = extractCourseCode(record);
                String location = determineLocation(record, locationType);
                
                if (studentId != null && studentName != null && time != null && courseCode != null) {
                    formattedLines.add(String.format("[%s | %s | %s | %s | %s]",
                            studentId, studentName, courseCode, location, time));
                } else {
                    writer.write(record.trim());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing to removedLines.txt: " + e.getMessage());
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
    private static List<String> buildCompleteRecords(String[] lines, BufferedWriter removedWriter) throws IOException {
        List<String> completeRecords = new ArrayList<>();
        StringBuilder currentRecord = new StringBuilder();
        
        for (String line : lines) {
            if (shouldSkipLine(line)) {
                continue;
            }
            
            if (line.matches(".*B\\d{8}.*")) {
                if (currentRecord.length() > 0) {
                    completeRecords.add(currentRecord.toString());
                    currentRecord = new StringBuilder();
                }
            } else if (line.matches(".*B0.*")) {
                removedWriter.write(line.trim());
                removedWriter.newLine();
            }
            
            currentRecord.append(line).append(" ");
        }
        
        if (currentRecord.length() > 0) {
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
    private static boolean shouldSkipLine(String line) {
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
    private static String extractStudentId(String record) {
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
    private static String extractStudentName(String record, String studentId) {
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
    private static String extractTime(String record) {
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
    private static String determineLocation(String record, String locationType) {
        if (locationType.equals("ALTLOC")) {
            return extractAltLocLocation(record);
        } else {
            return locationType;
        }
    }
    
    /**
     * Extracts the location from a record in case of ALTLOC location type.
     *
     * @param record The record to extract location from
     * @return Cleaned location string, or placeholder if not found
     */
    private static String extractAltLocLocation(String record) {
        Pattern locationPattern = Pattern.compile("\\d{1,2}:\\d{2}\\s[AP]M\\s\\d{3}\\s([^-]+?)(?:\\s-|\\s\\d{2}\\s|$)");
        Matcher locationMatcher = locationPattern.matcher(record);
        
        if (locationMatcher.find()) {
            String location = locationMatcher.group(1).trim();
            return cleanLocation(location);
        }
        
        Pattern altPattern = Pattern.compile("\\d{1,2}:\\d{2}\\s[AP]M\\s\\d{3}\\s([A-Z].+?)(?:\\s\\(\\d+\\+?\\d*\\)|\\s-|$)");
        Matcher altMatcher = altPattern.matcher(record);
        
        if (altMatcher.find()) {
            String location = altMatcher.group(1).trim();
            return cleanLocation(location);
        }
        
        return "[location]";
    }
    
    /**
     * Cleans up the extracted location string by removing unnecessary characters.
     *
     * @param location The raw location string
     * @return The cleaned location
     */
    private static String cleanLocation(String location) {
        String retValue = location.replaceAll("\\s\\d+$", "")
                .replaceAll("[.-]", "")
                .replaceAll("COMP\\s*", "")
                .replaceAll("(?i)\\s*(BRIGHTSPACE|WP|READER|SCRIBE|brightspace|wp|reader|scribe|,)\\s*", "")
                .trim();
        
        String[] parts = retValue.split("\\s+");
        int len = parts.length;
        
        for (int i = 1; i <= len / 2; i++) {
            boolean isDuplicate = true;
            for (int j = 0; j < i; j++) {
                if (!parts[j].equals(parts[len - i + j])) {
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
