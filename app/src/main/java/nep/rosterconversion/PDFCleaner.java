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

    /**
     * Processes input file to filter appointments and generate grouped output.
     *
     * @param inputFilePath Path to the input file containing raw appointment data
     * @param outputFilePath Path for the filtered appointments output file
     * @param groupedFilePath Path for the grouped appointments output file
     */
    public static void filterAppointments(String inputFilePath, String outputFilePath, String groupedFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter filteredWriter = new BufferedWriter(new FileWriter(outputFilePath));
             BufferedWriter groupedWriter = new BufferedWriter(new FileWriter(groupedFilePath))) {
            
            Map<String, Map<String, Integer>> courseGroups = processInputFile(reader, filteredWriter);
            writeGroupedOutput(courseGroups, groupedWriter);

            System.out.println("Successfully generated output files.");
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Processes the input file and builds a map of course groups.
     *
     * @param reader BufferedReader for the input file
     * @param filteredWriter BufferedWriter for the filtered output
     * @return Map containing grouped course information
     * @throws IOException If there's an error reading or writing files
     */
    private static Map<String, Map<String, Integer>> processInputFile(BufferedReader reader, 
                                                                     BufferedWriter filteredWriter) 
                                                                     throws IOException {
        Map<String, Map<String, Integer>> courseGroups = new TreeMap<>();
        String line;

        while ((line = reader.readLine()) != null) {
            processLine(line, filteredWriter, courseGroups);
        }

        return courseGroups;
    }

    /**
     * Processes a single line from the input file.
     *
     * @param line The line to process
     * @param filteredWriter BufferedWriter for the filtered output
     * @param courseGroups Map to store the grouped course information
     * @throws IOException If there's an error writing to the output
     */
    private static void processLine(String line, BufferedWriter filteredWriter, 
                                  Map<String, Map<String, Integer>> courseGroups) 
                                  throws IOException {
        line = line.replaceAll("[\\[\\]]", "");
        String[] parts = line.split("\\|");
        
        if (parts.length >= 5) {
            String time = parts[4].trim();
            String originalCourse = parts[2].trim();
            String normalizedCourse = normalizeCrossListedCourse(originalCourse);
            
            filteredWriter.write(String.format("Time: %s, Course Code: %s%n", time, originalCourse));
            updateCourseGroups(courseGroups, normalizedCourse, time);
        }
    }

    /**
     * Updates the course groups map with new time information.
     *
     * @param courseGroups Map containing the grouped courses
     * @param course The course code
     * @param time The appointment time
     */
    private static void updateCourseGroups(Map<String, Map<String, Integer>> courseGroups,
                                         String course, String time) {
        courseGroups
            .computeIfAbsent(course, k -> new TreeMap<>())
            .merge(time, 1, Integer::sum);
    }

    /**
     * Writes the grouped appointment information to the output file.
     *
     * @param courseGroups Map containing the grouped course information
     * @param groupedWriter BufferedWriter for the grouped output
     * @throws IOException If there's an error writing to the output
     */
    private static void writeGroupedOutput(Map<String, Map<String, Integer>> courseGroups,
                                         BufferedWriter groupedWriter) throws IOException {
        for (Map.Entry<String, Map<String, Integer>> courseEntry : courseGroups.entrySet()) {
            writeCourseHeader(courseEntry.getKey(), groupedWriter);
            writeTimeEntries(courseEntry, groupedWriter);
            groupedWriter.newLine();
        }
    }

    /**
     * Writes the course header information to the output file.
     *
     * @param courseCode The course code to write
     * @param groupedWriter BufferedWriter for the grouped output
     * @throws IOException If there's an error writing to the output
     */
    private static void writeCourseHeader(String courseCode, BufferedWriter groupedWriter) 
                                        throws IOException {
        String courseLine = String.format("Course Code: %s", courseCode);
        groupedWriter.write(String.format("%-50s%s%n", courseLine, METADATA_FIELDS[0]));
    }

    /**
     * Writes the time entries for a course to the output file.
     *
     * @param courseEntry Map entry containing the course time information
     * @param groupedWriter BufferedWriter for the grouped output
     * @throws IOException If there's an error writing to the output
     */
    private static void writeTimeEntries(Map.Entry<String, Map<String, Integer>> courseEntry,
                                       BufferedWriter groupedWriter) throws IOException {
        boolean firstTimeEntry = true;

        for (Map.Entry<String, Integer> timeEntry : courseEntry.getValue().entrySet()) {
            String timeLine = String.format(" %d - [location] - %s", 
                              timeEntry.getValue(), timeEntry.getKey());
            groupedWriter.write(String.format("%-50s", timeLine));
            
            if (firstTimeEntry) {
                writeMetadataFields(groupedWriter);
                firstTimeEntry = false;
            } else {
                groupedWriter.write("\n");
            }
        }
    }

    /**
     * Writes the metadata fields to the output file.
     *
     * @param groupedWriter BufferedWriter for the grouped output
     * @throws IOException If there's an error writing to the output
     */
    private static void writeMetadataFields(BufferedWriter groupedWriter) throws IOException {
        for (int i = 1; i < METADATA_FIELDS.length; i++) {
            groupedWriter.write(METADATA_FIELDS[i] + "\n");
            if (i < METADATA_FIELDS.length - 1) {
                groupedWriter.write(String.format("%-50s", ""));
            }
        }
    }

    /**
     * Normalizes cross-listed course codes by sorting department prefixes.
     *
     * @param courseCode The original course code
     * @return Normalized course code with sorted department prefixes
     */
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