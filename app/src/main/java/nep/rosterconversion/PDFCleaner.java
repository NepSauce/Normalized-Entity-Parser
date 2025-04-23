package nep.rosterconversion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDFCleaner {
    public static void main(String[] args) {
        String inputFilePath = "Media/output.txt";  // Path to your input text file
        String outputFilePath = "Media/filtered_appointments.txt";  // Path to your output text file
        filterAppointments(inputFilePath, outputFilePath);
    }

    public static void filterAppointments(String inputFilePath, String outputFilePath) {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        StringBuilder fullText = new StringBuilder();  // To hold all the lines as a single string

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

            // Regular expression to match the time and course code
            String regex = "(\\d{1,2}:\\d{2} [APM]{2}).*?(\\w{4,6} \\d{4})";
            Pattern pattern = Pattern.compile(regex);

            // Use a matcher on the full text
            Matcher matcher = pattern.matcher(fullText.toString());

            // Filter and write to the output file
            while (matcher.find()) {
                String time = matcher.group(1);
                String courseCode = matcher.group(2);
                writer.write("Time: " + time + ", Course Code: " + courseCode);
                writer.newLine();  // Write each result on a new line
            }

            System.out.println("Filtered appointments have been written to " + outputFilePath);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
