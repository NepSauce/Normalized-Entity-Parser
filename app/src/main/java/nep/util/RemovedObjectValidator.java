package nep.util;

import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class RemovedObjectValidator {
    
    private static final Pattern ID_PATTERN = Pattern.compile("B\\d{8}");
    private static final Pattern TIME_PATTERN = Pattern.compile("\\d{1,2}:\\d{2}\\s[AP]M");
    private static final Pattern CODE_PATTERN = Pattern.compile("([A-Za-z]{2,6}/)?([A-Za-z]{2,6})[\\s-]\\d{4,5}(?:[\\s.-]\\d{1,2})?");
    
    // Basic alphabetic names, allow space or comma
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z, ]{3,}");
    private static final Pattern LOCATION_PATTERN = Pattern.compile("[a-zA-Z0-9 ]{3,}");
    
    /**
     * Validates the record and moves it to the restored file if valid.
     *
     * @param rawData the full raw record from the removed objects file
     * @return true if restored, false otherwise
     */
    public static boolean validateAndRestore(String rawData, Path removedFilePath, int lineIndex) {
        if (!isValid(rawData)) return false;
        
        try {
            // Get latest modified file in CombinedObjects directory
            Path combinedDir = Paths.get("NormalizedEntityParser/CombinedObjects");
            Files.createDirectories(combinedDir);
            
            Path latestFile = Files.list(combinedDir)
                    .filter(Files::isRegularFile)
                    .max((f1, f2) -> {
                        try {
                            return Files.getLastModifiedTime(f1).compareTo(Files.getLastModifiedTime(f2));
                        } catch (IOException e) {
                            return 0;
                        }
                    })
                    .orElse(null);
            
            if (latestFile == null) {
                System.err.println("⚠ No target file found in CombinedObjects to restore into.");
                return false;
            }
            
            // Clean the rawData to remove the keys and keep the raw values
            String cleanedData = cleanRawData(rawData);
            
            // Append to latest combined object file
            Files.write(latestFile, (cleanedData + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            
            // Remove from removed file
            removeLineFromFile(removedFilePath, lineIndex);
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static String cleanRawData(String rawData) {
        String cleanedData = rawData;
        
        // Remove keys like "DalID:", "Name:", "Code:", "Location:"
        cleanedData = cleanedData.replaceAll("DalID:\\s*", "")
                .replaceAll("Name:\\s*", "")
                .replaceAll("Code:\\s*", "")
                .replaceAll("Location:\\s*null\\s*", "") // Handle null in Location
                .replaceAll("Location:\\s*", ""); // Remove any extra location values
        
        // Trim the string to remove leading or trailing spaces
        cleanedData = cleanedData.trim();
        
        return cleanedData;
    }
    
    private static boolean isValid(String data) {
        return matches(ID_PATTERN, data) &&
                matches(TIME_PATTERN, data) &&
                matches(CODE_PATTERN, data) &&
                matches(NAME_PATTERN, extractName(data)) &&
                matches(LOCATION_PATTERN, extractLocation(data));
    }
    
    private static boolean matches(Pattern pattern, String input) {
        return input != null && pattern.matcher(input).find();
    }
    
    private static String extractName(String record) {
        // Remove "Name:" from the record and extract the raw name value
        String namePart = record.replaceAll("Name:\\s*", "").trim();
        Matcher idMatcher = ID_PATTERN.matcher(namePart);
        if (idMatcher.find()) {
            return namePart.substring(0, idMatcher.start()).replaceAll("[^a-zA-Z, ]", "").trim();
        }
        return "";
    }
    
    private static String extractLocation(String record) {
        // Remove "Location:" from the record and extract the raw location value
        Matcher locMatcher = Pattern.compile("Location:\\s*(.*?)\\s*(\\||])").matcher(record);
        if (locMatcher.find()) {
            return locMatcher.group(1).trim();
        }
        return "";
    }
    
    private static void removeLineFromFile(Path file, int lineIndex) throws IOException {
        java.util.List<String> lines = Files.readAllLines(file);
        if (lineIndex >= 0 && lineIndex < lines.size()) {
            lines.remove(lineIndex);
            Files.write(file, lines);
        }
    }
}
