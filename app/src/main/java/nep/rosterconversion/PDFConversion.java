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
        String pdfPath = "Media/rosterSEXTON.pdf";
        String outputTextPath = "Media/output.txt";

        try {
            String pdfText = convertPdfToString(pdfPath);
            String locationType = extractLocationType(pdfText);
            List<String> formattedLines = processRosterText(pdfText, locationType);
            Files.write(Path.of(outputTextPath), formattedLines);
        } catch (IOException e) {
            System.err.println("Error processing the PDF: " + e.getMessage());
        }
    }

    private static String convertPdfToString(String pdfPath) throws IOException {
        try (PDDocument document = PDDocument.load(new File(pdfPath))) {
            if (document.isEncrypted()) {
                throw new IOException("The PDF is encrypted and cannot be processed.");
            }
            PDFTextStripper textStripper = new PDFTextStripper();
            return textStripper.getText(document);
        }
    }

    private static String extractLocationType(String text) {
        Matcher matcher = Pattern.compile("Location: EXAM-SCHED(?:ULING)?-([A-Z0-9]+)").matcher(text);
        return matcher.find() ? matcher.group(1) : "UNKNOWN";
    }

    private static List<String> processRosterText(String text, String locationType) {
        List<String> formattedLines = new ArrayList<>();
        List<String> completeRecords = buildCompleteRecords(text.split("\\r?\\n"));

        for (String record : completeRecords) {
            String studentId = extractStudentId(record);
            String studentName = extractStudentName(record, studentId);
            String time = extractTime(record);
            String courseCode = extractCourseCode(record);
            String location = determineLocation(record, locationType);

            if (studentId != null && studentName != null && time != null && courseCode != null) {
                formattedLines.add(String.format("[%s | %s | %s | %s | %s]", 
                    studentId, studentName, courseCode, location, time));
            }
        }

        return formattedLines;
    }

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

    private static boolean shouldSkipLine(String line) {
        return line.contains("Phone NumberStudent ID") || 
               line.contains("All Appointments") || 
               line.contains("Printed:") || 
               line.contains("Location:") || 
               line.contains("Sorted by") || 
               line.contains("Do Not Call") ||
               line.trim().isEmpty();
    }

    private static String extractStudentId(String record) {
        Matcher idMatcher = Pattern.compile("B\\d{8}").matcher(record);
        return idMatcher.find() ? idMatcher.group() : null;
    }

    private static String extractStudentName(String record, String studentId) {
        int idPosition = record.indexOf(studentId);
        String namePart = record.substring(0, idPosition).trim();
        return namePart.replaceAll("[^a-zA-Z, ]", "").trim();
    }

    private static String extractTime(String record) {
        Matcher timeMatcher = Pattern.compile("\\d{1,2}:\\d{2}\\s[AP]M").matcher(record);
        return timeMatcher.find() ? timeMatcher.group() : null;
    }

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

    private static String determineLocation(String record, String locationType) {
        if (locationType.equals("ALTLOC")) {
            return extractAltLocLocation(record);
        } else {
            return locationType;
        }
    }

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

    private static String cleanLocation(String location) {
        return location.replaceAll("\\s\\d+$", "")
                     .replaceAll("[.-]", "")
                     .replaceAll("COMP\\s*", "")
                     .trim();
    }
}