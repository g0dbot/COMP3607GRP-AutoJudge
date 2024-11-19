package com.hado90.judge.testRunner;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class JudgingClassLoader {

    public List<Class<?>> loadTestClasses() throws Exception {

        List<Class<?>> classes = new ArrayList<>();

        URL[] urls = new URL[] { new File("kurohitsugi/src/main/java").toURI().toURL() };

        ClassLoader classLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());

        File testDir = new File("kurohitsugi/src/main/java/com/hado90/temp/tests");
        File[] testFiles = testDir.listFiles((dir, name) -> name.endsWith(".class"));

        System.out.println(testFiles.length);

        if (testFiles != null) {
            for (File testFile : testFiles) {
                String className = testFile.getName().replace(".class", "");
                try {
                    Class<?> testClass = classLoader.loadClass("com.hado90.temp.tests." + className);
                    classes.add(testClass); // Store the test class
                } catch (ClassNotFoundException e) {
                    System.out.println("Class not found: com.hado90.temp.tests." + className);
                }
            }
        }
        return classes;
    }

    public List<Class<?>> loadSubmissionClasses() throws Exception {

        List<Class<?>> classes = new ArrayList<>();

        URL[] urls = new URL[] { new File("kurohitsugi/src/main/java").toURI().toURL() };

        ClassLoader classLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());

        File testDir = new File("kurohitsugi/src/main/java/com/hado90/temp/submissions");
        File[] testFiles = testDir.listFiles((dir, name) -> name.endsWith(".class"));

        System.out.println(testFiles.length);

        if (testFiles != null) {
            for (File testFile : testFiles) {
                String className = testFile.getName().replace(".class", "");
                try {
                    Class<?> testClass = classLoader.loadClass("com.hado90.temp.submissions." + className);
                    classes.add(testClass); // Store the test class
                } catch (ClassNotFoundException e) {
                    System.out.println("Class not found: com.hado90.temp.submissions." + className);
                }
            }
        }
        return classes;
    }

}
