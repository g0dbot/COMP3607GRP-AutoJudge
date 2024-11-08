package com.comp3607.models;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JobScheduler {
    private List<String> zipJobs;

    public JobScheduler() {
        this.zipJobs = new ArrayList<>();
    }

    public void addJob(String zipFilePath) {
        zipJobs.add(zipFilePath);
    }

    public void processJobs(String outputDirectory) {
        Iterator<String> iterator = zipJobs.iterator();

        while (iterator.hasNext()) {
            String zipFilePath = iterator.next();

            try {
                System.out.println("Obtaining Zip file: " + zipFilePath);

                String zipName = new File(zipFilePath).getName().replaceFirst("[.][^.]+$", "");
                String outputPath = outputDirectory + File.separator + zipName;

                // Call the static extract method
                ExtractorZip.extract(zipFilePath, outputPath);

            } catch (IOException e) {
                System.err.println("Unable to access Zip File " + zipFilePath + ": " + e.getMessage());
            }
        }
    }

    public void collectZipFiles(String directoryPath) {
        File directory = new File(directoryPath);

        if (!directory.isDirectory()) {
            System.err.println(directoryPath + " is an incorrect directory.");
            return;
        }

        for (File file : directory.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".zip")) {
                addJob(file.getAbsolutePath());
            }
        }
    }
}
