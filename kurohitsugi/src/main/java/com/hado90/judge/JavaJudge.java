package com.hado90.judge;

import java.io.File;
import java.util.HashMap;

import com.hado90.fileMgt.FileManager;
import com.hado90.judge.compiler.JavaCompilerUtil;
import com.hado90.judge.testRunner.JavaTestRunner;
import com.hado90.pdfGenerator.PDFGenerator;
import com.hado90.submissionMgt.Submission;

/**
 * The JavaJudge class extends the {@link Judge} class and provides functionality
 * for judging Java programming assignments. It involves preparing test criteria,
 * compiling test and submission code, running tests, and generating results.
 */
public class JavaJudge extends Judge {

    /**
     * Constructs a new JavaJudge instance.
     * 
     * @param fileManager the {@link FileManager} used for handling files.
     * @param javaCompilerUtil the {@link JavaCompilerUtil} used for compiling Java code.
     * @param javaTestRunner the {@link JavaTestRunner} used for running Java tests.
     * @param pdfGenerator the {@link PDFGenerator} used for generating PDF reports.
     */
    public JavaJudge(FileManager fileManager, JavaCompilerUtil javaCompilerUtil,
                     JavaTestRunner javaTestRunner, PDFGenerator pdfGenerator) {
        super(fileManager, javaCompilerUtil, javaTestRunner, pdfGenerator);
    }

    /**
     * Prepares the criteria for judging the Java assignment by extracting and compiling
     * the test classes from the specified compressed test file.
     * 
     * @param compressedTestFilePath the path to the compressed test file (ZIP file).
     */
    @Override
    public void prepareCriteria(String compressedTestFilePath) {
        FileManager fileManager = getFileManager();
        String uncompressedTestFilePath = null;
        
        try {
            uncompressedTestFilePath = fileManager.extract(compressedTestFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(uncompressedTestFilePath);

        String testOutputPath = "kurohitsugi" + File.separator + "src" + File.separator + "main" + File.separator
                + "java";
        String testPackageName = "com.hado90.temp.tests";
        JavaCompilerUtil javaCompilerUtil = getJavaCompilerUtil();

        try {
            javaCompilerUtil.compileClassesWithPackage(uncompressedTestFilePath, testOutputPath, testPackageName);
        } catch (Exception e) {
            System.out.println("Error while compiling test class: " + e.getMessage());
        }

        fileManager.deleteFolder(uncompressedTestFilePath);
    }

    /**
     * Prepares the submission for judging by compiling the student's submission code.
     * 
     * @param submission the {@link Submission} object representing the student's submission.
     */
    @Override
    public void prepareSubmission(Submission submission) {
        JavaCompilerUtil javaCompilerUtil = getJavaCompilerUtil();
        String submissionPath = submission.getSubmissionPath();
        String submissionOutputPath = "kurohitsugi" + File.separator + "src" + File.separator + "main" + File.separator
                + "java";
        String submissionPackageName = "com.hado90.temp.submissions";

        try {
            javaCompilerUtil.compileClassesWithPackage(submissionPath, submissionOutputPath, submissionPackageName);
        } catch (Exception e) {
            System.out.println("Error while compiling submission class: " + e.getMessage());
        }
    }

    /**
     * Judges the student's submission by running the compiled tests and storing the results.
     * 
     * @param submission the {@link Submission} object representing the student's submission.
     */
    @Override
    public void judgeSubmission(Submission submission) {
        JavaTestRunner javaTestRunner = getJavaTestRunner();
        try {
            javaTestRunner.loadRunner();
        } catch (Exception e) {
            System.out.println("Error while loading runner: " + e.getMessage());
        }
        
        HashMap<String, HashMap<String, String>> results = javaTestRunner.run();
        submission.setSubmissionResults(results);
    }

    /**
     * Reports the results of the student's submission, typically by generating a PDF report.
     * This method is currently a placeholder for the report generation process.
     * 
     * @param studentSubmission the {@link Submission} object representing the student's submission.
     */
    @Override
    public void reportSubmissionResults(Submission studentSubmission) {
        // Placeholder for result reporting, including PDF generation
        // Creates result reporting and generates the PDF.
    }

    /**
     * Main method that demonstrates the entire judging flow for a Java programming assignment.
     * It prepares the criteria, compiles and judges the student's submission, and cleans up.
     * 
     * @param args the command-line arguments.
     * @throws Exception if an error occurs during the judging process.
     */
    public static void main(String[] args) throws Exception {
        FileManager fileManager = new FileManager(new String[] {"zip"});
        JavaCompilerUtil javaCompilerUtil = new JavaCompilerUtil(fileManager);
        JavaTestRunner javaTestRunner = new JavaTestRunner();
        PDFGenerator pdfGenerator = new PDFGenerator();

        JavaJudge judge = new JavaJudge(fileManager, javaCompilerUtil, javaTestRunner, pdfGenerator);
        
        // Start of the flow
        judge.prepareCriteria("C:\\Users\\g0dbot\\Desktop\\TESTINGAIZEN\\classesandtests\\tests.zip");

        // Iterate over each submission
        Submission submission = new Submission("C:\\Users\\g0dbot\\Desktop\\TESTINGAIZEN\\classesandtests\\testsubjects");
        judge.prepareSubmission(submission);
        judge.judgeSubmission(submission);
        judge.cleanupSubmission();

        // After the entire process
        judge.cleanupJudge();
    }
}

