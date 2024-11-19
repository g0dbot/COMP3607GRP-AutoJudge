package com.hado90.judge.testRunner;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class JudgingClassLoader {
    
    public List<Class<?>> loadClasses(String directoryPath, String packagenName) throws Exception {
        List<Class<?>> classes = new ArrayList<>();

        URL[] urls = new URL[] { new File ("src/main/java").toURI().toURL() };
        ClassLoader classLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());

        File testDir = new File(directoryPath);
        File[] testFiles = testDir.listFiles((dir, name) -> name.endsWith(".class"));

        if (testFiles != null) {
            for (File testFile : testFiles) {
                String className = testFile.getName().replace(".class", "");
                try {
                    Class<?> testClass = classLoader.loadClass(packagenName + "." + className);
                    classes.add(testClass);
                } catch (ClassNotFoundException e) {
                    System.out.println("Class not found: " + packagenName + "." + className);
                }
            }
        }

        return classes;
    }
}
