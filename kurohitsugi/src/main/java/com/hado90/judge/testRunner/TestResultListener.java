package com.hado90.judge.testRunner;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;

import java.util.HashMap;

public class TestResultListener implements TestExecutionListener {
    private HashMap<String, String> results;

    public TestResultListener() {
        this.results = new HashMap<>();
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        if (testIdentifier.isTest()) {
            String testName = testIdentifier.getDisplayName();
            TestExecutionResult.Status status = testExecutionResult.getStatus();
            String result = (status == TestExecutionResult.Status.SUCCESSFUL) ? "PASSED" : "FAILED";

            if (status == TestExecutionResult.Status.FAILED) {
                testExecutionResult.getThrowable().ifPresent(throwable -> {
                    String errorMessage = "Error: " + throwable.getMessage();
                    results.put(testName, "0" + result + "_" + errorMessage);
                });
            } else {
                results.put(testName, "1" + result + "_No Error");
            }
        }
    }

    public HashMap<String, String> getResults() {
        return results;
    }
}