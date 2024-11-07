package com.comp3607.models.ui;

//java display libs
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class MainUi extends JFrame{
    private JTextField filePathField;
    private JTextArea outputArea;

    public MainUi() {
        setTitle("Java Assignment Submission Processor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(400, 300));
        initializeUI();
        pack();
    }

    private void initializeUI() {
        // Panel for file selection
        JPanel filePanel = new JPanel();
        filePanel.setLayout(new FlowLayout());
    
        JLabel fileLabel = new JLabel("Select a zipped submission file:");
        filePanel.add(fileLabel);
    
        filePathField = new JTextField(30);
        filePanel.add(filePathField);
    
        JButton browseButton = new JButton("Browse");
        browseButton.addActionListener(new BrowseAction());
        filePanel.add(browseButton);
    
        // Separate panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
    
        JButton extractButton = new JButton("Extract and Process");
        buttonPanel.add(extractButton); // Remove comment
    
        // Text area to show output with dynamic sizing
        outputArea = new JTextArea(20, 0); 
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    
        // Layout settings with dynamic sizing
        add(filePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER); 
        add(buttonPanel, BorderLayout.SOUTH); 
    }

    // Action for browsing file
    private class BrowseAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Zip files", "zip"));
            int result = fileChooser.showOpenDialog(MainUi.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                filePathField.setText(selectedFile.getAbsolutePath());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainUi app = new MainUi();
            app.setVisible(true);
        });
    }
}
