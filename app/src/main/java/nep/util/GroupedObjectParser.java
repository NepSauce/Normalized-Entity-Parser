package nep.util;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class GroupedObjectParser{
    public static void main(String[] args) throws IOException{
        String directoryPath = "NormalizedEntityParser/GroupedObjects/";
        File latestFile = getMostRecentFile(directoryPath);
        
        if (latestFile == null){
            System.out.println("No files found.");
            return;
        }
        
        ParseResult result = parseFile(latestFile);
        int totalSum = result.totalSum;
        int courseCount = result.uniqueCourses;
        
        System.out.println("Total numeric sum: " + totalSum);
        System.out.println("Total unique courses: " + courseCount);
    }
    
    public static File getMostRecentFile(String dirPath){
        File dir = new File(dirPath);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        
        if (files == null || files.length == 0) return null;
        
        File latest = files[0];
        
        for (File file : files){
            if (file.lastModified() > latest.lastModified()) {
                latest = file;
            }
        }
        
        return latest;
    }
    
    public static ParseResult parseFile(File file) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        int totalNumber = 0;
        Set<String> uniqueCourses = new HashSet<>();
        Pattern numberPattern = Pattern.compile("^(\\d+)\\s*-");
        
        String currentCourse = null;
        
        while ((line = reader.readLine()) != null){
            line = line.trim();
            
            if (line.startsWith("Course Code:")){
                currentCourse = line.substring("Course Code:".length()).trim();
                uniqueCourses.add(currentCourse);
            }
            else if (currentCourse != null && numberPattern.matcher(line).find()){
                Matcher matcher = numberPattern.matcher(line);
                
                if (matcher.find()) {
                    int number = Integer.parseInt(matcher.group(1));
                    totalNumber += number;
                }
            }
        }
        
        reader.close();
        return new ParseResult(totalNumber, uniqueCourses.size());
    }
    
    public static class ParseResult{
        public int totalSum;
        public int uniqueCourses;
        
        ParseResult(int totalSum, int uniqueCourses){
            this.totalSum = totalSum;
            this.uniqueCourses = uniqueCourses;
        }
    }
}
