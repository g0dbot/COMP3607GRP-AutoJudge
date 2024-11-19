package com.hado90.judge;

import com.hado90.fileMgt.FileManager;
import com.hado90.submissionMgt.Submission;

public abstract class Judge {

    private FileManager fileManager;
    
    public Judge(String compressedTestFilePath, FileManager fileManager){
        //sets file manager instance
        this.fileManager = fileManager;
        //takes the path to tests and unzips and processes them
        prepareCriteria(compressedTestFilePath);
    }

    public void prepareCriteria(String compressedTestFilePath){
        //processes the test
        //compile and store in temp/tests
    }

    public void prepareSubmission(Submission submission){
        //takes submission extract path
        //uses path to compile and store in temp/submissions
    }

    public void judgeSubmission(Submission studentSubmission) {
        //loads the test classes and submission classes
        //runs the tests
        //captures the result in submission
    }

    public void reportSubmissionResults(Submission studentSubmission) {
        //creates result reporting
        //generates the pdf
    }

    public void cleanupSubmission() {
        //deletes the temp files for that students submission
        //temp/submission
    }

    public void cleanupJudge(){
        //cleans up after judge is finished
    }
}
