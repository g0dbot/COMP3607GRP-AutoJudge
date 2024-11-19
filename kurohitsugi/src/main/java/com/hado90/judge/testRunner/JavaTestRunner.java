package com.hado90.judge.testRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JavaTestRunner {
    private JudgingClassLoader classLoader;
    private TestExecutor testExecutor;
    private List<Class<?>> testClasses;
    private List<Class<?>> submissionClasses;

    public JavaTestRunner(){
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
        testClasses = classLoader.loadClasses("src/main/java/com/hado90/temp/tests", "com.hado90.temp.tests");
        submissionClasses = classLoader.loadClasses("src/main/java/com/hado90/temp/tests", "com.hado90.temp.submissions");
        //System.out.println(getSubmissionClassByName("ChatBot").getName());
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

    public HashMap<String, HashMap<String, String>> run() throws Exception {
        loadRunner();
        return cycleRunner();
    }
}
