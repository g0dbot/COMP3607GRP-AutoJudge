package com.hado90.judge.testRunner;

import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.*;

import java.util.HashMap;

public class TestExecutor {
    public HashMap<String, String> runTest(Class<?> testClass) throws Exception {
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

        return results;
    }
}
