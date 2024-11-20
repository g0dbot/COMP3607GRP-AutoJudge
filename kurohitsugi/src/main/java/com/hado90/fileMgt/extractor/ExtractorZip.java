package com.hado90.fileMgt.extractor;

import java.io.*;
import java.util.zip.*;

/**
 * This class implements the {@link Extractor} interface for handling the extraction of ZIP files.
 * It provides methods to extract files from a ZIP archive, either to a default location or a specified output path.
 * It also handles nested ZIP files and ensures that subdirectories are created during the extraction process.
 */
public class ExtractorZip implements Extractor {

    private FileUtility fileUtility;

    /**
     * Constructor for the ExtractorZip class.
     * 
     * @param fileUtility the utility class used for file operations (such as creating folders).
     */
    public ExtractorZip(FileUtility fileUtility) { 
        this.fileUtility = fileUtility; 
    }

    /**
     * Extracts the contents of a ZIP file to a folder with the same name as the compressed file (without extension).
     * 
     * @param inputPath the path to the ZIP file to be extracted.
     * @return the path to the folder where the files were extracted, or null if the file type is not valid.
     * @throws IOException if an I/O error occurs during the extraction process.
     */
    public String extract(String inputPath) throws IOException {
        // Check if the file is a valid compressed file type
        if (!isValidCompressedFileType(inputPath)) { 
            return null; 
        }

        // Get the compressed file name without extension
        String compressedFileName = getCompressedFileName(inputPath);
        System.out.println(compressedFileName);

        // Set output path based on the input path
        String outputPath = inputPath.substring(0, inputPath.lastIndexOf('.'));

        // Create the output folder
        fileUtility.createFolder(outputPath);

        // Extract files to the output folder
        try (ZipInputStream zipStream = new ZipInputStream(new FileInputStream(inputPath))) {
            byte[] buffer = new byte[1024];
            int len;
            ZipEntry entry;

            while ((entry = zipStream.getNextEntry()) != null) {
                File filePath = new File(outputPath, entry.getName());

                // If it's a directory, create the folder
                if (entry.isDirectory()) {
                    fileUtility.createFolder(filePath.getAbsolutePath()); 
                } else {
                    // Ignore nested zip files (they are copied without extraction)
                    if (entry.getName().endsWith(".zip")) { 
                        copyCompressedFile(zipStream, filePath); 
                    } else {
                        // Extract normal files
                        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
                            while ((len = zipStream.read(buffer)) != -1) {
                                fileOutputStream.write(buffer, 0, len);
                            }
                        }
                    }
                }

                // Close the current entry in the ZIP stream
                zipStream.closeEntry();
            }
        }

        // Return the output path
        return outputPath;
    }

    /**
     * Extracts the contents of a ZIP file to a specified output path.
     * 
     * @param inputPath the path to the ZIP file to be extracted.
     * @param outputPath the path where the files should be extracted to.
     * @return the path to the folder where the files were extracted.
     * @throws IOException if an I/O error occurs during the extraction process.
     */
    public String extract(String inputPath, String outputPath) throws IOException {
        // Check if the file is a valid compressed file type
        if (!isValidCompressedFileType(inputPath)) {
            return null; 
        }

        // Get the compressed file name without extension
        String compressedFileName = getCompressedFileName(inputPath);
        System.out.println(compressedFileName);

        // Set output path with the compressed file name
        outputPath = outputPath + File.separator + compressedFileName;

        // Create the output folder
        fileUtility.createFolder(outputPath);

        // Extract files to the output folder
        try (ZipInputStream zipStream = new ZipInputStream(new FileInputStream(inputPath))) {
            byte[] buffer = new byte[1024];
            int len;
            ZipEntry entry;

            while ((entry = zipStream.getNextEntry()) != null) {
                File filePath = new File(outputPath, entry.getName());

                // If it's a directory, create the folder
                if (entry.isDirectory()) {
                    fileUtility.createFolder(filePath.getAbsolutePath());
                } else {
                    // Ignore nested zip files (they are copied without extraction)
                    if (entry.getName().endsWith(".zip")) {
                        copyCompressedFile(zipStream, filePath);
                    } else {
                        // Extract normal files
                        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
                            while ((len = zipStream.read(buffer)) != -1) {
                                fileOutputStream.write(buffer, 0, len);
                            }
                        }
                    }
                }

                // Close the current entry in the ZIP stream
                zipStream.closeEntry();
            }
        }

        // Return the output path
        return outputPath;
    }    

    /**
     * Copies a nested compressed file (ZIP) from the ZipInputStream to the specified file path without extracting its contents.
     * 
     * @param zipin the ZipInputStream to read the nested compressed file from.
     * @param filePath the file path to write the compressed file to.
     * @throws IOException if an I/O error occurs during the copying process.
     */
    private void copyCompressedFile(ZipInputStream zipin, File filePath) throws IOException {
        byte[] buffer = new byte[1024];
        int len;

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            while ((len = zipin.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
        }
    }

    /**
     * Returns the name of the compressed file (without its extension) from the given path.
     * 
     * @param input the path to the compressed file.
     * @return the name of the compressed file without its extension.
     */
    public String getCompressedFileName(String input) {
        return new File(input).getName().replaceFirst("[.][^.]+$", "");
    }

    /**
     * Validates if the given file path points to a valid ZIP file.
     * 
     * @param input the file path to validate.
     * @return true if the file is a valid ZIP file, false otherwise.
     */
    public boolean isValidCompressedFileType(String input) {
        return input.endsWith("zip");
    }
}
