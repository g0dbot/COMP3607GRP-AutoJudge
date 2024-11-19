package com.hado90.judge.testRunner;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JavaTestRunner {
    private JudgingClassLoader classLoader;
    private TestExecutor testExecutor;
    private List<Class<?>> testClasses;
    private List<Class<?>> submissionClasses;

    public JavaTestRunner() {
        classLoader = new JudgingClassLoader();
        testExecutor = new TestExecutor();
        testClasses = new ArrayList<>();
        submissionClasses = new ArrayList<>();
    }

    private Class<?> getSubmissionClassByName(String className) {
        return submissionClasses.stream()
                .filter(clazz -> clazz.getSimpleName().equals(className))
                .findFirst()
                .orElse(null);
    }

    public void loadRunner() throws Exception {
        System.out.println("Loading runner...");
        loadTestClasses();
        loadSubmissionClasses();
    }

    private void loadTestClasses() throws Exception {
        testClasses = classLoader.loadTestClasses();
    }

    private void loadSubmissionClasses() throws Exception {
        submissionClasses = classLoader.loadSubmissionClasses();
    }

    public HashMap<String, HashMap<String, String>> cycleRunner() {
        HashMap<String, HashMap<String, String>> testResults = new HashMap<>();

        for (Class<?> testClass : testClasses) {
            for (Class<?> submissionClass : submissionClasses) {
                String testClassNameWithoutTest = testClass.getSimpleName().replace("Test", "");
                if (submissionClass.getSimpleName().equals(testClassNameWithoutTest)) {
                    try {
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

    public static void main(String[] args) throws Exception {
        JavaTestRunner runner = new JavaTestRunner();
        runner.loadRunner();
        HashMap<String, HashMap<String, String>> results = runner.cycleRunner();
        System.out.println(results);
    }
}