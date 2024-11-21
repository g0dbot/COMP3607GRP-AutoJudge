package com.hado90.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import com.hado90.config.style.Style;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.InputStream;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.nio.file.Paths;

public class SplashScreen extends JFrame {
    //window
    private final int borderRadius;
    private final int screenWidth;
    
    private final int imgWidth;
    private final int imgHeight;

    private final int progressBarHeight;
    private final Color progressBarFGColor;
    private final Color progressBarBGColor;

    private final int fillerPanelHeight;
    private final Color fillerPanelBGColor;
    
    private final int screenHeight;

    //ui components
    private JProgressBar splashProgressBar;
    private JLabel splashImage;
    private JLabel fillerPanel;

    public SplashScreen() throws Exception {

        this.borderRadius = Integer.parseInt(Style.getConfigValue("SIZE_M2"));
        this.screenWidth = Integer.parseInt(Style.getConfigValue("SIZE_WIN_WIDTH_S"));
    
        this.imgWidth = Integer.parseInt(Style.getConfigValue("SIZE_WIN_WIDTH_S"));
        this.imgHeight = Integer.parseInt(Style.getConfigValue("SIZE_WIN_WIDTH_S"));

        this.progressBarHeight = Integer.parseInt(Style.getConfigValue("SIZE_XS1"));
        this.progressBarFGColor = decodeColor(Style.getConfigValue("TEXT_MAIN_COLOR"));
        this.progressBarBGColor = decodeColor(Style.getConfigValue("PRIMARY_COLOR_SHADE_DISABLED"));

        this.fillerPanelHeight = Integer.parseInt(Style.getConfigValue("SIZE_XL2"));    
        this.fillerPanelBGColor = decodeColor(Style.getConfigValue("BG_SECONDARY_COLOR"));
    
        this.screenHeight = imgHeight + progressBarHeight + fillerPanelHeight;

        setUndecorated(true);//remove title bar and decorations
        setShape(new RoundRectangle2D.Double(0, 0, screenWidth, screenHeight, borderRadius, borderRadius));
        setLayout(new BorderLayout());

        //create components
        this.splashImage = createSplashImgLabel("img/logo.jpg", imgWidth, imgHeight);
        this.splashProgressBar = createSplashProgressBar(progressBarFGColor, progressBarBGColor, screenWidth, progressBarHeight);
        this.fillerPanel = createTextLabel(screenWidth, fillerPanelHeight, fillerPanelBGColor, "Hello");

        setSize(screenWidth, screenHeight);
        setLocationRelativeTo(null);//center screen

        add(splashImage, BorderLayout.NORTH);
        add(splashProgressBar, BorderLayout.CENTER);
        add(fillerPanel, BorderLayout.SOUTH);
    }

    // Create splash image label
    private JLabel createSplashImgLabel(String path, int imgWidth, int imgHeight) throws Exception {
        try (InputStream imageStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (imageStream == null) {
                throw new IllegalArgumentException("Resource not found: " + path);
            }
            ImageIcon icon = new ImageIcon(ImageIO.read(imageStream));
            Image image = icon.getImage().getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(image));
        }
    }

    // Create progress bar
    private JProgressBar createSplashProgressBar(Color fgColor, Color bgColor, int width, int height) {
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(false);
        progressBar.setBorder(null);
        progressBar.setForeground(fgColor);
        progressBar.setBackground(bgColor);
        progressBar.setPreferredSize(new Dimension(width, height));
        return progressBar;
    }

    // Create filler panel
    private JLabel createTextLabel(int width, int height, Color bgColor, String initialText) {
        JLabel label = new JLabel(initialText);
        label.setForeground(decodeColor(Style.getConfigValue("TEXT_DISABLED_COLOR")));
        label.setPreferredSize(new Dimension(width, height));
        label.setOpaque(true);
        label.setBackground(bgColor);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    public void updateTextLabel(String text) {
        this.fillerPanel.setText(text);
    }
    

    private Color decodeColor(String colorCode) {
        try {
            return Color.decode(colorCode);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid color code: " + colorCode, e);
        }
    }

    public void updateSplashLoaderProgress(int progress) {
        int currentProgress = this.splashProgressBar.getValue();

        int targetProgress = Math.min(100, Math.max(0, progress));

        while (currentProgress < targetProgress) {
            currentProgress++;
            this.splashProgressBar.setValue(currentProgress);

            try { Thread.sleep(10); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    // Show splash screen
    public void showSplashScreen() {
        setVisible(true);
    }

    // Close splash screen

    public void closeSplashScreen() {
        Platform.startup(() -> { 
            Media media = new Media(Paths.get("kurohitsugi\\src\\main\\resources\\kurohitsugi.mp3").toUri().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
    
            mediaPlayer.setOnEndOfMedia(() -> {
                setVisible(false);
                dispose();
            });
    
            mediaPlayer.play();
        });
        // long startTime = System.currentTimeMillis();
        // while (System.currentTimeMillis() - startTime < 1000) { }
        setVisible(false);
        dispose();
    }
}
