package nep.rosterconversion;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFConversion {

    public static void main(String[] args) {
        String pdfPath = "Media/rosterG28.pdf";
        String outputTextPath = "Media/output.txt";

        try {
            String pdfText = convertPdfToString(pdfPath);
            List<String> formattedLines = processRosterText(pdfText);
            Files.write(Path.of(outputTextPath), formattedLines);
        } catch (IOException e) {
            System.err.println("Error processing the PDF: " + e.getMessage());
        }
    }

    /**
     * Converts a PDF file to a string containing its text content.
     *
     * @param pdfPath The path to the PDF file
     * @return The extracted text content as a string
     * @throws IOException If there's an error reading the PDF file
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
     * Processes the roster text and formats it into structured records.
     *
     * @param text The raw text extracted from the PDF
     * @return A list of formatted record strings
     */
    private static List<String> processRosterText(String text) {
        List<String> formattedLines = new ArrayList<>();
        List<String> completeRecords = buildCompleteRecords(text.split("\\r?\\n"));

        for (String record : completeRecords) {
            String studentId = extractStudentId(record);
            String studentName = extractStudentName(record, studentId);
            String time = extractTime(record);
            String courseCode = extractCourseCode(record);
            String location = extractLocation(record);

            if (studentId != null && studentName != null && time != null && courseCode != null) {
                formattedLines.add(String.format("[%s | %s | %s | %s | %s]", 
                    studentId, studentName, courseCode, location, time));
            }
        }

        return formattedLines;
    }

    /**
     * Builds complete records by joining lines that belong together.
     *
     * @param lines The individual lines from the PDF text
     * @return A list of complete records as strings
     */
    private static List<String> buildCompleteRecords(String[] lines) {
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
            }
            currentRecord.append(line).append(" ");
        }

        if (currentRecord.length() > 0) {
            completeRecords.add(currentRecord.toString());
        }

        return completeRecords;
    }

    /**
     * Determines if a line should be skipped during processing.
     *
     * @param line The line to check
     * @return true if the line should be skipped, false otherwise
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
     * @param record The complete record string
     * @return The student ID or null if not found
     */
    private static String extractStudentId(String record) {
        Matcher idMatcher = Pattern.compile("B\\d{8}").matcher(record);
        return idMatcher.find() ? idMatcher.group() : null;
    }

    /**
     * Extracts and cleans the student name from a record.
     *
     * @param record The complete record string
     * @param studentId The student ID to help locate the name
     * @return The cleaned student name
     */
    private static String extractStudentName(String record, String studentId) {
        int idPosition = record.indexOf(studentId);
        String namePart = record.substring(0, idPosition).trim();
        return namePart.replaceAll("[^a-zA-Z, ]", "").trim();
    }

    /**
     * Extracts the time from a record.
     *
     * @param record The complete record string
     * @return The time string or null if not found
     */
    private static String extractTime(String record) {
        Matcher timeMatcher = Pattern.compile("\\d{1,2}:\\d{2}\\s[AP]M").matcher(record);
        return timeMatcher.find() ? timeMatcher.group() : null;
    }

    /**
     * Extracts the course code from a record.
     *
     * @param record The complete record string
     * @return The course code or null if not found
     */
    private static String extractCourseCode(String record) {
        Matcher courseMatcher = Pattern.compile("(?<=^|\\s|\\n)([A-Za-z]{3,4}(?:/[A-Za-z]{3,4})?[- ]\\d{4}(?:-\\d{2})?)")
                                      .matcher(record);

        List<String> potentialCodes = new ArrayList<>();
        while (courseMatcher.find()) {
            String potentialCode = courseMatcher.group().toUpperCase();
            if (!potentialCode.startsWith("COMP")) {
                potentialCodes.add(potentialCode);
            }
        }

        return potentialCodes.isEmpty() ? null : potentialCodes.get(0).replace('-', ' ');
    }

    /**
     * Extracts and cleans the location from a record.
     *
     * @param record The complete record string
     * @return The cleaned location or "[location]" if not found
     */
    private static String extractLocation(String record) {
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
     * Cleans and normalizes a location string.
     *
     * @param location The raw location string
     * @return The cleaned location string
     */
    private static String cleanLocation(String location) {
        String cleaned = location.replaceAll("\\s\\d+$", "")
                               .replaceAll("[.-]", "")
                               .replaceAll("COMP\\s*", "")
                               .trim();
        
        return (cleaned.startsWith("ER") || cleaned.startsWith("G2")) ? "[location]" : cleaned;
    }
}