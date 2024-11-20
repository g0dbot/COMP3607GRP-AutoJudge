package com.hado90.ui.screen;

import javax.swing.*;
import java.awt.*;

public class ContentPanel extends JPanel {
    public ContentPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(500, 450));
        setBackground(Color.LIGHT_GRAY);

        JLabel placeholderLabel = new JLabel("Content Section - Add your components here");
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        placeholderLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(placeholderLabel, BorderLayout.CENTER);
    }
}

