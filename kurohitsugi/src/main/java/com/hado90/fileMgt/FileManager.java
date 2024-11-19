package com.hado90.fileMgt;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.hado90.fileMgt.extractor.Extractor;
import com.hado90.fileMgt.extractor.ExtractorZip;
import com.hado90.fileMgt.extractor.FileUtility;

public class FileManager {
    public final FileUtility fileUtility;
    private final HashMap<String, Extractor> extractors;

    public FileManager(String[] extractorTypes) {
        this.fileUtility = new FileUtility();
        this.extractors = new HashMap<>();

        //init an register extractors
        for (String type : extractorTypes) {
            Extractor extractor = createExtractorType(type);
            if(extractor != null){ registerExtractor(type, extractor); } 
            else { System.out.println("Invalid extractor type: " + type); }            
        }
    }

    public Extractor getExtractor(String type) {
        if (extractors.containsKey(type)) {return extractors.get(type);}
        return null;
    }

    public void registerExtractor(String type, Extractor extractor) { this.extractors.put(type, extractor); }

    private Extractor createExtractorType(String type) {
        if (type.equalsIgnoreCase("zip")) { return new ExtractorZip(fileUtility); }
        return null;
    }

    //FACADE
    //FILE UTILS
    //create folder in a specified dir
    public void createFolder(String folderPath){ this.fileUtility.createFolder(folderPath); }
    public boolean fileOrDirectoryExists(String filePath) { return this.fileUtility.fileOrDirectoryExists(filePath); }
    public void deleteFile(String filePath) { this.fileUtility.deleteFile(filePath); }
    public void moveFile(String inputFilePath, String outputFilePath) { this.fileUtility.moveFile(inputFilePath, outputFilePath); }  
    public void wipeFolderContents(String folderPath) { this.fileUtility.wipeFolderContents(folderPath); }
    public List<String> getDirectoryContents (String directoryPath){ return this.fileUtility.getDirectoryContents(directoryPath);}
    public List<String> filterDirectoryContentByTypes(String[] types, List<String> directoryContentPaths) { return this.fileUtility.filterDirectoryContentByTypes(types, directoryContentPaths);}
    public String getFileExtensionFromPath(String inputPath) { return this.fileUtility.getFileExtensionFromPath(inputPath); }
    public List<String> getSubdirectoryPaths(String directoryPath) { return this.fileUtility.getSubdirectoryPaths(directoryPath); }
    public List<String> getSubdirectoryFiles(String directoryPath) { return this.fileUtility.getSubdirectoryFiles(directoryPath); }

    //EXTRACTORS
    public void extract(String archivePath, String destinationPath) throws IOException {
        String fileExtension = getFileExtensionFromPath(archivePath);
        Extractor extractor = extractors.get(fileExtension);
        if (extractor != null) {
            extractor.extract(archivePath, destinationPath);
        } else {
            throw new IllegalArgumentException("Unsupported archive format: " + fileExtension);
        }
    }

    public void extract(String archivePath) throws IOException {
        String fileExtension = getFileExtensionFromPath(archivePath);
        Extractor extractor = extractors.get(fileExtension);
        if (extractor != null) {
            extractor.extract(archivePath);
        } else {
            throw new IllegalArgumentException("Unsupported archive format: " + fileExtension);
        }
    }
}