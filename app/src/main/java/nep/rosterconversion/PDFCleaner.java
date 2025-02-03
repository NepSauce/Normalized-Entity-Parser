package nep.rosterconversion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
        Map<String, String> stringMap = new HashMap<>();

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

            // Regular expression to match the time and course code(s) including section numbers
            String regex = "(\\d{1,2}:\\d{2} [APM]{2}).*?((\\w{4,6}(?:/\\w{4,6})?)\\s*\\d{4}(?:\\s*-\\s*\\d{2})?)";
            Pattern pattern = Pattern.compile(regex);

            // Use a matcher on the full text
            Matcher matcher = pattern.matcher(fullText.toString());

            // Filter and write to the output file
            while (matcher.find()) {
                String time = matcher.group(1);
                String courseCode = matcher.group(2);

                // Normalize the course code for grouping
                getCrossListings(courseCode, stringMap);
                courseCode = courseCode.replaceAll("\\s*-\\s*", "-");
                String normalizedCourseCode = normalizeCourseCode(courseCode, stringMap);
                normalizedCourseCode = normalizedCourseCode.replaceAll("\\s*-\\s*", "-");
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

                groupedWriter.write("Course Code: " + normalizedCourseCode.toUpperCase());
                groupedWriter.newLine();
                // Create a map to store the count of each time
                Map<String, Integer> timeCountMap = new HashMap<>();

                // Count the occurrences of each time in the 'times' list
                for (String time : times) {
                    timeCountMap.put(time, timeCountMap.getOrDefault(time, 0) + 1);
                }

                // Now write the time and its count to the groupedWriter
                for (Map.Entry<String, Integer> timeEntry : timeCountMap.entrySet()) {
                    String time = timeEntry.getKey();
                    int count = timeEntry.getValue();
                    groupedWriter.write(time + " (" + count + ")");
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
     * Normalizes the course code to handle cross-listed courses and section numbers.
     * For example, "PSYO/NESC 2000-01" and "NESC/PSYO 2000-01" will both be normalized to "PSYO/NESC 2000-01".
     */
    private static String normalizeCourseCode(String courseCode, Map<String, String> stringMap) {
        // Split the course code into parts (e.g., "PSYO/NESC 2000-01" -> ["PSYO/NESC", "2000-01"])
        String[] parts = courseCode.split(" ");
        if(parts[0].contains("/")){
            if(stringMap.containsValue(parts[0])){
                return courseCode.toLowerCase();
            }
            else if(stringMap.containsKey(parts[0])){
                String toReturn = stringMap.get(parts[0]) + " " + parts[1];
                return toReturn.toLowerCase();
            }
        }

        if (parts.length < 2) {
            return courseCode.toLowerCase();  // If no course number, return as is
        }

        if(stringMap.containsKey(parts[0])){
            String toReturn = stringMap.get(parts[0]) + " " + parts[1];
            return toReturn.toLowerCase();
        }

        String codePart = parts[0];  // e.g., "PSYO/NESC"
        String numberPart = parts[1];  // e.g., "2000-01"

        // Split the code part by "/" to handle cross-listed courses
        String[] codes = codePart.split("/");
        Arrays.sort(codes);  // Sort the codes alphabetically to ensure consistent grouping

        // Rebuild the normalized course code (e.g., "NESC/PSYO 2000-01" -> "PSYO/NESC 2000-01")
        String normalizedCode = String.join("/", codes) + " " + numberPart;

        return normalizedCode.toLowerCase();  // Return in lowercase for case-insensitive grouping
    }

    public static void getCrossListings(String courseCode, Map<String, String> stringMap){
        String[] parts = courseCode.split(" ");
        if(parts[0].contains("/")){
            if(!stringMap.containsKey(parts[0]) && !stringMap.containsValue(parts[0])){
                String[] crosslisted = parts[0].split("/");
                stringMap.put(crosslisted[0], parts[0]);
                stringMap.put(crosslisted[1], parts[0]);
                String reversed = crosslisted[1] + "/" + crosslisted[0];
                stringMap.put(reversed, parts[0]);
            }
        }
    }

    public static String getKeyByValue(Map<String, String> map, String value) {
        // Iterate through the map
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();  // Return the key if the value matches
            }
        }
        return null;  // Return null if no matching value is found
    }
}