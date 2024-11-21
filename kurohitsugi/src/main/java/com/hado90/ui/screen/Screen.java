package com.hado90.ui.screen;

import javax.imageio.ImageIO;
import javax.swing.*;
import com.hado90.config.style.Style;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.InputStream;

public abstract class Screen extends JFrame {
    protected final int screenWidth;
    protected final int screenHeight;
    protected final int borderRadius;
    protected final Color screenColor;

    protected Color navbarPanelColor;
    protected int navbarPanelHeight;

    protected int closeBtnWidth;
    protected int closeBtnHeight;
    protected Color closeBtnMainColor;
    protected Color closeBtnActiveColor;
    protected Color closeBtnDisabledColor;

    protected JPanel navbarPanel;
    protected JButton closeButton;
    protected JPanel contentPanel;
    public Style configStyle;

    public Screen(Style configStyle) {
        this.screenWidth = Integer.parseInt(Style.getConfigValue("SIZE_WIN_WIDTH_L"));
        this.screenHeight = 400;
        this.borderRadius = Integer.parseInt(Style.getConfigValue("SIZE_M1"));
        this.screenColor = decodeColor(Style.getConfigValue("BG_SECONDARY_COLOR"));

        this.navbarPanelColor = decodeColor(Style.getConfigValue("BG_MAIN_COLOR"));
        this.navbarPanelHeight = Integer.parseInt(Style.getConfigValue("SIZE_XL1"));
        this.closeBtnWidth = Integer.parseInt(Style.getConfigValue("SIZE_XL3"));
        this.closeBtnHeight = Integer.parseInt(Style.getConfigValue("SIZE_XL3"));
        this.closeBtnMainColor = navbarPanelColor;
        this.closeBtnActiveColor = decodeColor(Style.getConfigValue("EMO_COLOR_NEG_SHADE_ACTIVE"));
        this.closeBtnDisabledColor = decodeColor(Style.getConfigValue("EMO_COLOR_NEG_SHADE_DISABLED"));

        initFrame();
        initNavbar();
        initContentPanel();
        setupLayout();
    }

    private void initFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(screenWidth, screenHeight);
        this.setUndecorated(true);
        this.setShape(new RoundRectangle2D.Double(0, 0, screenWidth, screenHeight, borderRadius, borderRadius));
        this.setLocationRelativeTo(null);
    }

    private void initNavbar() {
        navbarPanel = new JPanel();
        navbarPanel.setLayout(new BorderLayout());
        navbarPanel.setBackground(navbarPanelColor);
        navbarPanel.setPreferredSize(new Dimension(screenWidth, navbarPanelHeight));
        initCloseButton();
        navbarPanel.add(closeButton, BorderLayout.EAST);
    }

    private void initCloseButton() {
        closeButton = new JButton();
        closeButton.setBackground(closeBtnMainColor);
        closeButton.setBorder(null);
        closeButton.setFocusable(false);
        closeButton.setPreferredSize(new Dimension(closeBtnWidth, closeBtnHeight));

        try (InputStream imageStream = getClass().getClassLoader().getResourceAsStream("img/closeBtn.png")) {
            if (imageStream == null) throw new IllegalArgumentException("Resource not found: img/closeBtn.png");
            ImageIcon icon = new ImageIcon(ImageIO.read(imageStream));
            Image image = icon.getImage().getScaledInstance(closeBtnWidth - 10, closeBtnHeight - 10, Image.SCALE_SMOOTH);
            closeButton.setIcon(new ImageIcon(image));
        } 
        catch (Exception e) { throw new RuntimeException("Failed to load close button icon", e); }

        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { closeButton.setBackground(closeBtnActiveColor); }

            @Override
            public void mouseExited(MouseEvent e) { closeButton.setBackground(closeBtnMainColor); }
        });

        closeButton.addActionListener(e -> System.exit(0));
    }

    private void initContentPanel() {
        contentPanel = new JPanel();
        contentPanel.setBackground(screenColor);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        addContent(contentPanel);
    }

    protected abstract void addContent(JPanel contentPanel);

    private void setupLayout() {
        this.setLayout(new BorderLayout());
        this.add(navbarPanel, BorderLayout.NORTH);
        this.add(contentPanel, BorderLayout.CENTER);
        this.pack();
    }

    public Color decodeColor(String colorStr) {
        if (colorStr == null || colorStr.trim().isEmpty()) {
            System.err.println("Invalid color value, using default color.");
            return Color.GRAY; 
        }
        try {
            return Color.decode(colorStr);
        } catch (NumberFormatException e) {
            System.err.println("Invalid color format: " + colorStr);
            return Color.GRAY;
        }
    }
    

    public void display() { setVisible(true); }
    public void close() { setVisible(false); }
}
