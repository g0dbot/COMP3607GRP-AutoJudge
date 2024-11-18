package com.hado90.judge.compiler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.hado90.fileMgt.FileManager;


public class JavaFileProcessor {
    private final FileManager fileManager;

    JavaFileProcessor(FileManager fileManager) { this.fileManager = fileManager; }

    public List<String> findJavaFilesinDirectory(String folderPath) throws IOException {
        List<String> javaFiles = new ArrayList<>();
        List<String> allFiles = fileManager.getSubdirectoryFiles(folderPath);
    
        System.out.println("All files in directory: " + allFiles);  // Debug output
    
        for (String filePath : allFiles) {
            if (filePath.endsWith(".java")) {
                javaFiles.add(filePath);
            }
        }
    
        if (javaFiles.isEmpty()) {
            System.out.println("No .java files found in directory: " + folderPath);
        } else {
            System.out.println("Java files found: " + javaFiles);
        }
    
        return javaFiles;
    }
    


    public void processFile(String javaFilePath, String packageName) throws IOException{
        String sourceCode = new String (Files.readAllBytes(Paths.get(javaFilePath)));
        sourceCode = removePackageDeclaration(sourceCode);
        sourceCode = "package " + packageName + ";" + System.lineSeparator() + sourceCode;
        Files.write(Paths.get(javaFilePath), sourceCode.getBytes());
    }

    public String removePackageDeclaration(String sourceCode) throws IOException {        
        // Remove the package declaration from the source code
        if (sourceCode.startsWith("package ")) {
            int endOfPackageDeclaration = sourceCode.indexOf(";");
            if (endOfPackageDeclaration != -1) {
                sourceCode = sourceCode.substring(endOfPackageDeclaration + 1).trim();
            }
        }
        return sourceCode;
    }
}
