package com.hado90;

import javax.swing.SwingUtilities;

import com.hado90.config.style.Style;
import com.hado90.ui.screen.DashScreen;
import com.hado90.ui.screen.Screen;
import com.hado90.ui.SplashScreen;


/**
 * Main entry point of the application, responsible for loading necessary services,
 * initializing UI components, and displaying the main dashboard screen after
 * loading the required components.
 */
public class Kurohitsugi {
     /**
     * The main method that initializes various services and manages the splash screen display
     * while loading resources. Once all resources are loaded, it transitions to the main dashboard.
     *
     * @param args the command-line arguments (not used in this case)
     * @throws Exception if any error occurs during the loading or initialization of services
     */
    public static void main(String[] args) throws Exception {

        //ALL SERVICES AND MANAGERS
        String[] ACCEPTED_TYPES = {"zip"};

        //load style config
        Style configStyle = new Style();
        configStyle.loadConfig("style.json");
        
        //splash screen loader
        SplashScreen splashScreen = new SplashScreen();
        splashScreen.showSplashScreen();        

        try {
            long startTime = System.currentTimeMillis();

            splashScreen.updateTextLabel("The oozing crest of corruption.");
            splashScreen.updateSplashLoaderProgress(10);
            splashScreen.updateTextLabel("The arrogant vessel of madness.");
            splashScreen.updateSplashLoaderProgress(20);
            splashScreen.updateTextLabel("Deny the seething urge");
            splashScreen.updateSplashLoaderProgress(30);
            splashScreen.updateTextLabel("To let things stun and flicker.");
            splashScreen.updateSplashLoaderProgress(40);
            splashScreen.updateTextLabel("Disrupt the sleep.");
            splashScreen.updateSplashLoaderProgress(50);
            splashScreen.updateTextLabel("The crawling princess of iron.");
            splashScreen.updateSplashLoaderProgress(60);
            splashScreen.updateTextLabel("The eternally self-destructing");
            splashScreen.updateSplashLoaderProgress(70);
            splashScreen.updateTextLabel("Doll of Mud.");
            splashScreen.updateSplashLoaderProgress(80);
            splashScreen.updateTextLabel("Unite, repulse, fill the earth");
            splashScreen.updateSplashLoaderProgress(90);
            splashScreen.updateTextLabel("And know your own powerlessness.");
            splashScreen.updateSplashLoaderProgress(95);
            splashScreen.updateTextLabel("Hado #90");
            splashScreen.updateSplashLoaderProgress(100);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        // After loading completes, close the splash screen and show the main UI
        splashScreen.closeSplashScreen();

        SwingUtilities.invokeLater(() -> {
            Screen mainScreen = new DashScreen(configStyle);
            mainScreen.display();
        });
    }
}