package com.hado90.ui;

import javax.swing.JFrame;

import com.hado90.config.style.Style;

public class MainScreen extends JFrame {

    Style configStyle;
    JFrame frame;

    public MainScreen(Style configStyle){
        this.configStyle = configStyle;

        this.frame = new JFrame("Kurohitsugi");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(500, 400);
        this.frame.setUndecorated(true);//remove title bar and decorations
        this.frame.setLocationRelativeTo(null);//center screen
        this.frame.setVisible(true);
    }

    public void display(boolean visible) {
        this.frame.setVisible(visible);
    }

    
}
