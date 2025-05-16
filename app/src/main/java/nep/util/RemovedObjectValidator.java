package nep.util;

import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class RemovedObjectValidator{
    
    private static final Pattern ID_PATTERN = Pattern.compile("B\\d{8}");
    private static final Pattern TIME_PATTERN = Pattern.compile("\\d{1,2}:\\d{2}\\s[AP]M");
    private static final Pattern CODE_PATTERN = Pattern.compile("([A-Z]{2,6}/)?([A-Z]{2,6})[\\s-]\\d{4,5}(?:[\\s.-]\\d{1,2})?");
    private static final Pattern NAME_PATTERN = Pattern.compile("[A-Z ,']{3,}");
    private static final Pattern LOCATION_PATTERN = Pattern.compile("[A-Z0-9 ()]{3,}");
    
    public static boolean validateAndRestore(String rawData, Path removedFilePath, int lineIndex){
        if (rawData == null || !isValid(rawData)){
            new DisplayUIPopup("Validation Failed", "Invalid record or missing fields.", 104).showInfoPopup();
            return false;
        }
        
        try{
            Path combinedDir = Paths.get("NormalizedEntityParser/CombinedObjects");
            Files.createDirectories(combinedDir);
            
            Path latestFile = Files.list(combinedDir)
                    .filter(Files::isRegularFile)
                    .max((f1, f2) -> {
                        try{
                            return Files.getLastModifiedTime(f1).compareTo(Files.getLastModifiedTime(f2));
                        }
                        catch (IOException e){
                            return 0;
                        }
                    })
                    .orElseGet(() -> {
                        try {
                            Path newFile = combinedDir.resolve("combined_" + System.currentTimeMillis() + ".txt");
                            Files.createFile(newFile);
                            return newFile;
                        }
                        catch (IOException e){
                            return null;
                        }
                    });
            
            if (latestFile == null){
                return false;
            }
            
            String cleanedData = cleanRawData(rawData);
            Files.write(latestFile, (cleanedData + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            
            removeLineFromFile(removedFilePath, lineIndex);
            
            new DisplayUIPopup("Success", "Entry has been successfully validated and restored.", 0).showInfoPopup();
            return true;
        }
        catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }
    
    private static boolean isValid(String data){
        if (data == null){
            return false;
        }
        
        String id = extractField("DalID", data);
        String name = extractField("Name", data);
        String code = extractField("Code", data);
        String location = extractField("Location", data);
        String time = extractTime(data);
        
        return isNonNullUpperValid(id, ID_PATTERN)
                && isNonNullUpperValid(name, NAME_PATTERN)
                && isNonNullUpperValid(code, CODE_PATTERN)
                && isNonNullUpperValid(location, LOCATION_PATTERN)
                && isNonNullUpperValid(time, TIME_PATTERN);
    }
    
    private static boolean isNonNullUpperValid(String input, Pattern pattern){
        return input != null && input.equals(input.toUpperCase()) && pattern.matcher(input).matches();
    }
    
    private static String extractField(String key, String record){
        Matcher matcher = Pattern.compile(key + ":\\s*([^|\\]]+)").matcher(record);
        return matcher.find() ? matcher.group(1).trim() : null;
    }
    
    private static String extractTime(String record){
        Matcher matcher = TIME_PATTERN.matcher(record);
        return matcher.find() ? matcher.group(0).trim() : null;
    }
    
    private static String cleanRawData(String rawData){
        return rawData.replaceAll("(?i)(DalID:|Name:|Code:|Location:|Time:)\\s*", "").trim();
    }
    
    private static void removeLineFromFile(Path file, int lineIndex) throws IOException {
        if (file == null || lineIndex < 0) return;
        
        java.util.List<String> lines = Files.readAllLines(file);
        if (lineIndex < lines.size()) {
            lines.remove(lineIndex);
            Files.write(file, lines);
        }
    }
}
