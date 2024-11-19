package com.hado90.judge.testRunner;

import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;

import java.util.HashMap;

public class TestExecutor {
    public HashMap<String, String> runTest(Class<?> testClass) throws Exception {
        System.out.println("Running test class: " + testClass.getName());
        HashMap<String, String> results = new HashMap<>();
        Launcher launcher = LauncherFactory.create();

        DiscoverySelector selector = DiscoverySelectors.selectClass(testClass);
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selector)
                .build();

        TestResultListener listener = new TestResultListener();
        launcher.registerTestExecutionListeners(listener);

        launcher.execute(request);
        results.putAll(listener.getResults());

        System.out.println("Finished running tests for class: " + testClass.getName());
        return results;
    }
}
