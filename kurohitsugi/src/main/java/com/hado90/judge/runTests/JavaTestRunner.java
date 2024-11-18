package com.hado90.judge.runTests;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.core.LauncherConfig;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.LoggingListener;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.*;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.io.File;


import java.lang.reflect.Method;

public class JavaTestRunner {

    // Variables to store the test and submission classes
    private static List<Class<?>> testClasses = new ArrayList<>();
    private static List<Class<?>> submissionClasses = new ArrayList<>();

    public void loadRunner() throws Exception {
        loadTestClasses();
        loadSubmissionClasses();
        System.out.println(getSubmissionClassByName("ChatBot").getName());
    }

    private void loadTestClasses() throws Exception {
        // Get the path to the root of the package (where the 'com' folder starts)
        URL[] urls = new URL[] { new File("src/main/java").toURI().toURL() };

        // Create a URLClassLoader to load classes from the package
        ClassLoader classLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());

        // Get all class files in the test directory
        File testDir = new File("src/main/java/com/hado90/temp/tests");
        File[] testFiles = testDir.listFiles((dir, name) -> name.endsWith(".class"));

        if (testFiles != null) {
            for (File testFile : testFiles) {
                String className = testFile.getName().replace(".class", "");
                try {
                    Class<?> testClass = classLoader.loadClass("com.hado90.temp.tests." + className);
                    testClasses.add(testClass);  // Store the test class
                } catch (ClassNotFoundException e) {
                    System.out.println("Class not found: com.hado90.temp.tests." + className);
                }
            }
        }
    }

    private void loadSubmissionClasses() throws Exception {
        // Get the path to the root of the package (where the 'com' folder starts)
        URL[] urls = new URL[] { new File("src/main/java").toURI().toURL() };

        // Create a URLClassLoader to load classes from the package
        ClassLoader classLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());

        // Get all class files in the submission directory
        File submissionDir = new File("src/main/java/com/hado90/temp/submissions");
        File[] submissionFiles = submissionDir.listFiles((dir, name) -> name.endsWith(".class"));

        if (submissionFiles != null) {
            for (File submissionFile : submissionFiles) {
                String className = submissionFile.getName().replace(".class", "");
                try {
                    Class<?> submissionClass = classLoader.loadClass("com.hado90.temp.submissions." + className);
                    submissionClasses.add(submissionClass);  // Store the submission class
                } catch (ClassNotFoundException e) {
                    System.out.println("Class not found: com.hado90.temp.submissions." + className);
                }
            }
        }
    }

    private Class<?> getSubmissionClassByName(String className) {
        for (Class<?> submissionClass : submissionClasses) {
            if (submissionClass.getSimpleName().equals(className)) { return submissionClass; }
        }
        return null;
    }

    private Class<?> getTestClassByName(String className) {
        for (Class<?> testClass : testClasses) { if (testClass.getSimpleName().equals(className)) { return testClass; } }
        return null;
    }

    private void cycleRunner() {
        Class<?> tempTestClass = null;
        Class<?> tempSubmissionClass = null;
        
        //iter
        for (Class<?> testClass : testClasses) {
            tempTestClass = testClass;
            System.out.println("Current Test Class in temp variable: " + tempTestClass.getName());
    
            //iter submission
            for (Class<?> submissionClass : submissionClasses) {
                //rmv test suffix
                String testClassNameWithoutTest = tempTestClass.getSimpleName().replace("Test", "");
    
                //check for match
                if (submissionClass.getSimpleName().equals(testClassNameWithoutTest)) {
                    tempSubmissionClass = submissionClass;
                    System.out.println("Matching Submission Class found: " + tempSubmissionClass.getName());
                    try{runTest(tempTestClass, tempSubmissionClass);} catch (Exception e) {e.printStackTrace();}
                }
            }
    
            // If no matching submission class was found
            if (tempSubmissionClass == null) {
                System.out.println("No matching submission class found for test class: " + tempTestClass.getName());
            }
        }
    }

    public void runTest(Class<?> testClass, Class<?> submissionClass) throws Exception {
        // Create a Launcher to discover and execute tests
        Launcher launcher = LauncherFactory.create();

        // Create a discovery request for the test class passed
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(DiscoverySelectors.selectClass(testClass))
                .build();

        // Register a listener to capture test results
        TestExecutionListener listener = new TestExecutionListener() {
            @Override
            public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
                if (testIdentifier.isTest()) {
                    String testName = testIdentifier.getDisplayName();
                    TestExecutionResult.Status status = testExecutionResult.getStatus();
                    String result = (status == TestExecutionResult.Status.SUCCESSFUL) ? "PASSED" : "FAILED";

                    System.out.println("Test: " + testName + " - Result: " + result);

                    if (status == TestExecutionResult.Status.FAILED) {
                        testExecutionResult.getThrowable().ifPresent(throwable -> {
                            System.out.println("Error: " + throwable.getMessage());
                            // throwable.printStackTrace();
                        });
                    }
                }
            }
        };

        // Register listener for execution results
        launcher.registerTestExecutionListeners(listener);

        // Discover the tests and execute them
        TestPlan testPlan = launcher.discover(request);
        launcher.execute(request);

        System.out.println("All tests executed.");
    }

    public static void main(String[] args) throws Exception {
        JavaTestRunner runner = new JavaTestRunner();
        runner.loadRunner();
        runner.cycleRunner();
    }
}
