package com.hado90;

import javax.swing.SwingUtilities;

import com.hado90.config.style.Style;
import com.hado90.fileMgt.FileManager;
import com.hado90.judge.JavaJudge;
import com.hado90.judge.Judge;
import com.hado90.judge.compiler.JavaCompilerUtil;
import com.hado90.judge.testRunner.JavaTestRunner;
import com.hado90.pdfGenerator.PDFGenerator;
import com.hado90.submissionMgt.SubmissionManager;
import com.hado90.ui.MainScreen;
import com.hado90.ui.SplashScreen;

public class Kurohitsugi {
    public static void main(String[] args) throws Exception {

        //ALL SERVICES AND MANAGERS
        String[] ACCEPTED_TYPES = {"zip"};

        final FileManager FILE_MANAGER;
        final SubmissionManager SUBMISSION_MANAGER;
        final JavaCompilerUtil JAVA_COMPILER_UTIL;
        final Judge JAVA_JUDGE;
        final JavaTestRunner JAVA_TEST_RUNNER;
        final PDFGenerator PDF_GENERATOR;

        //load style config
        Style configStyle = new Style();
        configStyle.loadConfig("style.json");
        
        //splash screen loader
        SplashScreen splashScreen = new SplashScreen();
        splashScreen.showSplashScreen();        

        Thread loadingThread = new Thread(() -> {
            try {
                FILE_MANAGER = new FileManager(ACCEPTED_TYPES);
                SUBMISSION_MANAGER = new SubmissionManager();
                JAVA_COMPILER_UTIL = new JavaCompilerUtil(FILE_MANAGER);
                JAVA_TEST_RUNNER = new JavaTestRunner();
                PDF_GENERATOR = new PDFGenerator();
                JAVA_JUDGE = new JavaJudge(FILE_MANAGER, JAVA_COMPILER_UTIL, JAVA_TEST_RUNNER, PDF_GENERATOR);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // After loading completes, close the splash screen and show the main UI
            splashScreen.closeSplashScreen();

            // Now create and display the main UI
            SwingUtilities.invokeLater(() -> {
                MainScreen mainScreen = new MainScreen(configStyle);
                mainScreen.display(true);
            });
        });

        // Start the loading thread
        loadingThread.start();
    }
}