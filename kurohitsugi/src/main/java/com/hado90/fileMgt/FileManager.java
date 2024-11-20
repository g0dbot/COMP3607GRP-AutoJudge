package com.hado90.fileMgt;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.hado90.fileMgt.extractor.Extractor;
import com.hado90.fileMgt.extractor.ExtractorZip;
import com.hado90.fileMgt.extractor.FileUtility;

/**
 * This class provides a facade for file management utilities and extractors.
 * It simplifies the creation, registration, and use of extractors for different file types, 
 * as well as providing common file operations such as creating folders, moving files, 
 * deleting files and folders, and extracting files from archives.
 */
public class FileManager {

    public final FileUtility fileUtility; // Instance of FileUtility for file operations.
    private final HashMap<String, Extractor> extractors; // A map to store extractors by file type.

    /**
     * Constructor that initializes the file utility and registers extractors based on the provided types.
     * 
     * @param extractorTypes an array of extractor types to be registered.
     */
    public FileManager(String[] extractorTypes) {
        this.fileUtility = new FileUtility();
        this.extractors = new HashMap<>();

        // Initialize and register extractors
        for (String type : extractorTypes) {
            Extractor extractor = createExtractorType(type);
            if (extractor != null) {
                registerExtractor(type, extractor);
            } else {
                System.out.println("Invalid extractor type: " + type);
            }
        }
    }

    /**
     * Retrieves the extractor for a specific type.
     * 
     * @param type the type of extractor to retrieve (e.g., "zip").
     * @return the Extractor object for the given type, or null if not found.
     */
    public Extractor getExtractor(String type) {
        if (extractors.containsKey(type)) {
            return extractors.get(type);
        }
        return null;
    }

    /**
     * Registers an extractor for a specific file type.
     * 
     * @param type the type of extractor (e.g., "zip").
     * @param extractor the Extractor object to be registered.
     */
    public void registerExtractor(String type, Extractor extractor) {
        this.extractors.put(type, extractor);
    }

    /**
     * Creates an extractor based on the given type.
     * 
     * @param type the type of extractor to create (e.g., "zip").
     * @return the created Extractor object, or null if the type is unsupported.
     */
    private Extractor createExtractorType(String type) {
        if (type.equalsIgnoreCase("zip")) {
            return new ExtractorZip(fileUtility);  // Create Zip extractor
        }
        return null;
    }

    // **Facade Methods for File Operations**
    
    /**
     * Creates a folder in the specified directory path.
     * 
     * @param folderPath the path where the folder should be created.
     */
    public void createFolder(String folderPath) {
        this.fileUtility.createFolder(folderPath);
    }

    /**
     * Checks if a file or directory exists at the specified path.
     * 
     * @param filePath the path to check.
     * @return true if the file or directory exists, false otherwise.
     */
    public boolean fileOrDirectoryExists(String filePath) {
        return this.fileUtility.fileOrDirectoryExists(filePath);
    }

    /**
     * Deletes the file at the specified path.
     * 
     * @param filePath the path to the file to be deleted.
     */
    public void deleteFile(String filePath) {
        this.fileUtility.deleteFile(filePath);
    }

    /**
     * Deletes the folder and its contents (files and subdirectories) at the specified path.
     * 
     * @param folderPath the path of the folder to be deleted.
     */
    public void deleteFolder(String folderPath) {
        this.fileUtility.deleteFolder(folderPath);
    }

    /**
     * Moves a file from one location to another.
     * 
     * @param inputFilePath the path of the file to move.
     * @param outputFilePath the destination path for the file.
     */
    public void moveFile(String inputFilePath, String outputFilePath) {
        this.fileUtility.moveFile(inputFilePath, outputFilePath);
    }

    /**
     * Deletes all files from the specified folder, but leaves the folder intact.
     * 
     * @param folderPath the path of the folder to wipe.
     */
    public void wipeFolderContents(String folderPath) {
        this.fileUtility.wipeFolderContents(folderPath);
    }

    /**
     * Retrieves the contents of a specified directory.
     * 
     * @param directoryPath the path of the directory to scan.
     * @return a list of file paths in the directory.
     */
    public List<String> getDirectoryContents(String directoryPath) {
        return this.fileUtility.getDirectoryContents(directoryPath);
    }

    /**
     * Filters the contents of a directory based on the specified file types.
     * 
     * @param types the file extensions to filter by (e.g., "zip", "txt").
     * @param directoryContentPaths the list of file paths to filter.
     * @return a list of file paths that match one of the specified types.
     */
    public List<String> filterDirectoryContentByTypes(String[] types, List<String> directoryContentPaths) {
        return this.fileUtility.filterDirectoryContentByTypes(types, directoryContentPaths);
    }

    /**
     * Retrieves the file extension from a given file path.
     * 
     * @param inputPath the path to the file.
     * @return the file extension (e.g., "txt"), or an empty string if no extension is found.
     */
    public String getFileExtensionFromPath(String inputPath) {
        return this.fileUtility.getFileExtensionFromPath(inputPath);
    }

    /**
     * Retrieves the paths of all subdirectories within a specified directory.
     * 
     * @param directoryPath the path of the directory to scan.
     * @return a list of subdirectory paths, or an empty list if no subdirectories are found.
     */
    public List<String> getSubdirectoryPaths(String directoryPath) {
        return this.fileUtility.getSubdirectoryPaths(directoryPath);
    }

    /**
     * Retrieves all files, including those in subdirectories, within a specified directory.
     * 
     * @param directoryPath the path of the directory to scan.
     * @return a list of file paths within the directory and its subdirectories.
     */
    public List<String> getSubdirectoryFiles(String directoryPath) {
        return this.fileUtility.getSubdirectoryFiles(directoryPath);
    }

    // **Methods for Extracting Archives**

    /**
     * Extracts the contents of an archive to the specified destination folder.
     * 
     * @param archivePath the path to the archive file.
     * @param destinationPath the destination folder where the extracted files will be placed.
     * @return the path to the folder where the files were extracted.
     * @throws IOException if an error occurs during extraction.
     */
    public String extract(String archivePath, String destinationPath) throws IOException {
        String fileExtension = getFileExtensionFromPath(archivePath);
        Extractor extractor = extractors.get(fileExtension);
        String uncompressedPath;

        if (extractor != null) {
            uncompressedPath = extractor.extract(archivePath, destinationPath);
        } else {
            throw new IllegalArgumentException("Unsupported archive format: " + fileExtension);
        }

        return uncompressedPath;
    }

    /**
     * Extracts the contents of an archive to a default destination folder.
     * 
     * @param archivePath the path to the archive file.
     * @return the path to the folder where the files were extracted.
     * @throws IOException if an error occurs during extraction.
     */
    public String extract(String archivePath) throws IOException {
        String fileExtension = getFileExtensionFromPath(archivePath);
        Extractor extractor = extractors.get(fileExtension);
        String uncompressedPath;

        if (extractor != null) {
            uncompressedPath = extractor.extract(archivePath);
        } else {
            throw new IllegalArgumentException("Unsupported archive format: " + fileExtension);
        }

        return uncompressedPath;
    }
}
