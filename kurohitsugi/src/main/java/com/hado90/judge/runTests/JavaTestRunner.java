package com.hado90.judge.runTests;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.hado90.pdfGenerator.PDFGenerator;
import com.hado90.pdfGenerator.Student;
import com.hado90.judge.runTests.JavaTestRunner;
import com.hado90.judge.Judge;
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

public class JavaTestRunner{

    // Variables to store the test and submission classes
    private static List<Class<?>> testClasses = new ArrayList<>();
    private static List<Class<?>> submissionClasses = new ArrayList<>();

    public void loadRunner(String submissionPath) throws Exception {
        loadTestClasses();
        loadSubmissionClasses(submissionPath);
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

    private void loadSubmissionClasses(String submissionPath) throws Exception {
        // Get the path to the root of the package (where the 'com' folder starts)
        URL[] urls = new URL[] { new File("src/main/java").toURI().toURL() };

        // Create a URLClassLoader to load classes from the package
        ClassLoader classLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());

        // Get all class files in the submission directory
        // File submissionDir = new File("src/main/java/com/hado90/temp/submissions");
        File submissionDir = new File(submissionPath);
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

    private HashMap<String, HashMap<String, String>> cycleRunner() {
        Class<?> tempTestClass = null;
        Class<?> tempSubmissionClass = null;
        boolean found = false; //used to find the corresponding class of submission
        HashMap<String, String> result = new HashMap<>(); // Individual test result
        HashMap<String, HashMap<String, String>> testResults = new HashMap<>(); // Collective test results
        /// Hashmap <String, <Hashmap<String, String>>>
        
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
                    found = true;
                    tempSubmissionClass = submissionClass;
                    System.out.println("Matching Submission Class found: " + tempSubmissionClass.getName());
                    try{
                        result = runTest(tempTestClass, tempSubmissionClass);
                        testResults.put(testClassNameWithoutTest, result);
                        //Put the results into the hashmap
                    } 
                    catch (Exception e) {e.printStackTrace();}
                }
            }
    
            // If no matching submission class was found
            if (found == false) {
                System.out.println("No matching submission class found for test class: " + tempTestClass.getName());
            }
        }
        return testResults;
    }

    public HashMap<String,String> runTest(Class<?> testClass, Class<?> submissionClass) throws Exception {
        HashMap<String,String> results = new HashMap<>();
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

                    // System.out.println("Test: " + testName + " - Result: " + result);
                    if (status == TestExecutionResult.Status.FAILED) {
                        testExecutionResult.getThrowable().ifPresent(throwable -> {
                            String errorMessage = "Error: " + throwable.getMessage();
                            results.put(testName, "0" + result + "_" + errorMessage);
                            // throwable.printStackTrace();

                        });}
                    else{
                        results.put(testName, "1" + result + "_" + "No Error");

                    
                    }
                }
            } 
        };
        
            
        // Register listener for execution results
        launcher.registerTestExecutionListeners(listener);

        // Discover the tests and execute them
        TestPlan testPlan = launcher.discover(request);
        launcher.execute(request);
        return results;
    }

    public HashMap<String, HashMap<String, String>> run() throws Exception {
        JavaTestRunner runner = new JavaTestRunner();
        runner.loadRunner();
        HashMap<String, HashMap<String, String>> results = runner.cycleRunner();
        return results;
        // int[] passedTests = new int[1];
        // int[] totalTestResults = new int[1];
        // results.forEach((testClassName, testResults) -> {
        //     testResults.forEach((testName, result) -> {
        //         if (result.substring(0, 1).equals("1")) {
        //             passedTests[0] = passedTests[0] + 1;
        //         }
        //     });
        // });
        // System.out.println("Total number of passed tests: " + passedTests[0]);
        //     results.forEach((testClassName, testResults) -> {
        //         totalTestResults[0] += testResults.size();
        //     });
        // System.out.println("Total number of test results: " + totalTestResults[0]);
        // System.out.println("Percentage of passed tests: " + ((double) passedTests[0] / totalTestResults[0]) * 100);
        // String score = String.format("%.2f", ((double) passedTests[0] / totalTestResults[0]) * 100);
        // PDFGenerator pdfGenerator = new PDFGenerator(); 
        // Student s = new Student("John", "Doe", "1234567890", "Generic Course Name", "GEN101", "1", score);
        // pdfGenerator.generatePDF(s,results);

        // results.forEach((testClassName, testResults) -> {
        //     System.out.println("Test Class: " + testClassName);
        //     testResults.forEach((testName, result) -> {
        //         System.out.println("  Test: " + testName + " - Result: " + result);
        //     });
        // });
    }
    // public static void main(String[] args) throws Exception {
    //     JavaTestRunner runner = new JavaTestRunner();
    //     runner.loadRunner();
    //     HashMap<String, HashMap<String, String>> results = runner.cycleRunner();
    //     int[] passedTests = new int[1];
    //     int[] totalTestResults = new int[1];
    //     results.forEach((testClassName, testResults) -> {
    //         testResults.forEach((testName, result) -> {
    //             if (result.substring(0, 1).equals("1")) {
    //                 passedTests[0] = passedTests[0] + 1;
    //             }
    //         });
    //     });
    //     System.out.println("Total number of passed tests: " + passedTests[0]);
    //         results.forEach((testClassName, testResults) -> {
    //             totalTestResults[0] += testResults.size();
    //         });
    //     System.out.println("Total number of test results: " + totalTestResults[0]);
    //     System.out.println("Percentage of passed tests: " + ((double) passedTests[0] / totalTestResults[0]) * 100);
    //     String score = String.format("%.2f", ((double) passedTests[0] / totalTestResults[0]) * 100);
    //     PDFGenerator pdfGenerator = new PDFGenerator(); 
    //     Student s = new Student("John", "Doe", "1234567890", "Generic Course Name", "GEN101", "1", score);
    //     pdfGenerator.generatePDF(s,results);

    //     // results.forEach((testClassName, testResults) -> {
    //     //     System.out.println("Test Class: " + testClassName);
    //     //     testResults.forEach((testName, result) -> {
    //     //         System.out.println("  Test: " + testName + " - Result: " + result);
    //     //     });
    //     // });
    // }
}
