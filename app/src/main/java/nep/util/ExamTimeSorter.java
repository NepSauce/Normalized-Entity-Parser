package nep.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExamTimeSorter {

    public static String sortExamsByTimeSections(String input) {
        Map<String, List<String>> morning = new LinkedHashMap<>();
        Map<String, List<String>> noon = new LinkedHashMap<>();
        Map<String, List<String>> afternoon = new LinkedHashMap<>();
        Map<String, List<String>> evening = new LinkedHashMap<>();

        String[] lines = input.split("\n");
        String currentCourse = null;
        List<String> currentTimes = new ArrayList<>();

        Pattern coursePattern = Pattern.compile("^Course Code:\\s*(.+)$");
        Pattern timePattern = Pattern.compile(".*-\\s*([\\d]{1,2}:\\d{2}\\s*[APM]{2})$");

        for (String line : lines) {
            Matcher courseMatcher = coursePattern.matcher(line);
            Matcher timeMatcher = timePattern.matcher(line);

            if (courseMatcher.matches()) {
                currentCourse = courseMatcher.group(1).trim();
            } else if (timeMatcher.matches() && currentCourse != null) {
                String timeString = timeMatcher.group(1).trim();
                LocalTime time = LocalTime.parse(timeString.toUpperCase(), DateTimeFormatter.ofPattern("h:mm a"));

                // Determine section
                Map<String, List<String>> targetMap;
                if (!time.isBefore(LocalTime.of(8, 0)) && time.isBefore(LocalTime.of(12, 0))) {
                    targetMap = morning;
                } else if (!time.isBefore(LocalTime.of(12, 0)) && time.isBefore(LocalTime.of(16, 0))) {
                    targetMap = noon;
                } else if (!time.isBefore(LocalTime.of(16, 0)) && time.isBefore(LocalTime.of(20, 0))) {
                    targetMap = afternoon;
                } else {
                    targetMap = evening;
                }

                // Add to map
                targetMap.computeIfAbsent(currentCourse, k -> new ArrayList<>()).add(line);
            }
        }

        // Build output
        StringBuilder output = new StringBuilder();

        if (!morning.isEmpty()) {
            output.append("Morning\n");
            for (Map.Entry<String, List<String>> entry : morning.entrySet()) {
                output.append("Course Code: ").append(entry.getKey()).append("\n");
                for (String s : entry.getValue()) {
                    output.append(s).append("\n");
                }
            }
        }

        if (!noon.isEmpty()) {
            output.append("Noon\n");
            for (Map.Entry<String, List<String>> entry : noon.entrySet()) {
                output.append("Course Code: ").append(entry.getKey()).append("\n");
                for (String s : entry.getValue()) {
                    output.append(s).append("\n");
                }
            }
        }

        if (!afternoon.isEmpty()) {
            output.append("Afternoon\n");
            for (Map.Entry<String, List<String>> entry : afternoon.entrySet()) {
                output.append("Course Code: ").append(entry.getKey()).append("\n");
                for (String s : entry.getValue()) {
                    output.append(s).append("\n");
                }
            }
        }

        if (!evening.isEmpty()) {
            output.append("Evening\n");
            for (Map.Entry<String, List<String>> entry : evening.entrySet()) {
                output.append("Course Code: ").append(entry.getKey()).append("\n");
                for (String s : entry.getValue()) {
                    output.append(s).append("\n");
                }
            }
        }

        return output.toString();
    }
}
