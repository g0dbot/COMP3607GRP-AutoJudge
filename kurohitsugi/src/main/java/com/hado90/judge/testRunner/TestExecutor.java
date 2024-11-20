package com.hado90.judge.testRunner;

import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;

import java.util.HashMap;

/**
 * The TestExecutor class is responsible for running JUnit test classes using the JUnit Platform.
 * It provides a method to execute a test class and retrieve the results.
 */
public class TestExecutor {

    /**
     * Runs the specified test class using the JUnit Platform and collects the test results.
     * The results are stored in a {@link HashMap} where the key is the test method name
     * and the value is the result of the test execution.
     *
     * @param testClass the {@link Class} object representing the test class to execute.
     * @return a {@link HashMap} containing the results of the tests where the key is the test method name
     *         and the value is the result of that test.
     * @throws Exception if there is an error during the test execution.
     */
    public HashMap<String, String> runTest(Class<?> testClass) throws Exception {
        System.out.println("Running test class: " + testClass.getName());
        
        // Initialize a map to store test results
        HashMap<String, String> results = new HashMap<>();

        // Create a launcher instance for executing tests
        Launcher launcher = LauncherFactory.create();

        // Create a discovery selector to select the test class to run
        DiscoverySelector selector = DiscoverySelectors.selectClass(testClass);

        // Build the launcher discovery request with the selector for the test class
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selector)
                .build();

        // Create a listener to capture test results
        TestResultListener listener = new TestResultListener();
        
        // Register the listener to capture the test execution results
        launcher.registerTestExecutionListeners(listener);

        // Execute the tests with the discovery request
        launcher.execute(request);

        // Collect and store the results from the listener
        results.putAll(listener.getResults());

        System.out.println("Finished running tests for class: " + testClass.getName());
        
        // Return the test results
        return results;
    }
}
