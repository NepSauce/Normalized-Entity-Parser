package nep.util;

import java.io.*;
import java.util.*;

public class GroupedObjectParser{
    public static void main(String[] args){
        String directoryPath = "path/to/your/directory";
        File mostRecentFile = getMostRecentFile(directoryPath);
        
        if (mostRecentFile != null){
            Map<String, Integer> courseSums = parseFile(mostRecentFile);
            int totalCourses = courseSums.size();
            int totalStudents = courseSums.values().stream().mapToInt(Integer::intValue).sum();

        }
        else{
            System.out.println("No file found in the specified directory.");
        }
    }

    public static File getMostRecentFile(String directoryPath){
        File directory = new File(directoryPath);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt"));
        
        if (files == null || files.length == 0){
            return null;
        }

        Arrays.sort(files, (file1, file2) -> Long.compare(file2.lastModified(), file1.lastModified()));
        
        return files[0];
    }
    
    public static Map<String, Integer> parseFile(File file) {
        Map<String, Integer> courseSums = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            
            while ((line = br.readLine()) != null) {
                // Split by " - "
                String[] parts = line.split(" - ");
                
                if (parts.length >= 3) {
                    try {
                        int number = Integer.parseInt(parts[0].trim());
                        String courseCode = parts[2].trim();
                        courseSums.put(courseCode, courseSums.getOrDefault(courseCode, 0) + number);
                    }
                    catch (NumberFormatException e){
                        throw new RuntimeException(e);
                    }
                    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return courseSums;
    }
    
}
