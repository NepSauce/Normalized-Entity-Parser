package nep.rosterconversion;

import java.io.*;
import java.util.*;

public class PDFCleaner {
    private static final String[] METADATA_FIELDS = {
        "Duration:",
        "Permitted Materials:",
        "Exam Source:",
        "Medium:",
        "PU/DEL:",
        "Password:",
        "Notes:"
    };
    
    private static final int COURSE_CODE_WIDTH = 50;

    public static void main(String[] args) {
        String inputFilePath = "Media/output.txt";
        String outputFilePath = "Media/filtered_appointments.txt";
        String groupedFilePath = "Media/grouped_appointments.txt";
        filterAppointments(inputFilePath, outputFilePath, groupedFilePath);
    }

    public static void filterAppointments(String inputFilePath, String outputFilePath, String groupedFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter filteredWriter = new BufferedWriter(new FileWriter(outputFilePath));
             BufferedWriter groupedWriter = new BufferedWriter(new FileWriter(groupedFilePath))) {
            
            Map<String, Map<String, Integer>> courseGroups = new TreeMap<>();
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("[\\[\\]]", "");
                String[] parts = line.split("\\|");
                if (parts.length >= 5) {
                    String time = parts[4].trim();
                    String originalCourse = parts[2].trim();
                    String normalizedCourse = normalizeCrossListedCourse(originalCourse);
                    
                    filteredWriter.write(String.format("Time: %s, Course Code: %s%n", time, originalCourse));
                    
                    courseGroups
                        .computeIfAbsent(normalizedCourse, k -> new TreeMap<>())
                        .merge(time, 1, Integer::sum);
                }
            }

            for (Map.Entry<String, Map<String, Integer>> courseEntry : courseGroups.entrySet()) {
                String courseLine = String.format("Course Code: %s", courseEntry.getKey());
                groupedWriter.write(padRight(courseLine, COURSE_CODE_WIDTH) + METADATA_FIELDS[0] + "\n");
                
                boolean firstTimeEntry = true;
                for (Map.Entry<String, Integer> timeEntry : courseEntry.getValue().entrySet()) {
                    String timeLine = String.format(" %d - [location] - %s", 
                                      timeEntry.getValue(), timeEntry.getKey());
                    groupedWriter.write(padRight(timeLine, COURSE_CODE_WIDTH));
                    
                    if (firstTimeEntry) {
                        for (int i = 1; i < METADATA_FIELDS.length; i++) {
                            groupedWriter.write(METADATA_FIELDS[i] + "\n");
                            if (i < METADATA_FIELDS.length - 1) {
                                groupedWriter.write(padRight("", COURSE_CODE_WIDTH));
                            }
                        }
                        firstTimeEntry = false;
                    } else {
                        groupedWriter.write("\n");
                    }
                }
                groupedWriter.newLine();
            }

            System.out.println("Successfully generated output files.");
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String normalizeCrossListedCourse(String courseCode) {
        String[] parts = courseCode.split(" ");
        if (parts.length == 2 && parts[0].contains("/")) {
            String[] departments = parts[0].split("/");
            Arrays.sort(departments);
            return String.join("/", departments) + " " + parts[1];
        }
        return courseCode;
    }

    private static String padRight(String s, int width) {
        return String.format("%-" + width + "s", s);
    }
}