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

    /**
     * Filters and groups the appointments based on input text file.
     *
     * @param inputFilePath  Path to the input text file.
     * @param outputFilePath Path to write filtered output.
     * @param groupedFilePath Path to write grouped output.
     */
    public static void filterAppointments(String inputFilePath, String outputFilePath, String groupedFilePath) {
        try (BufferedReader firstPassReader = new BufferedReader(new FileReader(inputFilePath))) {
            buildCrossListedCourseMappings(firstPassReader);
        } catch (IOException e) {
            System.err.println("Error during first pass: " + e.getMessage());
            e.printStackTrace();
            return;
        }

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

    /**
     * Builds mappings for cross-listed courses from input file lines.
     *
     * @param reader BufferedReader to read the input file.
     * @throws IOException if file reading fails.
     */
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

    /**
     * Adds cross-listed course variants and their canonical forms to the mapping.
     *
     * @param crossListedCourse Course string with multiple departments.
     */
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

    /**
     * Creates a standardized canonical form for cross-listed course codes.
     *
     * @param departments Array of department codes.
     * @param courseNumber Course number.
     * @return Canonical string representation.
     */
    private static String createCanonicalForm(String[] departments, String courseNumber) {
        String[] sortedDepts = departments.clone();
        Arrays.sort(sortedDepts);
        return String.join("/", sortedDepts) + " " + courseNumber;
    }

    /**
     * Processes the input file and generates course group data.
     *
     * @param reader BufferedReader to read the file.
     * @param filteredWriter Writer to write filtered appointment lines.
     * @return Map of course groups.
     * @throws IOException if reading or writing fails.
     */
    private static Map<String, Map<String, Map<String, Integer>>> processInputFile(
            BufferedReader reader, BufferedWriter filteredWriter) throws IOException {
        Map<String, Map<String, Map<String, Integer>>> courseGroups = new TreeMap<>();
        String line;

        while ((line = reader.readLine()) != null) {
            processLine(line, filteredWriter, courseGroups);
        }

        return courseGroups;
    }

    /**
     * Processes a single line of appointment data.
     *
     * @param line Text line to parse.
     * @param filteredWriter Writer for filtered output.
     * @param courseGroups Map structure to update groupings.
     * @throws IOException if writing fails.
     */
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

    /**
     * Returns the canonical form of a course if it is cross-listed.
     *
     * @param courseCode Original course code.
     * @return Normalized course code.
     */
    private static String normalizeCrossListedCourse(String courseCode) {
        return crossListedCourses.getOrDefault(courseCode, courseCode);
    }

    /**
     * Updates the nested map structure used for grouped output.
     *
     * @param courseGroups Map to update.
     * @param course Course name.
     * @param location Location name.
     * @param time Exam time.
     */
    private static void updateCourseGroups(
            Map<String, Map<String, Map<String, Integer>>> courseGroups,
            String course, String location, String time) {
        courseGroups
            .computeIfAbsent(course, k -> new TreeMap<>())
            .computeIfAbsent(location, k -> new TreeMap<>())
            .merge(time, 1, Integer::sum);
    }

    /**
     * Writes the grouped course output to file.
     *
     * @param courseGroups Nested grouping structure.
     * @param groupedWriter Writer to output grouped info.
     * @throws IOException if writing fails.
     */
    private static void writeGroupedOutput(
            Map<String, Map<String, Map<String, Integer>>> courseGroups,
            BufferedWriter groupedWriter) throws IOException {
        for (Map.Entry<String, Map<String, Map<String, Integer>>> courseEntry : courseGroups.entrySet()) {
            writeCourseHeader(courseEntry.getKey(), groupedWriter);
            writeLocationEntries(courseEntry.getValue(), groupedWriter);
            groupedWriter.newLine();
        }
    }

    /**
     * Writes the header line for each course group.
     *
     * @param courseCode Course code string.
     * @param groupedWriter Writer for output.
     * @throws IOException if writing fails.
     */
    private static void writeCourseHeader(String courseCode, BufferedWriter groupedWriter) 
            throws IOException {
        String courseLine = String.format("Course Code: %s", courseCode);
        groupedWriter.write(String.format("%-50s%s%n", courseLine, METADATA_FIELDS[0]));
    }

    /**
     * Writes entries grouped by location and time.
     *
     * @param locationMap Map of location to time slots.
     * @param groupedWriter Writer for output.
     * @throws IOException if writing fails.
     */
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

    /**
     * Writes the time and count entries for each location.
     *
     * @param location Location name.
     * @param timeMap Map of times to student counts.
     * @param groupedWriter Writer to output to.
     * @param firstLocation Flag indicating if this is the first location.
     * @throws IOException if writing fails.
     */
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

    /**
     * Writes additional metadata fields for grouped output.
     *
     * @param groupedWriter Writer for output.
     * @throws IOException if writing fails.
     */
    private static void writeMetadataFields(BufferedWriter groupedWriter) throws IOException {
        for (int i = 1; i < METADATA_FIELDS.length; i++) {
            groupedWriter.write(METADATA_FIELDS[i] + "\n");
            if (i < METADATA_FIELDS.length - 1) {
                groupedWriter.write(String.format("%-50s", ""));
            }
        }
    }
}
