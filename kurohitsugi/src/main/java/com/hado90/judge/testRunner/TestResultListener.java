package com.hado90.judge.testRunner;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;

import java.util.HashMap;

/**
 * The TestResultListener class implements the {@link TestExecutionListener} interface
 * to listen for the completion of test executions. It captures the result of each test
 * and stores them in a {@link HashMap}, where the key is the test name and the value
 * is the result of the test (either "PASSED" or "FAILED").
 */
public class TestResultListener implements TestExecutionListener {

    // A map to store test results with the test name as the key and the result as the value
    private HashMap<String, String> results;

    /**
     * Constructs a new {@link TestResultListener} and initializes the results map.
     */
    public TestResultListener() {
        this.results = new HashMap<>();
    }

    /**
     * This method is called when the execution of a test finishes.
     * It stores the result of the test in the results map.
     * The result is stored as "1PASSED_No Error" if the test passes, 
     * or "0FAILED_<error message>" if the test fails.
     *
     * @param testIdentifier the identifier of the test that was executed.
     * @param testExecutionResult the result of the test execution.
     */
    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        // Only process test cases, not other entities (e.g., test containers)
        if (testIdentifier.isTest()) {
            String testName = testIdentifier.getDisplayName();
            TestExecutionResult.Status status = testExecutionResult.getStatus();
            
            // Determine the result string based on the test status
            String result = (status == TestExecutionResult.Status.SUCCESSFUL) ? "PASSED" : "FAILED";

            // If the test failed, capture the error message
            if (status == TestExecutionResult.Status.FAILED) {
                testExecutionResult.getThrowable().ifPresent(throwable -> {
                    String errorMessage = "Error: " + throwable.getMessage();
                    results.put(testName, "0" + result + "_" + errorMessage);
                });
            } else {
                // If the test passed, no error message is included
                results.put(testName, "1" + result + "_No Error");
            }
        }
    }

    /**
     * Returns the results of all the executed tests.
     *
     * @return a {@link HashMap} containing the test results,
     *         where the key is the test name and the value is the result.
     */
    public HashMap<String, String> getResults() {
        return results;
    }
}
