package com.hado90.judge.testRunner;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * The JudgingClassLoader class is responsible for loading test and submission classes from 
 * the specified directory paths. It dynamically loads the `.class` files from the given directories
 * and makes them available for test execution.
 */
public class JudgingClassLoader {

    /**
     * Loads the test classes from the specified directory containing compiled test classes.
     * The method assumes that the compiled test classes are located under the
     * "kurohitsugi/src/main/java/com/hado90/temp/tests" directory.
     *
     * @return a list of {@link Class} objects representing the test classes.
     * @throws Exception if an error occurs during the class loading process.
     */
    public List<Class<?>> loadTestClasses() throws Exception {

        List<Class<?>> classes = new ArrayList<>();

        // Specify the base URL for class loading
        URL[] urls = new URL[] { new File("kurohitsugi/src/main/java").toURI().toURL() };

        // Create a new class loader using the specified URL
        ClassLoader classLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());

        // Define the directory containing the compiled test classes
        File testDir = new File("kurohitsugi/src/main/java/com/hado90/temp/tests");
        // List all files in the directory that end with .class (compiled classes)
        File[] testFiles = testDir.listFiles((dir, name) -> name.endsWith(".class"));

        System.out.println("Number of test classes found: "+testFiles.length); // Debug output to display the number of test classes found

        // Load each class file found in the directory
        if (testFiles != null) {
            for (File testFile : testFiles) {
                String className = testFile.getName().replace(".class", ""); // Remove the .class extension
                try {
                    // Dynamically load the class using the class loader
                    Class<?> testClass = classLoader.loadClass("com.hado90.temp.tests." + className);
                    classes.add(testClass); // Store the test class in the list
                } catch (ClassNotFoundException e) {
                    // Print an error message if the class cannot be found
                    System.out.println("Class not found: com.hado90.temp.tests." + className);
                }
            }
        }
        return classes; // Return the list of loaded test classes
    }

    /**
     * Loads the submission classes from the specified directory containing compiled submission classes.
     * The method assumes that the compiled submission classes are located under the
     * "kurohitsugi/src/main/java/com/hado90/temp/submissions" directory.
     *
     * @return a list of {@link Class} objects representing the submission classes.
     * @throws Exception if an error occurs during the class loading process.
     */
    public List<Class<?>> loadSubmissionClasses() throws Exception {

        List<Class<?>> classes = new ArrayList<>();

        // Specify the base URL for class loading
        URL[] urls = new URL[] { new File("kurohitsugi/src/main/java").toURI().toURL() };

        // Create a new class loader using the specified URL
        ClassLoader classLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());

        // Define the directory containing the compiled submission classes
        File testDir = new File("kurohitsugi/src/main/java/com/hado90/temp/submissions");
        // List all files in the directory that end with .class (compiled classes)
        File[] testFiles = testDir.listFiles((dir, name) -> name.endsWith(".class"));

        System.out.println("Number of subject classes found: "+testFiles.length); // Debug output to display the number of submission classes found

        // Load each class file found in the directory
        if (testFiles != null) {
            for (File testFile : testFiles) {
                String className = testFile.getName().replace(".class", ""); // Remove the .class extension
                try {
                    // Dynamically load the class using the class loader
                    Class<?> testClass = classLoader.loadClass("com.hado90.temp.submissions." + className);
                    classes.add(testClass); // Store the submission class in the list
                } catch (ClassNotFoundException e) {
                    // Print an error message if the class cannot be found
                    System.out.println("Class not found: com.hado90.temp.submissions." + className);
                }
            }
        }
        return classes; // Return the list of loaded submission classes
    }
}
