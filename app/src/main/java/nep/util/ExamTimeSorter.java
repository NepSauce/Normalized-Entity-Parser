package nep.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExamTimeSorter {
    public static String categorizeExams(String input) {
        // Split by course sections
        String[] lines = input.split("\n");
        StringBuilder morning = new StringBuilder("Morning (8:00-12:00):\n");
        StringBuilder noon = new StringBuilder("Noon (12:00-4:00):\n");
        StringBuilder afternoon = new StringBuilder("Afternoon (4:00-10:00):\n");

        String currentCourse = "";
        Pattern timePattern = Pattern.compile("(\\d{1,2}):(\\d{2})\\s*(AM|PM)");

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            // Check if line is a course code
            if (line.startsWith("Course Code:")) {
                currentCourse = line;
                continue;
            }

            // Extract time from the line
            Matcher matcher = timePattern.matcher(line);
            if (matcher.find()) {
                int hour = Integer.parseInt(matcher.group(1));
                int minute = Integer.parseInt(matcher.group(2));
                String ampm = matcher.group(3);

                if (ampm.equalsIgnoreCase("PM") && hour != 12) hour += 12;
                if (ampm.equalsIgnoreCase("AM") && hour == 12) hour = 0;

                // Categorize based on hour
                if (hour >= 8 && hour < 12) {
                    morning.append(currentCourse).append("\n").append(line).append("\n");
                } else if (hour >= 12 && hour < 16) {
                    noon.append(currentCourse).append("\n").append(line).append("\n");
                } else if (hour >= 16 && hour < 22) {
                    afternoon.append(currentCourse).append("\n").append(line).append("\n");
                }
            }
        }

        return morning.toString() + "\n" + noon.toString() + "\n" + afternoon.toString();
    }
}
