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
        String pdfPath = "Media/rosterG28.pdf"; //test using one of the following: rosterMAH.pdf, rosterG28.pdf, rosterALTLOC.pdf
        String outputTextPath = "Media/output.txt";

        try {
            try (PDDocument document = PDDocument.load(new File(pdfPath))) {
                if (!document.isEncrypted()) {
                    PDFTextStripper textStripper = new PDFTextStripper();
                    String text = textStripper.getText(document);
                    
                    List<String> formattedLines = processRosterText(text);
                    
                    // Write the formatted lines to the output file
                    Files.write(Path.of(outputTextPath), formattedLines);
                } else {
                    System.out.println("The PDF is encrypted and cannot be processed.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error processing the PDF: " + e.getMessage());
        }
    }

    private static List<String> processRosterText(String text) {
        List<String> formattedLines = new ArrayList<>();
        String[] lines = text.split("\\r?\\n");
        
        // Reconstruct complete records by joining lines that belong together
        List<String> completeRecords = new ArrayList<>();
        StringBuilder currentRecord = new StringBuilder();
        
        for (String line : lines) {
            // Skip header/footer lines and empty lines
            if (line.contains("Phone NumberStudent ID") || 
                line.contains("All Appointments") || 
                line.contains("Printed:") || 
                line.contains("Location:") || 
                line.contains("Sorted by") || 
                line.contains("Do Not Call") ||
                line.trim().isEmpty()) {
                continue;
            }
            
            // New record
            if (line.matches(".*B\\d{8}.*")) {
                if (currentRecord.length() > 0) {
                    completeRecords.add(currentRecord.toString());
                    currentRecord = new StringBuilder();
                }
            }
            currentRecord.append(line).append(" ");
        }
        // Add the last record
        if (currentRecord.length() > 0) {
            completeRecords.add(currentRecord.toString());
        }
        
        // Process each complete record
        for (String record : completeRecords) {
            // Clean up extra spaces
            record = record.replaceAll("\\s+", " ").trim();
            
            // Extract Student ID
            Matcher idMatcher = Pattern.compile("B\\d{8}").matcher(record);
            if (!idMatcher.find()) continue;
            String studentId = idMatcher.group();
            
            // Extract Student Name
            String namePart = record.substring(0, idMatcher.start()).trim();
            String studentName = namePart.replaceAll("[^a-zA-Z, ]", "").trim();
            
            // Extract Time (format like 8:30 AM)
            Matcher timeMatcher = Pattern.compile("\\d{1,2}:\\d{2}\\s[AP]M").matcher(record);
            if (!timeMatcher.find()) continue;
            String time = timeMatcher.group();
            
            // Extract Course Code
            String courseCode = "";
            Matcher courseMatcher = Pattern.compile("(?<=^|\\s|\\n)([A-Za-z]{3,4}(?:/[A-Za-z]{3,4})?[- ]\\d{4}(?:-\\d{2})?)")
                                        .matcher(record);

            // Find all potential course codes
            List<String> potentialCodes = new ArrayList<>();
            while (courseMatcher.find()) {
                String potentialCode = courseMatcher.group().toUpperCase();
                // Skip "COMP" and keep searching
                if (!potentialCode.startsWith("COMP")) {
                    potentialCodes.add(potentialCode);
                }
            }

            // Use the first valid course code found (if any)
            if (!potentialCodes.isEmpty()) {
                courseCode = potentialCodes.get(0);
                // Normalize the separator to space if it's a hyphen
                courseCode = courseCode.replace('-', ' ');
            } else {
                System.err.println("Could not find valid course code in record: " + record);
                continue;
            }
            
            String location = "[location]";
            // Pattern to match the location format: 3 digits, space, location text, then space and either hyphen or 2 digits
            Pattern locationPattern = Pattern.compile("\\d{1,2}:\\d{2}\\s[AP]M\\s\\d{3}\\s([^-]+?)(?:\\s-|\\s\\d{2}\\s|$)");
            Matcher locationMatcher = locationPattern.matcher(record);

            if (locationMatcher.find()) {
                location = locationMatcher.group(1).trim();
            } else {
                // Alternative pattern for cases like "308 WELDON W (10)"
                Pattern altPattern = Pattern.compile("\\d{1,2}:\\d{2}\\s[AP]M\\s\\d{3}\\s([A-Z].+?)(?:\\s\\(\\d+\\+?\\d*\\)|\\s-|$)");
                Matcher altMatcher = altPattern.matcher(record);
                if (altMatcher.find()) {
                    location = altMatcher.group(1).trim();
                }
            }

            // Clean up the location
            location = location.replaceAll("\\s\\d+$", "")
                                .replaceAll("[.-]", "")
                                .replaceAll("COMP\\s*", "")
                                .trim();
            if (location.startsWith("ER") || location.startsWith("G2")) {
                location = "[location]";
            }

            // Format the line
            formattedLines.add(String.format("[%s | %s | %s | %s | %s]", 
                studentId, 
                studentName, 
                courseCode, 
                location,
                time));
        }
        
        return formattedLines;
    }
}