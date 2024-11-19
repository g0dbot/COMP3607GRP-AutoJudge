package com.hado90;

import javax.swing.SwingUtilities;

import com.hado90.config.style.Style;
import com.hado90.ui.MainScreen;
import com.hado90.ui.SplashScreen;

public class Kurohitsugi {
    public static void main(String[] args) throws Exception {

        //load style config
        Style configStyle = new Style();
        configStyle.loadConfig("style.json");
        
        //splash screen loader
        SplashScreen splashScreen = new SplashScreen();
        splashScreen.showSplashScreen();

        //simulate loading
        Thread loadingThread = new Thread(() -> {
            try {
                // Simulate loading tasks and update splash screen progress
                for (int i = 0; i <= 100; i++) {
                    splashScreen.updateSplashLoaderProgress(i);
                    Thread.sleep(10); // Simulate a task
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // After loading completes, close the splash screen and show the main UI
            splashScreen.closeSplashScreen();

            // Now create and display the main UI
            SwingUtilities.invokeLater(() -> {
                MainScreen mainScreen = new MainScreen(configStyle);
                mainScreen.display(true);
            });
        });

        // Start the loading thread
        loadingThread.start();
    }
}