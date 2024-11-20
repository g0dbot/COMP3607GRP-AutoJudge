package com.hado90.ui.screen;

import javax.swing.JPanel;

import com.hado90.config.style.Style;

public class JudgeScreen extends Screen{

    private String studentBulkFilePath;
    private String testBulkFilePath;
    private String outputFilePath;

    public JudgeScreen(Style configStyle, String studentBulkFilePath, String testBulkFilePath, String outputFilePath) {
        super(configStyle);
        this.configStyle = configStyle;
        this.studentBulkFilePath = studentBulkFilePath;
        this.testBulkFilePath = testBulkFilePath;
        this.outputFilePath = outputFilePath;
    }

    @Override
    protected void addContent(JPanel contentPanel) {
        System.out.println(studentBulkFilePath);
        System.out.println(testBulkFilePath);
        System.out.println(outputFilePath);
    }    
}
