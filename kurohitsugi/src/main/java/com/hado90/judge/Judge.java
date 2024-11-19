package com.hado90.judge;

import java.io.File;

import com.hado90.fileMgt.FileManager;
import com.hado90.judge.compiler.JavaCompilerUtil;
import com.hado90.judge.testRunner.JavaTestRunner;
import com.hado90.pdfGenerator.PDFGenerator;
import com.hado90.submissionMgt.Submission;

public abstract class Judge {

    private FileManager fileManager;
    private JavaCompilerUtil javaCompilerUtil;
    private JavaTestRunner javaTestRunner;
    private PDFGenerator pdfGenerator;

    public Judge(FileManager fileManager, JavaCompilerUtil javaCompilerUtil,
            JavaTestRunner javaTestRunner, PDFGenerator pdfGenerator) {
        // sets file manager instance
        this.fileManager = fileManager;
        this.javaCompilerUtil = javaCompilerUtil;
        this.javaTestRunner = javaTestRunner;
        this.pdfGenerator = pdfGenerator;
    }

    public void prepareCriteria(String compressedTestFilePath) {
        // takes the path to tests and unzips and processes them
        // processes the test
        // compile and store in temp/tests
    }

    public void prepareSubmission(Submission submission) {
        // takes submission extract path
        // uses path to compile and store in temp/submissions
    }

    public void judgeSubmission(Submission submission) {
        // loads the test classes and submission classes
        // runs the tests
        // captures the result in submission
    }

    public void reportSubmissionResults(Submission studentSubmission) {
        // creates result reporting
        // generates the pdf
    }

    public void cleanupSubmission() { this.fileManager.wipeFolderContents("kurohitsugi/src/main/java/com/hado90/temp/submissions"); }

    public void cleanupJudge() { this.fileManager.wipeFolderContents("kurohitsugi/src/main/java/com/hado90/temp/tests"); }

    // accessors
    public FileManager getFileManager() {
        return this.fileManager;
    }

    public JavaCompilerUtil getJavaCompilerUtil() {
        return this.javaCompilerUtil;
    }

    public JavaTestRunner getJavaTestRunner() {
        return this.javaTestRunner;
    }
}
