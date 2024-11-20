package com.hado90.ui.screen;

import com.hado90.config.style.Style;
import com.hado90.fileMgt.FileManager;
import com.hado90.fileMgt.extractor.ExtractorZip;
import com.hado90.judge.JavaJudge;
import com.hado90.judge.compiler.JavaCompilerUtil;
import com.hado90.judge.testRunner.JavaTestRunner;
import com.hado90.pdfGenerator.PDFGenerator;
import com.hado90.submissionMgt.Submission;
import com.hado90.submissionMgt.SubmissionManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;

public class DashScreen extends Screen {
    
    protected String studentBulkFilePath;
    protected String testBulkFilePath;
    protected String outputFilePath;

    private int gapHeight;
    private int gapWidth;

    private Color btnColor;

    private boolean isStudentPathSet = false;
    private boolean isTestPathSet = false;
    private boolean isOutputPathSet = false;

    private JudgeButtonPanel judgeButtonPanel;

    public DashScreen(Style configStyle) {
        super(configStyle);
    }

    @Override
    protected void addContent(JPanel contentPanel) {
        contentPanel.setLayout(new BorderLayout());

        this.gapHeight = Integer.parseInt(Style.getConfigValue("SIZE_XS1"));
        this.gapWidth = Integer.parseInt(Style.getConfigValue("SIZE_XS1"));
        this.btnColor = decodeColor(Style.getConfigValue("BG_SECONDARY_COLOR"));
       

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4, gapWidth, gapHeight));

        buttonPanel.add(new DragDropButton("Student Submissions Compressed", btnColor, "Student Bulk File Path"));
        buttonPanel.add(new DragDropButton("Test Compressed File Path", btnColor, "Test Bulk File Path"));
        buttonPanel.add(new DragDropButton("Output File Path", btnColor, "Output File Path"));

        contentPanel.add(buttonPanel, BorderLayout.NORTH);

        contentPanel.add(new JProgressBar(), BorderLayout.CENTER);

        contentPanel.add(new ContentPanel(), BorderLayout.CENTER);

        judgeButtonPanel = new JudgeButtonPanel(this);
        contentPanel.add(judgeButtonPanel, BorderLayout.SOUTH);
    }

    public boolean isStudentPathSet() { return isStudentPathSet; }

    public boolean isTestPathSet() { return isTestPathSet; }

    public boolean isOutputPathSet() { return isOutputPathSet; }

    public void setStudentPathSet(boolean value) { 
        this.isStudentPathSet = value;
        refreshJudgeButton();
    }

    public void setTestPathSet(boolean value) {
        this.isTestPathSet = value;
        refreshJudgeButton();
    }

    public void setOutputPathSet(boolean value) {
        this.isOutputPathSet = value;
        refreshJudgeButton();
    }

    private void refreshJudgeButton() {
        if (judgeButtonPanel != null) {
            judgeButtonPanel.updateJudgeButtonState();
        }
    }

    public void executeJudgeLogic() {
        System.out.println(studentBulkFilePath);
        System.out.println(testBulkFilePath);
        System.out.println(outputFilePath);

        //auto judge process

        FileManager FILE_MANAGER = new FileManager(new String[] { "zip" });
        SubmissionManager SUBMISSION_MANAGER = new SubmissionManager();
        JavaCompilerUtil JAVA_COMPILER_UTIL = new JavaCompilerUtil(FILE_MANAGER);
        JavaJudge JAVA_JUDGE = new JavaJudge(FILE_MANAGER, JAVA_COMPILER_UTIL, new JavaTestRunner(), new PDFGenerator());

        ExtractorZip extractorZip = (ExtractorZip) FILE_MANAGER.getExtractor("zip");

        String locationOfSubmissions = null;

        try { locationOfSubmissions = extractorZip.extract(studentBulkFilePath, outputFilePath); }
        catch (Exception e) { e.printStackTrace(); }

        System.out.println("All submissions extracted to: " + locationOfSubmissions);

        System.out.println("Getting contents of submissions folder");
        List<String> list = FILE_MANAGER.fileUtility.getDirectoryContents(locationOfSubmissions);
        System.out.println(list);

        System.out.println("Filtering valid compressed formats");
        List<String> validList = FILE_MANAGER.fileUtility.filterDirectoryContentByTypes(new String[] { "zip" }, list);
        System.out.println(validList);

        SUBMISSION_MANAGER.bulkAddSubmissions(validList);
        SUBMISSION_MANAGER.getAllSubmissions();
        
        //PREPARE JUDGE
        JAVA_JUDGE.prepareCriteria(testBulkFilePath);

        //prepare submissions
        Map<String, Submission> submittedStatusSubmission = SUBMISSION_MANAGER.filterSubmissionsByStatus("SUBMITTED");
        submittedStatusSubmission.forEach((submissionId, submission) -> {
            System.out.println("Extracting submission: " + submissionId);
            try {
                String extractedPath = extractorZip.extract(submission.getSubmissionPath());
                submission.setSubmissionPath(extractedPath);
            } catch (Exception e) {
                // TODO: handle exception
            }
            System.out.println("Submission extracted to: " + submission.getSubmissionPath());
        });


        //loop through judging process
        //prepare submissions
        submittedStatusSubmission.forEach((submissionId, submission) -> {
            System.out.println("Preparing Submission: " + submissionId);
            JAVA_JUDGE.prepareSubmission(submission);
            System.out.println("Judging Submission: " + submissionId);
            JAVA_JUDGE.judgeSubmission(submission);
            System.out.println("Reporting Results: " + submissionId);
            JAVA_JUDGE.reportSubmissionResults(submission);
            System.out.println("Cleaning Up Submission: " + submissionId);
            JAVA_JUDGE.cleanupSubmission();
            System.out.println("Submission extracted to: " + submission.getSubmissionPath());
        });
        
        JAVA_JUDGE.cleanupJudge();

        try{
            Desktop.getDesktop().open(new File(outputFilePath));
        } catch (Exception e) { e.printStackTrace(); }
        
    }
}
