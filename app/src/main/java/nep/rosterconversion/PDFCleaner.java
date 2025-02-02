package nep.rosterconversion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDFCleaner {
    public static void main(String[] args) {
        String inputFilePath = "Media/output.txt";  // Path to your input text file
        String outputFilePath = "Media/filtered_appointments.txt";  // Path to your output text file
        String groupedFilePath = "Media/grouped_appointments.txt";  // Path to the grouped output file
        filterAppointments(inputFilePath, outputFilePath, groupedFilePath);
    }

    public static void filterAppointments(String inputFilePath, String outputFilePath, String groupedFilePath) {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        BufferedWriter groupedWriter = null;
        StringBuilder fullText = new StringBuilder();  // To hold all the lines as a single string

        // Map to store grouped times by normalized course code
        Map<String, List<String>> courseCodeMap = new HashMap<>();

        try {
            reader = new BufferedReader(new FileReader(inputFilePath));
            String line;

            // Read all lines and append them into one single string
            while ((line = reader.readLine()) != null) {
                // Remove the line containing the "Printed: ..." timestamp
                line = line.replaceAll("Printed:.*", "");

                // Remove the line containing the "Location: ..." information
                line = line.replaceAll("Location:.*", "");

                // Append the cleaned line
                fullText.append(line).append(" ");  // Add space between lines
            }

            writer = new BufferedWriter(new FileWriter(outputFilePath));
            groupedWriter = new BufferedWriter(new FileWriter(groupedFilePath));

            // Regular expression to match the time and course code(s)
            String regex = "(\\d{1,2}:\\d{2} [APM]{2}).*?((\\w{4,6}(?:/\\w{4,6})?) \\d{4})";
            Pattern pattern = Pattern.compile(regex);

            // Use a matcher on the full text
            Matcher matcher = pattern.matcher(fullText.toString());

            // Filter and write to the output file
            while (matcher.find()) {
                String time = matcher.group(1);
                String courseCode = matcher.group(2);

                // Normalize the course code for grouping
                String normalizedCourseCode = normalizeCourseCode(courseCode);

                // Write to the filtered_appointments.txt file
                writer.write("Time: " + time + ", Course Code: " + courseCode);
                writer.newLine();

                // Group times by normalized course code
                courseCodeMap.computeIfAbsent(normalizedCourseCode, k -> new ArrayList<>()).add(time);
            }

            // Write grouped data to the grouped_appointments.txt file
            for (Map.Entry<String, List<String>> entry : courseCodeMap.entrySet()) {
                String normalizedCourseCode = entry.getKey();
                List<String> times = entry.getValue();

                // Sort the times in ascending order
                times.sort((time1, time2) -> {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("h:mm a");

                        // Clean the time strings before parsing
                        time1 = time1.trim().replace('\u00A0', ' '); // Trim and replace non-breaking spaces
                        time2 = time2.trim().replace('\u00A0', ' '); // Trim and replace non-breaking spaces

                        Date date1 = format.parse(time1);
                        Date date2 = format.parse(time2);
                        System.err.println(date1);
                        return date1.compareTo(date2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                });

                groupedWriter.write("Course Code: " + normalizedCourseCode);
                groupedWriter.newLine();
                for (String time : times) {
                    groupedWriter.write("  - " + time);
                    groupedWriter.newLine();
                }
                groupedWriter.newLine();  // Add a blank line between course codes
            }

            System.out.println("Filtered appointments have been written to " + outputFilePath);
            System.out.println("Grouped appointments have been written to " + groupedFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
                if (groupedWriter != null) {
                    groupedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Normalizes the course code to handle cross-listed courses.
     * For example, "PSYO/NESC 2000" and "NESC/PSYO 2000" will both be normalized to "PSYO/NESC 2000".
     */
    private static String normalizeCourseCode(String courseCode) {
        // Split the course code into parts (e.g., "PSYO/NESC 2000" -> ["PSYO/NESC", "2000"])
        String[] parts = courseCode.split(" ");
        if (parts.length < 2) {
            return courseCode.toLowerCase();  // If no course number, return as is
        }

        String codePart = parts[0];  // e.g., "PSYO/NESC"
        String numberPart = parts[1];  // e.g., "2000"

        // Split the code part by "/" to handle cross-listed courses
        String[] codes = codePart.split("/");
        Arrays.sort(codes);  // Sort the codes alphabetically to ensure consistent grouping

        // Rebuild the normalized course code (e.g., "NESC/PSYO 2000" -> "PSYO/NESC 2000")
        String normalizedCode = String.join("/", codes) + " " + numberPart;

        return normalizedCode.toLowerCase();  // Return in lowercase for case-insensitive grouping
    }
}