package com.hado90.judge;

import java.io.File;
import java.util.HashMap;

import com.hado90.fileMgt.FileManager;
import com.hado90.judge.compiler.JavaCompilerUtil;
import com.hado90.judge.testRunner.JavaTestRunner;
import com.hado90.pdfGenerator.PDFGenerator;
import com.hado90.submissionMgt.Submission;

public class JavaJudge extends Judge {

    public JavaJudge(FileManager fileManager, JavaCompilerUtil javaCompilerUtil,
            JavaTestRunner javaTestRunner, PDFGenerator pdfGenerator) {
        super(fileManager, javaCompilerUtil, javaTestRunner, pdfGenerator);
    }

    @Override
    public void prepareCriteria(String compressedTestFilePath) {
        FileManager fileManager = getFileManager();
        String uncompressedTestFilePath = null;
        
        try { uncompressedTestFilePath = fileManager.extract(compressedTestFilePath); }
        catch (Exception e) { e.printStackTrace(); };

        System.out.println(uncompressedTestFilePath);

        String testOutputPath = "kurohitsugi" + File.separator + "src" + File.separator + "main" + File.separator
                + "java";
        String testPackageName = "com.hado90.temp.tests";
        JavaCompilerUtil javaCompilerUtil = getJavaCompilerUtil();

        try { javaCompilerUtil.compileClassesWithPackage(uncompressedTestFilePath, testOutputPath, testPackageName); } 
        catch (Exception e) { System.out.println("Error while compiling test class: " + e.getMessage());}

        fileManager.deleteFolder(uncompressedTestFilePath);
    }

    @Override
    public void prepareSubmission(Submission submission) {
        JavaCompilerUtil javaCompilerUtil = getJavaCompilerUtil();
        String submissionPath = submission.getSubmissionPath();
        String submissionOutputPath = "kurohitsugi" + File.separator + "src" + File.separator + "main" + File.separator
                + "java";
        String submissionPackageName = "com.hado90.temp.submissions";

        try { javaCompilerUtil.compileClassesWithPackage(submissionPath, submissionOutputPath, submissionPackageName); }
        catch (Exception e) { System.out.println("Error while compiling test class: " + e.getMessage()); }
    }

    @Override
    public void judgeSubmission(Submission submission) {
        JavaTestRunner javaTestRunner = getJavaTestRunner();
        try { javaTestRunner.loadRunner(); } 
        catch (Exception e) { System.out.println("Error while loading runner: " + e.getMessage()); }
        HashMap<String, HashMap<String, String>> results = javaTestRunner.run();
        submission.setSubmissionResults(results);
        submission.calculateScore();
    }

    @Override
    public void reportSubmissionResults(Submission studentSubmission) {
        // creates result reporting
        // generates the pdf
        PDFGenerator pdfGenerator = this.pdfGenerator;
        pdfGenerator.generatePDF(studentSubmission,studentSubmission.getTestResults());
        
    }

    public static void main(String[] args) throws Exception {
        FileManager fileManager = new FileManager(new String[] {"zip"});
        JavaCompilerUtil javaCompilerUtil = new JavaCompilerUtil(fileManager);
        JavaTestRunner javaTestRunner = new JavaTestRunner();
        PDFGenerator pdfGenerator = new PDFGenerator();

        JavaJudge judge = new JavaJudge(fileManager, javaCompilerUtil, javaTestRunner, pdfGenerator);
        
        //start of the flow
        judge.prepareCriteria("C:\\Users\\g0dbot\\Desktop\\TESTINGAIZEN\\classesandtests\\tests.zip");

        //iterate each submission here
        Submission submission = new Submission("C:\\Users\\g0dbot\\Desktop\\TESTINGAIZEN\\classesandtests\\testsubjects");
        judge.prepareSubmission(submission);
        judge.judgeSubmission(submission);
        judge.cleanupSubmission();

        //after entire process
        judge.cleanupJudge();
    }
}
