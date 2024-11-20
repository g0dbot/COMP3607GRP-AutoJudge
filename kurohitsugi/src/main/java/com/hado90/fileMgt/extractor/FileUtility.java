package com.hado90.fileMgt.extractor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This utility class provides various file management functions, such as creating folders,
 * deleting files and directories, moving files, and retrieving directory contents.
 * It also offers methods to filter files by type and to work with subdirectories.
 */
public class FileUtility {

    /**
     * Creates a folder in the specified directory path.
     * 
     * @param folderPath the path where the folder should be created.
     */
    public void createFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();  // Create the directory (and any missing parent directories)
        }
    }

    /**
     * Checks if a file or directory exists at the specified path.
     * 
     * @param filePath the path to check.
     * @return true if the file or directory exists, false otherwise.
     */
    public boolean fileOrDirectoryExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * Deletes the file at the specified path.
     * 
     * @param filePath the path to the file to be deleted.
     */
    public void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();  // Delete the file if it exists
        }
    }

    /**
     * Recursively deletes the folder and all its contents (files and subdirectories).
     * 
     * @param folderPath the path to the folder to be deleted.
     */
    public void deleteFolder(String folderPath) {
        File folder = new File(folderPath);

        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (file.isDirectory()) {
                    deleteFolder(file.getAbsolutePath());  // Recurse into subdirectories
                } else {
                    file.delete();  // Delete files
                }
            }
            folder.delete();  // Delete the folder itself
        }
    }

    /**
     * Moves a file from one location to another.
     * 
     * @param inputFilePath the path of the file to move.
     * @param outputFilePath the destination path for the file.
     */
    public void moveFile(String inputFilePath, String outputFilePath) {
        File sourceFile = new File(inputFilePath);
        File destinationFile = new File(outputFilePath);

        if (sourceFile.exists() && !destinationFile.exists()) {
            boolean moveSuccess = sourceFile.renameTo(destinationFile);
            if (!moveSuccess) {
                System.out.println("Failed to move the file.");
            }
        }
    }

    /**
     * Deletes all files from the specified folder, but does not delete the folder itself.
     * 
     * @param folderPath the path of the folder to wipe.
     */
    public void wipeFolderContents(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                deleteFile(file.getAbsolutePath());  // Delete each file in the folder
            }
        }
    }

    /**
     * Retrieves a list of file paths in the specified directory.
     * 
     * @param directoryPath the path to the directory to scan.
     * @return a list of file paths in the directory.
     */
    public List<String> getDirectoryContents(String directoryPath) {
        List<String> directoryContentPaths = new ArrayList<>();
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    directoryContentPaths.add(file.getAbsolutePath());  // Add file path to the list
                }
            }
        }

        return directoryContentPaths;
    }

    /**
     * Filters the contents of a directory by the specified file types.
     * 
     * @param types the file extensions to filter by (e.g., "zip", "txt").
     * @param directoryContentPaths the list of file paths to filter.
     * @return a list of file paths that match one of the specified types.
     */
    public List<String> filterDirectoryContentByTypes(String[] types, List<String> directoryContentPaths) {
        return directoryContentPaths.stream()
            .filter(path -> java.util.Arrays.stream(types).anyMatch(path::endsWith))  // Filter by file extension
            .collect(Collectors.toList());
    }

    /**
     * Extracts the file extension from a given file path.
     * 
     * @param inputPath the path to the file.
     * @return the file extension (e.g., "txt"), or an empty string if no extension is found.
     */
    public String getFileExtensionFromPath(String inputPath) {
        if (inputPath == null || !inputPath.contains(".")) {
            return "";  // Return empty string if no extension is found
        }
        return inputPath.substring(inputPath.lastIndexOf('.') + 1);  // Return extension after the last dot
    }

    /**
     * Retrieves a list of subdirectory paths from the specified directory.
     * 
     * @param directoryPath the path to the directory to scan.
     * @return a list of subdirectory paths, or an empty list if no subdirectories are found.
     */
    public List<String> getSubdirectoryPaths(String directoryPath) {
        if (!fileOrDirectoryExists(directoryPath) || !new File(directoryPath).isDirectory()) {
            return Collections.emptyList();  // Return an empty list if the directory doesn't exist
        }

        List<String> allDirectoryContents = getDirectoryContents(directoryPath);
        return filterDirectoryContentByTypes(new String[]{""}, allDirectoryContents).stream()
            .filter(path -> new File(path).isDirectory())  // Only include directories
            .map(path -> path.endsWith(File.separator) ? path : path + File.separator)  // Ensure directory paths end with separator
            .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of all files (including those in subdirectories) within a directory.
     * 
     * @param directoryPath the path to the directory to scan.
     * @return a list of file paths within the directory and its subdirectories.
     */
    public List<String> getSubdirectoryFiles(String directoryPath) {
        List<String> allFiles = new ArrayList<>();
        searchFilesRecursively(new File(directoryPath), allFiles);
        return allFiles;
    }

    /**
     * Recursively searches through a directory and its subdirectories to find all files.
     * 
     * @param directory the directory to search.
     * @param allFiles the list to store found file paths.
     */
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