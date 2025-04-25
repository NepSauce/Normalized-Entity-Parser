package nep.rosterconversion;

import java.io.*;
import java.util.*;

/**
 * This class processes exam roster data, normalizes cross-listed courses,
 * and generates filtered and grouped output files.
 */
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
    private static final Map<String, String> crossListedCourses = new HashMap<>();

    public static void main(String[] args) {
        String inputFilePath = "Media/output.txt";
        String outputFilePath = "Media/filtered_appointments.txt";
        String groupedFilePath = "Media/grouped_appointments.txt";
        filterAppointments(inputFilePath, outputFilePath, groupedFilePath);
    }

    public static void filterAppointments(String inputFilePath, String outputFilePath, String groupedFilePath) {
        // First pass to build cross-listed course mappings
        try (BufferedReader firstPassReader = new BufferedReader(new FileReader(inputFilePath))) {
            buildCrossListedCourseMappings(firstPassReader);
        } catch (IOException e) {
            System.err.println("Error during first pass: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Second pass to process the file
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter filteredWriter = new BufferedWriter(new FileWriter(outputFilePath));
             BufferedWriter groupedWriter = new BufferedWriter(new FileWriter(groupedFilePath))) {
            
            Map<String, Map<String, Map<String, Integer>>> courseGroups = processInputFile(reader, filteredWriter);
            writeGroupedOutput(courseGroups, groupedWriter);

            System.out.println("Successfully generated output files.");
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void buildCrossListedCourseMappings(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.replaceAll("[\\[\\]]", "");
            String[] parts = line.split("\\|");
            
            if (parts.length >= 3) {
                String course = parts[2].trim();
                if (course.contains("/")) {
                    addCrossListedCourseVariations(course);
                }
            }
        }
    }

    private static void addCrossListedCourseVariations(String crossListedCourse) {
        String[] parts = crossListedCourse.split(" ");
        if (parts.length != 2) return;
        
        String[] departments = parts[0].split("/");
        String courseNumber = parts[1];
        
        if (departments.length < 2) return;
        
        String canonicalForm = createCanonicalForm(departments, courseNumber);
        crossListedCourses.put(crossListedCourse, canonicalForm);
        
        for (int i = 0; i < departments.length; i++) {
            String singleDept = departments[i] + " " + courseNumber;
            crossListedCourses.put(singleDept, canonicalForm);
            
            for (int j = i + 1; j < departments.length; j++) {
                String reversed = departments[j] + "/" + departments[i] + " " + courseNumber;
                crossListedCourses.put(reversed, canonicalForm);
            }
        }
    }

    private static String createCanonicalForm(String[] departments, String courseNumber) {
        String[] sortedDepts = departments.clone();
        Arrays.sort(sortedDepts);
        return String.join("/", sortedDepts) + " " + courseNumber;
    }

    private static Map<String, Map<String, Map<String, Integer>>> processInputFile(
            BufferedReader reader, BufferedWriter filteredWriter) throws IOException {
        Map<String, Map<String, Map<String, Integer>>> courseGroups = new TreeMap<>();
        String line;

        while ((line = reader.readLine()) != null) {
            processLine(line, filteredWriter, courseGroups);
        }

        return courseGroups;
    }

    private static void processLine(String line, BufferedWriter filteredWriter,
            Map<String, Map<String, Map<String, Integer>>> courseGroups) throws IOException {
        line = line.replaceAll("[\\[\\]]", "");
        String[] parts = line.split("\\|");
        
        if (parts.length >= 5) {
            String time = parts[4].trim();
            String originalCourse = parts[2].trim();
            String location = parts[3].trim();
            
            if (location.equals("location")) {
                location = "[location]";
            }

            String normalizedCourse = normalizeCrossListedCourse(originalCourse);
            
            filteredWriter.write(String.format("Time: %s, Course Code: %s%n", 
                time, normalizedCourse));
            updateCourseGroups(courseGroups, normalizedCourse, location, time);
        }
    }

    private static String normalizeCrossListedCourse(String courseCode) {
        return crossListedCourses.getOrDefault(courseCode, courseCode);
    }

    private static void updateCourseGroups(
            Map<String, Map<String, Map<String, Integer>>> courseGroups,
            String course, String location, String time) {
        courseGroups
            .computeIfAbsent(course, k -> new TreeMap<>())
            .computeIfAbsent(location, k -> new TreeMap<>())
            .merge(time, 1, Integer::sum);
    }

    private static void writeGroupedOutput(
            Map<String, Map<String, Map<String, Integer>>> courseGroups,
            BufferedWriter groupedWriter) throws IOException {
        for (Map.Entry<String, Map<String, Map<String, Integer>>> courseEntry : courseGroups.entrySet()) {
            writeCourseHeader(courseEntry.getKey(), groupedWriter);
            writeLocationEntries(courseEntry.getValue(), groupedWriter);
            groupedWriter.newLine();
        }
    }

    private static void writeCourseHeader(String courseCode, BufferedWriter groupedWriter) 
            throws IOException {
        String courseLine = String.format("Course Code: %s", courseCode);
        groupedWriter.write(String.format("%-50s%s%n", courseLine, METADATA_FIELDS[0]));
    }

    private static void writeLocationEntries(
            Map<String, Map<String, Integer>> locationMap,
            BufferedWriter groupedWriter) throws IOException {
        boolean firstLocation = true;

        for (Map.Entry<String, Map<String, Integer>> locationEntry : locationMap.entrySet()) {
            writeTimeEntries(locationEntry.getKey(), locationEntry.getValue(), 
                groupedWriter, firstLocation);
            firstLocation = false;
        }
    }

    private static void writeTimeEntries(String location, 
            Map<String, Integer> timeMap, BufferedWriter groupedWriter,
            boolean firstLocation) throws IOException {
        boolean firstTimeEntry = true;

        for (Map.Entry<String, Integer> timeEntry : timeMap.entrySet()) {
            String timeLine = String.format(" %d - %s - %s", 
                timeEntry.getValue(), location, timeEntry.getKey());
            groupedWriter.write(String.format("%-50s", timeLine));
            
            if (firstTimeEntry && firstLocation) {
                writeMetadataFields(groupedWriter);
                firstTimeEntry = false;
            } else {
                groupedWriter.write("\n");
            }
        }
    }

    private static void writeMetadataFields(BufferedWriter groupedWriter) throws IOException {
        for (int i = 1; i < METADATA_FIELDS.length; i++) {
            groupedWriter.write(METADATA_FIELDS[i] + "\n");
            if (i < METADATA_FIELDS.length - 1) {
                groupedWriter.write(String.format("%-50s", ""));
            }
        }
    }
}