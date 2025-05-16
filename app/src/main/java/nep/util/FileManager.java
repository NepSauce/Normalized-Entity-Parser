package nep.util;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileManager{
    public static int countValidLinesInFile(String filePath) throws IOException {
        File file = new File(filePath);
        
        if (!file.isFile()){
            throw new IllegalArgumentException("Provided path is not a file: " + filePath);
        }
        int lines = 0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            while (reader.readLine() != null){
                lines++;
            }
        }
        return lines;
    }
    
    public static int countLinesInFirstTxtFile(String directoryPath){
        File dir = new File(directoryPath);
        
        if (!dir.exists() || !dir.isDirectory()){
            System.out.println("Directory not found: " + directoryPath);
            return -1;
        }
        
        File[] txtFiles = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".txt"));
        
        if (txtFiles == null || txtFiles.length == 0){
            System.out.println("No .txt files found in directory.");
            return -1;
        }
        
        File file = txtFiles[0];
        int lineCount = 0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            while (reader.readLine() != null){
                lineCount++;
            }
        }
        catch (IOException e){
            System.out.println("Error reading file: " + e.getMessage());
            return -1;
        }
        
        return lineCount;
    }

    public static int countLinesInDirectory(String directoryPath) throws IOException{
        File dir = new File(directoryPath);
        
        if (!dir.isDirectory()){
            throw new IllegalArgumentException("Provided path is not a directory: " + directoryPath);
        }
        
        int totalLines = 0;
        File[] files = dir.listFiles();
        
        if (files == null){
            return 0;
        }
        
        for (File file : files){
            if (file.isDirectory()){
                totalLines += countLinesInDirectory(file.getAbsolutePath());
            }
            else if (file.isFile()){
                totalLines += countValidLinesInFile(file.getAbsolutePath());
            }
        }
        return totalLines;
    }
}
