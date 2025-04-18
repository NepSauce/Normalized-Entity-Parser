package nep.rosterconversion;

import java.io.*;
import java.util.*;

public class PDFCleaner {
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
            
            Map<String, Map<String, Integer>> courseGroups = new TreeMap<>(); // Course -> (Time -> Count)
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("[\\[\\]]", "");
                String[] parts = line.split("\\|");
                if (parts.length >= 5) {
                    String time = parts[4].trim();
                    String originalCourse = parts[2].trim();
                    String normalizedCourse = normalizeCrossListedCourse(originalCourse);
                    
                    // Write to filtered appointments
                    filteredWriter.write(String.format("Time: %s, Course Code: %s%n", time, originalCourse));
                    
                    // Group by course and time
                    courseGroups
                        .computeIfAbsent(normalizedCourse, k -> new TreeMap<>())
                        .merge(time, 1, Integer::sum);
                }
            }

            // Write grouped appointments
            for (Map.Entry<String, Map<String, Integer>> courseEntry : courseGroups.entrySet()) {
                groupedWriter.write(String.format("Course Code: %s%n", courseEntry.getKey()));
                
                for (Map.Entry<String, Integer> timeEntry : courseEntry.getValue().entrySet()) {
                    groupedWriter.write(String.format("%d - [location] - %s%n", 
                                      timeEntry.getValue(), timeEntry.getKey()));
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
}