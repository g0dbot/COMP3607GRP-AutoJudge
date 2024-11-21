package com.hado90.ui.screen;

import com.hado90.config.Config;
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
    private Color judgeBgColor;
    private Color judgeFgColor;

    private boolean isStudentPathSet = false;
    private boolean isTestPathSet = false;
    private boolean isOutputPathSet = false;

    private JudgeButtonPanel judgeButtonPanel;
    private JLabel statusLabel;
    public JProgressBar progressbar;

    public DashScreen(Style configStyle) {
        super(configStyle);
    }

    @Override
    protected void addContent(JPanel contentPanel) {
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        this.gapHeight = Integer.parseInt(Style.getConfigValue("SIZE_S1"));
        this.gapWidth = Integer.parseInt(Style.getConfigValue("SIZE_XS1"));
        this.btnColor = super.decodeColor(Style.getConfigValue("PRIMARY_COLOR_SHADE_DISABLED"));
        this.judgeBgColor = super.decodeColor(Style.getConfigValue("PRIMARY_COLOR_SHADE_MAIN"));
        this.judgeFgColor = super.decodeColor(Style.getConfigValue("TEXT_MAIN_COLOR"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, gapWidth, gapHeight));
        
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 25, 10));
        buttonPanel.setBackground(decodeColor(Style.getConfigValue("BG_SECONDARY_COLOR")));

        buttonPanel.add(new DragDropButton("Student Submissions Compressed", btnColor, "Student Bulk File Path"));
        buttonPanel.add(new DragDropButton("Test Compressed File Path", btnColor, "Test Bulk File Path"));
        buttonPanel.add(new DragDropButton("Output File Path", btnColor, "Output File Path"));

        this.progressbar = new JProgressBar();
        this.progressbar.setPreferredSize(new Dimension(getWidth() - 40, 100)); 
        this.progressbar.setMinimum(0);
        this.progressbar.setForeground(decodeColor(Style.getConfigValue("PRIMARY_COLOR_SHADE_ACTIVE")));
        this.progressbar.setBackground(decodeColor(Style.getConfigValue("BG_SECONDARY_COLOR")));
               

        statusLabel = new JLabel("Status: Waiting for user input...");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Monospace", Font.PLAIN, 14));
        

        judgeButtonPanel = new JudgeButtonPanel(this, judgeBgColor, judgeFgColor);

        contentPanel.add(buttonPanel, BorderLayout.NORTH);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(progressbar, BorderLayout.CENTER);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(statusLabel, BorderLayout.CENTER);
        contentPanel.add(Box.createVerticalStrut(10));
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

    public void updateProgressBar(int progress) {
        int currentProgress = this.progressbar.getValue();

        int targetProgress = Math.min(100, Math.max(0, progress));

        while (currentProgress < targetProgress) {
            currentProgress++;
            this.progressbar.setValue(currentProgress);

            try { Thread.sleep(10); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    public void updateStatusLabel(String message) {
        long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < 1000) { }
            statusLabel.setText("Status: " + message);
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
        updateStatusLabel("extracting submissions...");
        try { locationOfSubmissions = extractorZip.extract(studentBulkFilePath, outputFilePath); }
        catch (Exception e) { e.printStackTrace(); }

        if (locationOfSubmissions == null) {
            updateStatusLabel("Failed to extract submissions");
            return;
        }

        updateStatusLabel("submissions extracted to: " + locationOfSubmissions);

        updateStatusLabel("getting contents of submissions...");
        List<String> list = FILE_MANAGER.fileUtility.getDirectoryContents(locationOfSubmissions);
        
        updateStatusLabel("extracted "+ list.size() + " submissions");

        updateStatusLabel("filtering submissions...");
        List<String> validList = FILE_MANAGER.fileUtility.filterDirectoryContentByTypes(new String[] { "zip" }, list);
        
        updateStatusLabel("found " + validList.size() + " valid submissions");

        updateStatusLabel("processing submissions...");
        SUBMISSION_MANAGER.bulkAddSubmissions(validList);
        SUBMISSION_MANAGER.getAllSubmissions();
        
        //PREPARE JUDGE
        updateStatusLabel("preparing judge...");
        JAVA_JUDGE.prepareCriteria(testBulkFilePath);

        updateStatusLabel("judge prepared...");
        //prepare submissions
        Map<String, Submission> submittedStatusSubmission = SUBMISSION_MANAGER.filterSubmissionsByStatus("SUBMITTED");
        submittedStatusSubmission.forEach((submissionId, submission) -> {
            System.out.println("Extracting submission: " + submissionId);
            try {
                updateStatusLabel("processing submission: " + submissionId);
                updateStatusLabel("extracting submission: " + submissionId);
                String extractedPath = extractorZip.extract(submission.getSubmissionPath());
                
                submission.setSubmissionPath(extractedPath);
            } catch (Exception e) {
                // TODO: handle exception
            }
            updateStatusLabel("submission: " + submissionId + "extracted to: " + submission.getSubmissionPath());
        });


        //loop through judging process
        //prepare submissions
        progressbar.setMaximum(validList.size());
        progressbar.setValue(0);
        final int[] progCount = new int[1];

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

            progCount[0]++;
            int progress = (int) (((double) progCount[0] / validList.size()) * 100);
            updateProgressBar(progress);
        });
        
        JAVA_JUDGE.cleanupJudge();

        try{
            Desktop.getDesktop().open(new File(outputFilePath));
        } catch (Exception e) { e.printStackTrace(); }
        
    }
}
