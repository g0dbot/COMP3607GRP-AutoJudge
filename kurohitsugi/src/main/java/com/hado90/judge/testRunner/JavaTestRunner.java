package com.hado90.judge.testRunner;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The JavaTestRunner class is responsible for loading test classes and submission classes,
 * and then executing the tests against the corresponding submission classes. It provides
 * functionality to load classes dynamically, match submission classes with their corresponding
 * tests, and collect results for evaluation.
 */
public class JavaTestRunner {
    
    private JudgingClassLoader classLoader;
    private TestExecutor testExecutor;
    private List<Class<?>> testClasses;
    private List<Class<?>> submissionClasses;

    /**
     * Initializes a new JavaTestRunner instance, which prepares the class loader, test executor,
     * and lists to hold test and submission classes.
     */
    public JavaTestRunner() {
        classLoader = new JudgingClassLoader();
        testExecutor = new TestExecutor();
        testClasses = new ArrayList<>();
        submissionClasses = new ArrayList<>();
    }

    /**
     * Retrieves a submission class by its name from the list of loaded submission classes.
     *
     * @param className the name of the class to retrieve.
     * @return the corresponding class object if found, otherwise returns null.
     */
    private Class<?> getSubmissionClassByName(String className) {
        return submissionClasses.stream()
                .filter(clazz -> clazz.getSimpleName().equals(className))
                .findFirst()
                .orElse(null);
    }

    /**
     * Loads the test and submission classes using the class loader.
     *
     * @throws Exception if an error occurs during the loading of the classes.
     */
    public void loadRunner() throws Exception {
        System.out.println("Loading runner...");
        loadTestClasses();
        loadSubmissionClasses();
    }

    /**
     * Loads the test classes using the {@link JudgingClassLoader}.
     *
     * @throws Exception if an error occurs while loading test classes.
     */
    private void loadTestClasses() throws Exception {
        testClasses = classLoader.loadTestClasses();
    }

    /**
     * Loads the submission classes using the {@link JudgingClassLoader}.
     *
     * @throws Exception if an error occurs while loading submission classes.
     */
    private void loadSubmissionClasses() throws Exception {
        submissionClasses = classLoader.loadSubmissionClasses();
    }

    /**
     * Runs the loaded test classes against the corresponding submission classes and collects the results.
     * The results are stored in a nested {@link HashMap} where the outer map's keys are the names of the
     * submission classes, and the inner map holds the individual test result details.
     *
     * @return a {@link HashMap} containing test results, keyed by submission class name and test method name.
     */
    public HashMap<String, HashMap<String, String>> run() {
        HashMap<String, HashMap<String, String>> testResults = new HashMap<>();

        // Iterate over test classes and find matching submission classes
        for (Class<?> testClass : testClasses) {
            for (Class<?> submissionClass : submissionClasses) {
                // Remove "Test" suffix from the test class name to match the submission class name
                String testClassNameWithoutTest = testClass.getSimpleName().replace("Test", "");
                if (submissionClass.getSimpleName().equals(testClassNameWithoutTest)) {
                    try {
                        // Run the test and collect the results
                        HashMap<String, String> result = testExecutor.runTest(testClass);
                        testResults.put(testClassNameWithoutTest, result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return testResults;
    }

    /**
     * The main method for running the test suite. It initializes a {@link JavaTestRunner} instance,
     * loads the classes, and executes the tests. The results are printed to the console.
     *
     * @param args command-line arguments (not used in this method).
     * @throws Exception if an error occurs during the test execution.
     */
    public static void main(String[] args) throws Exception {
        // Initialize the test runner and execute tests
        JavaTestRunner runner = new JavaTestRunner();
        runner.loadRunner();
        HashMap<String, HashMap<String, String>> results = runner.run();
        System.out.println(results);
    }
}
