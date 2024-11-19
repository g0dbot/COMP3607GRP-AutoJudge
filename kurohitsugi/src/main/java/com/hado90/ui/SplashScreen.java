package com.hado90.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import com.hado90.config.style.Style;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.InputStream;

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
    private JPanel fillerPanel;

    public SplashScreen() throws Exception {

        this.borderRadius = Integer.parseInt(Style.getConfigValue("SIZE_XS1"));
        this.screenWidth = Integer.parseInt(Style.getConfigValue("SIZE_WIN_WIDTH_S"));
    
        this.imgWidth = Integer.parseInt(Style.getConfigValue("SIZE_WIN_WIDTH_S"));
        this.imgHeight = Integer.parseInt(Style.getConfigValue("SIZE_WIN_WIDTH_S"));

        this.progressBarHeight = Integer.parseInt(Style.getConfigValue("SIZE_XS1"));
        this.progressBarFGColor = decodeColor(Style.getConfigValue("PRIMARY_COLOR_SHADE_ACTIVE"));
        this.progressBarBGColor = decodeColor(Style.getConfigValue("BG_MAIN_COLOR"));

        this.fillerPanelHeight = Integer.parseInt(Style.getConfigValue("SIZE_XL2"));    
        this.fillerPanelBGColor = decodeColor(Style.getConfigValue("BG_MAIN_COLOR"));
    
        this.screenHeight = imgHeight + progressBarHeight + fillerPanelHeight;

        setUndecorated(true);//remove title bar and decorations
        setShape(new RoundRectangle2D.Double(0, 0, screenWidth, screenHeight, borderRadius, borderRadius));
        setLayout(new BorderLayout());

        //create components
        this.splashImage = createSplashImgLabel("img/logo.jpg", imgWidth, imgHeight);
        this.splashProgressBar = createSplashProgressBar(progressBarFGColor, progressBarBGColor, screenWidth, progressBarHeight);
        this.fillerPanel = createFillerPanel(screenWidth, fillerPanelHeight, fillerPanelBGColor);

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
    private JPanel createFillerPanel(int width, int height, Color bgColor) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBackground(bgColor);
        return panel;
    }

    private Color decodeColor(String colorCode) {
        try {
            return Color.decode(colorCode);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid color code: " + colorCode, e);
        }
    }

    // Update progress bar
    public void updateSplashLoaderProgress(int progress) {
        this.splashProgressBar.setValue(progress);
    }

    // Show splash screen
    public void showSplashScreen() {
        setVisible(true);
    }

    // Close splash screen
    public void closeSplashScreen() {
        setVisible(false);
        dispose();
    }
}
