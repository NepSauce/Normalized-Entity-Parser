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

            printCourseSums(courseSums, totalCourses, totalStudents);
        }
        else{
            System.out.println("No file found in the specified directory.");
        }
    }

    private static File getMostRecentFile(String directoryPath){
        File directory = new File(directoryPath);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt"));
        
        if (files == null || files.length == 0){
            return null;
        }

        Arrays.sort(files, (file1, file2) -> Long.compare(file2.lastModified(), file1.lastModified()));
        
        return files[0];
    }

    private static Map<String, Integer> parseFile(File file){
        Map<String, Integer> courseSums = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            
            while ((line = br.readLine()) != null){
                String[] parts = line.split(" - ");
                
                if (parts.length == 3){
                    String courseCode = parts[0].trim();
                    
                    try{
                        int number = Integer.parseInt(parts[1].trim());
                        courseSums.put(courseCode, courseSums.getOrDefault(courseCode, 0) + number);
                    }
                    catch (NumberFormatException e){
                        System.err.println("Skipping invalid line: " + line);
                    }
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        
        return courseSums;
    }

    private static void printCourseSums(Map<String, Integer> courseSums, int totalCourses, int totalStudents){
        System.out.println("Course Code Totals:");
        
        for (Map.Entry<String, Integer> entry : courseSums.entrySet()){
            System.out.println("Course Code: " + entry.getKey() + " Total: " + entry.getValue());
        }

        System.out.println("\nTotal Courses: " + totalCourses);
        System.out.println("Total Students: " + totalStudents);
    }
}
