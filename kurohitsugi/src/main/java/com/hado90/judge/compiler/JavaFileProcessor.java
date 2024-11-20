package com.hado90.judge.compiler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.hado90.fileMgt.FileManager;

/**
 * Utility class for processing Java files, including finding Java files in a directory and modifying their package declarations.
 */
public class JavaFileProcessor {
    private final FileManager fileManager;

    /**
     * Constructor for the JavaFileProcessor class.
     * Initializes the processor with the provided {@link FileManager} for handling file operations.
     *
     * @param fileManager the {@link FileManager} used for file and directory operations.
     */
    JavaFileProcessor(FileManager fileManager) { 
        this.fileManager = fileManager; 
    }

    /**
     * Finds all Java files in the specified directory.
     * It retrieves the files from the directory and filters out the ones with a ".java" extension.
     *
     * @param folderPath the path of the folder to search for Java files.
     * @return a list of file paths that end with ".java".
     * @throws IOException if an I/O error occurs while accessing the files in the directory.
     */
    public List<String> findJavaFilesinDirectory(String folderPath) throws IOException {
        List<String> javaFiles = new ArrayList<>();
        List<String> allFiles = fileManager.getSubdirectoryFiles(folderPath);
    
        System.out.println("All files in directory: " + allFiles);  // Debug output
    
        // Filter out files with a .java extension
        for (String filePath : allFiles) {
            if (filePath.endsWith(".java")) {
                javaFiles.add(filePath);
            }
        }
    
        // Output if no Java files are found or display the list of Java files
        if (javaFiles.isEmpty()) {
            System.out.println("No .java files found in directory: " + folderPath);
        } else {
            System.out.println("Java files found: " + javaFiles);
        }
    
        return javaFiles;
    }

    /**
     * Processes a Java file by removing its current package declaration and adding the specified package name.
     * The method reads the file's content, modifies the package declaration, and rewrites the file with the updated source code.
     *
     * @param javaFilePath the path to the Java file to be processed.
     * @param packageName the new package name to be added to the Java file.
     * @throws IOException if an I/O error occurs while reading or writing the file.
     */
    public void processFile(String javaFilePath, String packageName) throws IOException {
        // Read the Java file's source code
        String sourceCode = new String(Files.readAllBytes(Paths.get(javaFilePath)));
        
        // Remove the current package declaration
        sourceCode = removePackageDeclaration(sourceCode);
        
        // Add the new package declaration at the top of the file
        sourceCode = "package " + packageName + ";" + System.lineSeparator() + sourceCode;
        
        // Write the updated source code back to the file
        Files.write(Paths.get(javaFilePath), sourceCode.getBytes());
    }

    /**
     * Removes the package declaration from the provided source code.
     * If the source code contains a package declaration at the beginning, it will be removed.
     *
     * @param sourceCode the Java source code from which the package declaration should be removed.
     * @return the source code without the package declaration.
     * @throws IOException if an I/O error occurs while processing the source code.
     */
    public String removePackageDeclaration(String sourceCode) throws IOException {
        // Check if the source code starts with a package declaration
        if (sourceCode.startsWith("package ")) {
            int endOfPackageDeclaration = sourceCode.indexOf(";");
            if (endOfPackageDeclaration != -1) {
                // Remove the package declaration and return the remaining source code
                sourceCode = sourceCode.substring(endOfPackageDeclaration + 1).trim();
            }
        }
        return sourceCode;
    }
}
