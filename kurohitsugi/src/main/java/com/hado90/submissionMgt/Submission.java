package com.hado90.submissionMgt;

import java.io.File;
import java.util.HashMap;

public class Submission {
    private String submissionId;
    private String submissionFirstName;
    private String submissionLastName;
    private String submissionPath;
    private String submissionStatus;
    private String submissionComment;

    private HashMap<String, HashMap<String, String>> testResults;

    public Submission(String submissionPath) {
        this.testResults = new HashMap<>();
        setSubmissionPath(submissionPath);
        splitSetSubmissionDetails(submissionPath);
    }

    // accessors
    public String getSubmissionPath() { return submissionPath; }
    public String getSubmissionStatus() { return submissionStatus; }
    public String getSubmissionComment() { return submissionComment; }
    public String getSubmissionId() {return submissionId; }
    public String getSubmissionFirstName() { return submissionFirstName; }
    public String getSubmissionLastName() { return submissionLastName; }
    public HashMap<String, HashMap<String, String>> getTestResults() { return testResults; }

    // mutators
    public void setSubmissionId(String submissionId) { this.submissionId = submissionId; }
    public void setSubmissionFirstName(String submissionFirstName) { this.submissionFirstName = submissionFirstName; }
    public void setSubmissionLastName(String submissionLastName) { this.submissionLastName = submissionLastName; }
    public void setSubmissionPath(String submissionPath) { this.submissionPath = submissionPath; }
    public void setSubmissionStatus(String submissionStatus) { this.submissionStatus = submissionStatus; }
    public void setSubmissionComment(String submissionComment) { this.submissionComment = submissionComment; }
    public void setSubmissionResults(HashMap<String, HashMap<String, String>> testResults) { this.testResults = testResults; }

    public boolean validateSubmissionNamingConvention(String firstName, String lastName, String submissionId) {
        if (firstName == null || firstName.isEmpty()) { return false; }
        if (lastName == null || lastName.isEmpty()) { return false; }
        try { Integer.parseInt(submissionId); } 
        catch (NumberFormatException e) { return false; }
        return true;
    }

    public String extractFileNameWithoutExtension(String filePath) { return (new File(filePath).getName()); }

    public void splitSetSubmissionDetails(String submissionString) {
        String submissionDetailString = extractFileNameWithoutExtension(submissionString);

        submissionDetailString = submissionDetailString.replaceFirst("[.][^.]+$", "");

        String[] submissionArray = submissionDetailString.split("_");
        if (submissionArray.length < 3) {
            System.out.println("Invalid naming convention: " + submissionDetailString);
            setSubmissionId(submissionString);
            setSubmissionStatus("INVALID");
            setSubmissionComment("Invalid naming convention String: " + submissionString);
        }

        else {
            String firstName = submissionArray[0];
            String lastName = submissionArray[1];
            String submissionId = submissionArray[2];

            if (!validateSubmissionNamingConvention(firstName, lastName, submissionId)) {
                System.out.println("Invalid submission string: " + submissionString);

                setSubmissionId(submissionString);
                setSubmissionStatus("INVALID");
                setSubmissionComment("Invalid Submission String: " + submissionString);
            } else {
                setSubmissionFirstName(firstName);
                setSubmissionLastName(lastName);
                setSubmissionId(submissionId);
                setSubmissionStatus("SUBMITTED");
            }
        }
    }
}
