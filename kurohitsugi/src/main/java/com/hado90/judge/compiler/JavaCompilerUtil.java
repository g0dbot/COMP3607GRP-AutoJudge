package com.hado90.judge.compiler;

import com.hado90.fileMgt.FileManager;

import java.io.*;
import java.util.*;

/**
 * Utility class for compiling Java classes from a given folder path, with the option to specify the package name.
 * It uses the {@link JavaCompilerService} to compile Java files and {@link JavaFileProcessor} to handle file processing.
 */
public class JavaCompilerUtil {

    private final FileManager fileManager;
    private final JavaCompilerService javaCompilerService;
    private final JavaFileProcessor javaFileProcessor;

    /**
     * Constructor for the JavaCompilerUtil class.
     * Initializes the necessary services for file management, Java compilation, and Java file processing.
     *
     * @param fileManager the {@link FileManager} used for file and directory operations.
     */
    public JavaCompilerUtil(FileManager fileManager) {
        this.fileManager = fileManager;
        this.javaCompilerService = new JavaCompilerService();
        this.javaFileProcessor = new JavaFileProcessor(fileManager);
    }

    /**
     * Compiles Java classes from the specified folder, adds the necessary package name, and outputs compiled classes 
     * to the given output path.
     * 
     * This method ensures the input folder exists, retrieves all Java files, processes each file to include the 
     * specified package, and then compiles them using the {@link JavaCompilerService}.
     * 
     * @param folderPath the path to the folder containing Java files to be compiled.
     * @param outputPath the output path where the compiled classes will be stored.
     * @param packageName the package name to be added to each Java file during compilation.
     * @throws IOException if an I/O error occurs during file operations or compilation.
     * @throws FileNotFoundException if the specified folder path does not exist.
     */
    public void compileClassesWithPackage(String folderPath, String outputPath, String packageName) throws IOException {
        // Ensure the input folder exists
        if (!fileManager.fileOrDirectoryExists(folderPath)) {
            throw new FileNotFoundException("Folder not found: " + folderPath);
        }

        // Get all Java files in the folder
        List<String> javaFiles = javaFileProcessor.findJavaFilesinDirectory(folderPath);

        // Prepare compile options
        List<String> compileOptions = new ArrayList<>();
        for (String javaFilePath : javaFiles) {
            System.out.println("Adding java file to compiler: " + javaFilePath);
            javaFileProcessor.processFile(javaFilePath, packageName); // Process files with the given package name
            compileOptions.add(javaFilePath);
        }

        // Add the output path to compile options
        compileOptions.add("-d");
        compileOptions.add(outputPath);

        // Compile Java files using the JavaCompilerService
        javaCompilerService.compile(compileOptions);
    }

    /**
     * Main method for testing the JavaCompilerUtil class. It demonstrates the compilation of both test and student Java files.
     * 
     * @param args command line arguments (not used in this implementation).
     */
    public static void main(String[] args) {
        try {
            // Initialize FileManager, JavaCompilerService, and JavaFileProcessor
            FileManager fileManager = new FileManager(new String[] {"zip"}); // Assuming FileManager is available
            JavaCompilerUtil javaCompilerUtil = new JavaCompilerUtil(fileManager);
        
            // Specify the source folder and output path for the test class
            String testSourceFolderPath = "C:\\Users\\g0dbot\\Desktop\\TESTINGAIZEN\\classesandtests\\tests"; // Path to the folder containing test files
            String testOutputPath = "kurohitsugi" + File.separator + "src" + File.separator + "main" + File.separator + "java"; // Target output path for test files
            String testPackageName = "com.hado90.temp.tests"; // Package name for test files
        
            // Compile the test class with the package information
            javaCompilerUtil.compileClassesWithPackage(testSourceFolderPath, testOutputPath, testPackageName);
        
            // Compile all Java files in the student folder
            String submissionSourceFolder = "C:\\Users\\g0dbot\\Desktop\\TESTINGAIZEN\\classesandtests\\testsubjects"; // Path to student folder
            String submissionOutputPath = "kurohitsugi" + File.separator + "src" + File.separator + "main" + File.separator + "java"; // Target output path for student files
            String submissionPackageName = "com.hado90.temp.submissions"; // Package name for student files
        
            // Compile the student's Java files
            javaCompilerUtil.compileClassesWithPackage(submissionSourceFolder, submissionOutputPath, submissionPackageName);
        
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
