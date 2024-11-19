package com.hado90.fileMgt.extractor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtility {
    //create folder in a specified dir
    public void createFolder(String folderPath){
        File folder = new File(folderPath);
        if(!folder.exists()){ folder.mkdirs();}
    }

    //check if a file exists
    public boolean fileOrDirectoryExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }
    
    //delete a file
    public void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) { file.delete(); }
    }

    //deleteFolder
    public void deleteFolder(String folderPath) {
        File folder = new File(folderPath);
    
        if (folder.exists() && folder.isDirectory()) {
            
            for (File file : folder.listFiles()) {
                if (file.isDirectory()) { deleteFolder(file.getAbsolutePath()); } 
                else { file.delete();}
            }
            folder.delete();
        }
    }
    
    //move file to another folder
    public void moveFile(String inputFilePath, String outputFilePath) {
        File sourceFile = new File(inputFilePath);
        File destinationFile = new File(outputFilePath);

        if (sourceFile.exists() && !destinationFile.exists()) { boolean moveSuccess = sourceFile.renameTo(destinationFile);
        if (!moveSuccess) { System.out.println("Failed to move the file.");}
        }
    }  

    //delete all files from a folder
    public void wipeFolderContents(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) { deleteFile(file.getAbsolutePath()); }
        }
    }

    //locate compressed files in a location
    public List<String> getDirectoryContents (String directoryPath){
        List<String> directoryContentPaths = new ArrayList<>();
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) { directoryContentPaths.add(file.getAbsolutePath()); }
            }
        }
        
        return directoryContentPaths;
    }

    public List<String> filterDirectoryContentByTypes(String[] types, List<String> directoryContentPaths) {
        return directoryContentPaths.stream()
        .filter(path -> java.util.Arrays.stream(types).anyMatch(path::endsWith))
        .collect(Collectors.toList());
    }

    public String getFileExtensionFromPath(String inputPath){
        if (inputPath == null || !inputPath.contains(".")) { return ""; }
        return inputPath.substring(inputPath.lastIndexOf('.') + 1);
    }

    public List<String> getSubdirectoryPaths(String directoryPath) {
        if (!fileOrDirectoryExists(directoryPath) || !new File(directoryPath).isDirectory()) { return Collections.emptyList(); }
    
        List<String> allDirectoryContents = getDirectoryContents(directoryPath);
        return filterDirectoryContentByTypes(new String[]{""}, allDirectoryContents).stream()
                .filter(path -> new File(path).isDirectory())
                .map(path -> path.endsWith(File.separator) ? path : path + File.separator)
                .collect(Collectors.toList());
    }  

    public List<String> getSubdirectoryFiles(String directoryPath) {
        List<String> allFiles = new ArrayList<>();
        searchFilesRecursively(new File(directoryPath), allFiles);
        return allFiles;
    }

    private void searchFilesRecursively(File directory, List<String> allFiles) {
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // Recurse into subdirectory
                        searchFilesRecursively(file, allFiles);
                    } else {
                        // Add file path if it's a file
                        allFiles.add(file.getAbsolutePath());
                    }
                }
            }
        }
    }
}
