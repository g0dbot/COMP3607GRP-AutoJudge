package com.hado90.judge;

import java.io.File;

import com.hado90.fileMgt.FileManager;
import com.hado90.judge.compiler.JavaCompilerUtil;
import com.hado90.judge.testRunner.JavaTestRunner;
import com.hado90.pdfGenerator.PDFGenerator;
import com.hado90.submissionMgt.Submission;

/**
 * Abstract base class that provides the structure for judging Java programming assignments.
 * This class defines the general flow of judging submissions, including preparation of test criteria,
 * compiling code, running tests, reporting results, and cleaning up resources.
 */
public abstract class Judge {

    protected FileManager fileManager;
    protected JavaCompilerUtil javaCompilerUtil;
    protected JavaTestRunner javaTestRunner;
    protected PDFGenerator pdfGenerator;

    /**
     * Constructs a new Judge instance with the necessary utilities for judging Java assignments.
     * 
     * @param fileManager the {@link FileManager} used for handling file operations.
     * @param javaCompilerUtil the {@link JavaCompilerUtil} used for compiling Java code.
     * @param javaTestRunner the {@link JavaTestRunner} used for running Java tests.
     * @param pdfGenerator the {@link PDFGenerator} used for generating PDF reports.
     */
    public Judge(FileManager fileManager, JavaCompilerUtil javaCompilerUtil,
                 JavaTestRunner javaTestRunner, PDFGenerator pdfGenerator) {
        // Sets the provided instances to be used for judging assignments
        this.fileManager = fileManager;
        this.javaCompilerUtil = javaCompilerUtil;
        this.javaTestRunner = javaTestRunner;
        this.pdfGenerator = pdfGenerator;
    }

    /**
     * Prepares the test criteria by extracting and compiling the test classes from a compressed file.
     * 
     * @param compressedTestFilePath the path to the compressed test file (ZIP format).
     */
    public void prepareCriteria(String compressedTestFilePath) {
        // Takes the path to the test file, unzips, processes it, compiles, and stores in the temp/tests directory
    }

    /**
     * Prepares the student's submission for judging by compiling the submission code.
     * 
     * @param submission the {@link Submission} object representing the student's submission.
     */
    public void prepareSubmission(Submission submission) {
        // Takes the submission path, extracts it, compiles, and stores the compiled classes in the temp/submissions directory
    }

    /**
     * Judges the student's submission by running the compiled tests and capturing the results.
     * 
     * @param submission the {@link Submission} object representing the student's submission.
     */
    public void judgeSubmission(Submission submission) {
        // Loads test and submission classes, runs tests, and captures results in the student's submission
    }

    /**
     * Reports the results of the student's submission, typically by generating a PDF report.
     * This method is a placeholder and can be implemented to generate result reports for students.
     * 
     * @param studentSubmission the {@link Submission} object representing the student's submission.
     */
    public void reportSubmissionResults(Submission studentSubmission) {
        // Creates result reporting, generates the PDF
    }

    /**
     * Cleans up temporary files related to the student's submission by wiping the contents of the temp/submissions directory.
     */
    public void cleanupSubmission() {
        this.fileManager.wipeFolderContents("kurohitsugi/src/main/java/com/hado90/temp/submissions");
    }

    /**
     * Cleans up temporary files related to the test criteria by wiping the contents of the temp/tests directory.
     */
    public void cleanupJudge() {
        this.fileManager.wipeFolderContents("kurohitsugi/src/main/java/com/hado90/temp/tests");
    }

    // Accessor methods for the utility objects

    /**
     * Returns the {@link FileManager} used for handling file operations.
     * 
     * @return the {@link FileManager} instance.
     */
    public FileManager getFileManager() {
        return this.fileManager;
    }

    /**
     * Returns the {@link JavaCompilerUtil} used for compiling Java code.
     * 
     * @return the {@link JavaCompilerUtil} instance.
     */
    public JavaCompilerUtil getJavaCompilerUtil() {
        return this.javaCompilerUtil;
    }

    /**
     * Returns the {@link JavaTestRunner} used for running Java tests.
     * 
     * @return the {@link JavaTestRunner} instance.
     */
    public JavaTestRunner getJavaTestRunner() {
        return this.javaTestRunner;
    }
}
