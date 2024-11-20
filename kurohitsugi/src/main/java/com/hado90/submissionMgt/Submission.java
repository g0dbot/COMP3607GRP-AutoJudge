package com.hado90.submissionMgt;

import java.io.File;
import java.util.HashMap;

/**
 * Represents a student's submission for a programming assignment.
 * This class holds details about the submission, such as the student's information, 
 * the submission path, status, and the results of any associated tests.
 */
public class Submission {
    private String submissionId;
    private String submissionFirstName;
    private String submissionLastName;
    private String submissionPath;
    private String submissionStatus;
    private String submissionComment;
    private String score;

    private HashMap<String, HashMap<String, String>> testResults;

    /**
     * Constructor to initialize the submission with a given path.
     * This constructor also processes and sets the submission details based on the file name.
     *
     * @param submissionPath the file path of the submission to be processed
     */
    public Submission(String submissionPath) {
        this.testResults = new HashMap<>();
        setSubmissionPath(submissionPath);
        splitSetSubmissionDetails(submissionPath);
    }

    // Accessors (Getters)
    
    /**
     * Gets the path of the submission.
     *
     * @return the submission path
     */
    public String getSubmissionPath() { 
        return submissionPath; 
    }

    /**
     * Gets the status of the submission (e.g., "SUBMITTED", "INVALID").
     *
     * @return the submission status
     */
    public String getSubmissionStatus() { 
        return submissionStatus; 
    }

    /**
     * Gets the comment associated with the submission.
     *
     * @return the submission comment
     */
    public String getSubmissionComment() { 
        return submissionComment; 
    }

    /**
     * Gets the unique ID of the submission.
     *
     * @return the submission ID
     */
    public String getSubmissionId() { 
        return submissionId; 
    }

    /**
     * Gets the first name of the student who submitted the assignment.
     *
     * @return the student's first name
     */
    public String getSubmissionFirstName() { 
        return submissionFirstName; 
    }

    /**
     * Gets the last name of the student who submitted the assignment.
     *
     * @return the student's last name
     */
    public String getSubmissionLastName() { 
        return submissionLastName; 
    }

    /**
     * Gets the test results for the submission.
     * The results are stored in a nested HashMap where the first key is the test class name 
     * and the second key is the test name, with the value being the test result.
     *
     * @return the test results of the submission
     */
    public HashMap<String, HashMap<String, String>> getTestResults() { 
        return testResults; 
    }

    // Mutators (Setters)

    /**
     * Sets the unique ID for the submission.
     *
     * @param submissionId the unique ID of the submission
     */
    public void setSubmissionId(String submissionId) { 
        this.submissionId = submissionId; 
    }

    /**
     * Sets the first name of the student.
     *
     * @param submissionFirstName the student's first name
     */
    public void setSubmissionFirstName(String submissionFirstName) { 
        this.submissionFirstName = submissionFirstName; 
    }

    /**
     * Sets the last name of the student.
     *
     * @param submissionLastName the student's last name
     */
    public void setSubmissionLastName(String submissionLastName) { 
        this.submissionLastName = submissionLastName; 
    }

    /**
     * Sets the path of the submission file.
     *
     * @param submissionPath the file path of the submission
     */
    public void setSubmissionPath(String submissionPath) { 
        this.submissionPath = submissionPath; 
    }

    /**
     * Sets the status of the submission (e.g., "SUBMITTED", "INVALID").
     *
     * @param submissionStatus the status of the submission
     */
    public void setSubmissionStatus(String submissionStatus) { 
        this.submissionStatus = submissionStatus; 
    }

    /**
     * Sets the comment associated with the submission.
     *
     * @param submissionComment the comment related to the submission
     */
    public void setSubmissionComment(String submissionComment) { 
        this.submissionComment = submissionComment; 
    }

    /**
     * Sets the test results for the submission.
     *
     * @param testResults the test results for the submission
     */
    public void setSubmissionResults(HashMap<String, HashMap<String, String>> testResults) { 
        this.testResults = testResults; 
    }

    /**
     * Validates the naming convention of the submission based on the student's first name, last name, and submission ID.
     * The naming convention must consist of the first name, last name, and a numeric submission ID.
     *
     * @param firstName the first name of the student
     * @param lastName the last name of the student
     * @param submissionId the submission ID to validate
     * @return true if the naming convention is valid, otherwise false
     */
    public boolean validateSubmissionNamingConvention(String firstName, String lastName, String submissionId) {
        if (firstName == null || firstName.isEmpty()) { return false; }
        if (lastName == null || lastName.isEmpty()) { return false; }
        try { 
            Integer.parseInt(submissionId); 
        } catch (NumberFormatException e) { 
            return false; 
        }
        return true;
    }

    /**
     * Extracts the file name without the extension from a given file path.
     *
     * @param filePath the file path to extract the name from
     * @return the file name without the extension
     */
    public String extractFileNameWithoutExtension(String filePath) { 
        return (new File(filePath).getName()); 
    }

    /**
     * Splits the submission details from the file name and sets the relevant submission information.
     * The expected naming convention is "firstName_lastName_submissionId.ext", where:
     * - firstName: the student's first name
     * - lastName: the student's last name
     * - submissionId: the unique submission ID (numeric)
     * If the naming convention is invalid, the status is set to "INVALID" and an error message is provided.
     *
     * @param submissionString the file path or file name representing the submission
     */
    public void splitSetSubmissionDetails(String submissionString) {
        String submissionDetailString = extractFileNameWithoutExtension(submissionString);

        submissionDetailString = submissionDetailString.replaceFirst("[.][^.]+$", "");

        String[] submissionArray = submissionDetailString.split("_");
        if (submissionArray.length < 3) {
            System.out.println("Invalid naming convention: " + submissionDetailString);
            setSubmissionId(submissionString);
            setSubmissionStatus("INVALID");
            setSubmissionComment("Invalid naming convention String: " + submissionString);
        } else {
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
