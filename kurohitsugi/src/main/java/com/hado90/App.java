package com.hado90;

import java.util.List;
import java.util.Map;

import com.hado90.fileMgt.FileManager;
import com.hado90.fileMgt.extractor.ExtractorZip;
import com.hado90.submissionMgt.Submission;
import com.hado90.submissionMgt.SubmissionManager;


public class App {
    public static void main(String[] args) throws Exception {
        // extract big zip
        // create a file manager
        System.out.println("Creating File manager");
        FileManager fileManager = new FileManager(new String[] { "zip" });

        // get an appropriate extractor
        System.out.println("Getting appropriate extractor");
        ExtractorZip extractorZip = (ExtractorZip) fileManager.getExtractor("zip");

        // get zip location of ALL submissions
        System.out.println("Extracting all submissions");
        String locationOfSubmissions = extractorZip.extract(
                "src\\main\\java\\com\\hado90\\temp\\submissions\\Student_Submissions.zip",
                "src\\main\\java\\com\\hado90\\temp\\submissions\\");
        System.out.println("All submissions extracted to: " + locationOfSubmissions);// confirm location

        // traverse directory and extract all the zips in that directory
        System.out.println("Getting contents of submissions folder");
        List<String> list = fileManager.fileUtility.getDirectoryContents(locationOfSubmissions);
        System.out.println(list);// confirm contents

        // filter valid compressed formats
        System.out.println("Filtering valid compressed formats");
        List<String> validList = fileManager.fileUtility.filterDirectoryContentByTypes(new String[] { "zip" }, list);
        System.out.println(validList);// confirm contents

        // use this to add to the submissions manager
        SubmissionManager submissionManager = new SubmissionManager();
        submissionManager.bulkAddSubmissions(validList);
        // submissionManager.getAllSubmissions();

        // extract each submission with status submitted
        Map<String, Submission> submissions = submissionManager.filterSubmissionsByStatus("SUBMITTED");
        submissions.forEach((submissionId, submission) -> {
            System.out.println("Extracting submission: " + submissionId);
            try {
                String extractedPath = extractorZip.extract(submission.getSubmissionPath());
                submission.setSubmissionPath(extractedPath);
            } catch (Exception e) {
                // TODO: handle exception
            }
            System.out.println("Submission extracted to: " + submission.getSubmissionPath());
        });
    }
}