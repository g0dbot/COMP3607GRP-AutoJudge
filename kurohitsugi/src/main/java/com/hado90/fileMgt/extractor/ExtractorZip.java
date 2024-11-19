package com.hado90.fileMgt.extractor;

import java.io.*;
import java.util.zip.*;

public class ExtractorZip implements Extractor {

    private FileUtility fileUtility;

    public ExtractorZip(FileUtility fileUtility) { this.fileUtility = fileUtility; }

    public String extract(String inputPath) throws IOException {
        //check file type
        if(!isValidCompressedFileType(inputPath)) { return null; }

        //get compressed file name
        String compressedFileName = getCompressedFileName(inputPath);
        System.out.println(compressedFileName);

        //set output path
        String outputPath = inputPath.substring(0, inputPath.lastIndexOf('.'));

        //create output folder
        fileUtility.createFolder(outputPath);

        //extract files to that folder
        try (ZipInputStream zipStream = new ZipInputStream(new FileInputStream(inputPath))) {
            byte[] buffer = new byte[1024];
            int len;
            ZipEntry entry;

            while ((entry = zipStream.getNextEntry()) != null) {
                File filePath = new File(outputPath, entry.getName());

                //if dir exist create folder
                if (entry.isDirectory()) { fileUtility.createFolder(filePath.getAbsolutePath()); } 
                else {
                    //ignore subzips
                    //IMPOTRATNT suffix changes with each type of extractor
                    if (entry.getName().endsWith(".zip")) { copyCompressedFile(zipStream, filePath); } 
                    else {
                        //else is normal file, extract
                        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
                            while ((len = zipStream.read(buffer)) != -1) {
                                fileOutputStream.write(buffer, 0, len);
                            }
                        }
                    }
                }

                //close stream
                zipStream.closeEntry();
            }
        }

        //return the output path
        return outputPath;
    }

    //overloaded method
    public String extract(String inputPath, String outputPath) throws IOException {
        //check file type
        if(!isValidCompressedFileType(inputPath)) { return null; }

        //get compressed file name
        String compressedFileName = getCompressedFileName(inputPath);
        System.out.println(compressedFileName);

        //set output path
        outputPath = outputPath + File.separator + compressedFileName;

        //create output folder
        fileUtility.createFolder(outputPath);

        //extract files to that folder
        try (ZipInputStream zipStream = new ZipInputStream(new FileInputStream(inputPath))) {
            byte[] buffer = new byte[1024];
            int len;
            ZipEntry entry;

            while ((entry = zipStream.getNextEntry()) != null) {
                File filePath = new File(outputPath, entry.getName());

                //if dir exist create folder
                if (entry.isDirectory()) { fileUtility.createFolder(filePath.getAbsolutePath()); } 
                else {
                    //ignore subzips
                    //IMPOTRATNT suffix changes with each type of extractor
                    if (entry.getName().endsWith(".zip")) { copyCompressedFile(zipStream, filePath); } 
                    else {
                        //else is normal file, extract
                        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
                            while ((len = zipStream.read(buffer)) != -1) {
                                fileOutputStream.write(buffer, 0, len);
                            }
                        }
                    }
                }

                //close stream
                zipStream.closeEntry();
            }
        }

        //return the output path
        return outputPath;
    }    

    //copy zip from zipinputStream to the output folder without extracting
    private void copyCompressedFile(ZipInputStream zipin, File filePath) throws IOException {
        byte[] buffer = new byte[1024];
        int len;

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            while ((len = zipin.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
        }
    }

    //return the compressed file name
    public String getCompressedFileName(String input) {
        return new File(input).getName().replaceFirst("[.][^.]+$", "");
    }

    public boolean isValidCompressedFileType(String input) { return input.endsWith("zip"); }
}
