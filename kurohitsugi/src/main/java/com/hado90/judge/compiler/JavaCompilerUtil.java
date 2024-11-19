package com.hado90.judge.compiler;

import com.hado90.fileMgt.FileManager;

import java.io.*;
import java.util.*;

public class JavaCompilerUtil {

    private final FileManager fileManager;
    private final JavaCompilerService javaCompilerService;
    private final JavaFileProcessor javaFileProcessor;

    public JavaCompilerUtil(FileManager fileManager) {
        this.fileManager = fileManager;
        this.javaCompilerService = new JavaCompilerService();
        this.javaFileProcessor = new JavaFileProcessor(fileManager);
    }
    
    public void compileClassesWithPackage(String folderPath, String outputPath, String packageName) throws IOException {
        //ensure inp folder exists
        if(!fileManager.fileOrDirectoryExists(folderPath)) { throw new FileNotFoundException("Folder not found: " + folderPath); }

        //get all java files in folder
        List<String> javaFiles = javaFileProcessor.findJavaFilesinDirectory(folderPath);

        //bundle compile
        List<String> compileOptions = new ArrayList<>();
        for (String javaFilePath : javaFiles) {
            System.out.println("Adding java file to compiler: " + javaFilePath);
            javaFileProcessor.processFile(javaFilePath, packageName);
            compileOptions.add(javaFilePath);
        }
        
        //compile options
        compileOptions.add("-d");
        compileOptions.add(outputPath);

        //compile java files
        javaCompilerService.compile(compileOptions);        
    }

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
