package nep.util;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileManager {
    
    // Count lines in a single file
    public static int countLinesInFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.isFile()) {
            throw new IllegalArgumentException("Provided path is not a file: " + filePath);
        }
        
        int lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) {
                lines++;
            }
        }
        return lines;
    }
    
    // Count lines in all files within a directory (recursively)
    public static int countLinesInDirectory(String directoryPath) throws IOException {
        File dir = new File(directoryPath);
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Provided path is not a directory: " + directoryPath);
        }
        
        int totalLines = 0;
        File[] files = dir.listFiles();
        if (files == null) return 0;
        
        for (File file : files) {
            if (file.isDirectory()) {
                totalLines += countLinesInDirectory(file.getAbsolutePath());
            } else if (file.isFile()) {
                totalLines += countLinesInFile(file.getAbsolutePath());
            }
        }
        return totalLines;
    }
    
    // Replace a specific line in a file within a folder
    public static void replaceLineInFileFromFolder(String folderPath, int lineNumber, String newContent) throws IOException {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) throw new IOException("No files in folder: " + folderPath);
        
        File file = files[0]; // Only one file per folder
        List<String> lines = Files.readAllLines(file.toPath());
        
        if (lineNumber < 1 || lineNumber > lines.size()) {
            throw new IllegalArgumentException("Invalid line number: " + lineNumber);
        }
        
        lines.set(lineNumber - 1, newContent);
        Files.write(file.toPath(), lines);
    }
    
    // Remove a specific line in a file within a folder
    public static void removeLineInFileFromFolder(String folderPath, int lineNumber) throws IOException {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) throw new IOException("No files in folder: " + folderPath);
        
        File file = files[0];
        List<String> lines = Files.readAllLines(file.toPath());
        
        if (lineNumber < 1 || lineNumber > lines.size()) {
            throw new IllegalArgumentException("Invalid line number: " + lineNumber);
        }
        
        lines.remove(lineNumber - 1);
        Files.write(file.toPath(), lines);
    }
    
    // Append a line to every file in a folder (one file per folder)
    public static void appendLineToAllFilesInFolder(String parentFolderPath, String lineToAppend) throws IOException {
        File parent = new File(parentFolderPath);
        if (!parent.isDirectory()) throw new IllegalArgumentException("Not a valid directory: " + parentFolderPath);
        
        File[] folders = parent.listFiles(File::isDirectory);
        if (folders == null) return;
        
        for (File folder : folders) {
            File[] files = folder.listFiles();
            if (files != null && files.length > 0) {
                File file = files[0]; // Only one file per folder
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                    writer.newLine();
                    writer.write(lineToAppend);
                }
            }
        }
    }
}
