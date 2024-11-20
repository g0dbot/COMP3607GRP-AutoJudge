package com.hado90.pdfGenerator;

import java.util.HashMap;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.hado90.submissionMgt.Submission;


import com.hado90.submissionMgt.Submission;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

/**
 * Class responsible for generating a PDF report containing feedback on a student's assignment.
 * The PDF includes the student's details and the results of the test cases.
 */
public class PDFGenerator {
    /**
     * Generates a PDF report for a student's assignment based on the test results.
     * The report includes the student's name, ID, course details, assignment score, and the outcome of each test case.
     * 
     * @param studentInfo an object containing the student's information (name, ID, course details, etc.)
     * @param testResults a HashMap containing the results of the test cases for the student's submission,
     *                    with the test suite names as keys and another HashMap with test names and their results as values.
     */
    public void generatePDF(Submission studentSubmission, HashMap<String, HashMap<String, String>> testResults) {
        String path = studentSubmission.getSubmissionPath();
        String filename = path + studentSubmission.getSubmissionId() + "_"
                + studentSubmission.getSubmissionFirstName() + "_"
                + studentSubmission.getSubmissionLastName() + "_"
                + "A1" + "_Feedback.pdf";

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 24);
            contentStream.setLeading(25.5f);
            contentStream.newLineAtOffset(50, 720);
            contentStream.showText("Student Name: " + studentSubmission.getSubmissionFirstName() + " " + studentSubmission.getSubmissionLastName());
            contentStream.showText("Student Name: " + studentSubmission.getSubmissionFirstName() + " " + studentSubmission.getSubmissionLastName());
            contentStream.newLine();
            contentStream.showText("Student ID: " + studentSubmission.getSubmissionId());
            contentStream.showText("Student ID: " + studentSubmission.getSubmissionId());
            contentStream.newLine();
            contentStream.setFont(PDType1Font.HELVETICA, 16);
            contentStream.showText("Course Name: Object Oriented Programming");
            contentStream.showText("Course Name: Object Oriented Programming");
            contentStream.newLine();
            contentStream.showText("Course Code: COMP2603");
            contentStream.showText("Course Code: COMP2603");
            contentStream.newLine();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.showText("Assignment #: A1");
            contentStream.showText("Assignment #: A1");
            contentStream.newLine();
            //contentStream.showText("Score: " + studentSubmission.getScore() + "%");
            contentStream.endText();
            contentStream.close();

            // Display the outcomes of all the test cases
            int pageNum = 1;
            // System.out.println("Test Results:");
            // for (String className : testResults.keySet()) {
            // System.out.println(className);
            // for (String testName : testResults.get(className).keySet()) {
            // System.out.println(" " + testName + ": " +
            // testResults.get(className).get(testName));
            // }
            // }

            for (String className : testResults.keySet()) {
                System.out.println(className);

                PDPage page2 = new PDPage();
                document.addPage(page2);
                try (PDPageContentStream contentStream2 = new PDPageContentStream(document,
                        document.getPage(pageNum))) {
                    contentStream2.beginText();
                    contentStream2.setFont(PDType1Font.HELVETICA, 24);
                    contentStream2.setLeading(15.5f);
                    contentStream2.newLineAtOffset(50, 700);
                    contentStream2.showText("Test Suite: " + className);
                    contentStream2.newLine();
                    contentStream2.showText("______________________________");
                    contentStream2.endText();
                    contentStream2.beginText();
                    contentStream2.setFont(PDType1Font.HELVETICA, 12);
                    contentStream2.newLineAtOffset(50, 640);
                    for (String testName : testResults.get(className).keySet()) {
                        String result = testResults.get(className).get(testName);
                        String[] parts = result.split("_", 2);
                        String status = parts[0].equals("1PASSED") ? "PASSED" : "FAILED";
                        String comment = parts.length > 1 ? parts[1] : "No Comment";

                        contentStream2.showText("Test: ");
                        contentStream2.setFont(PDType1Font.HELVETICA_BOLD, 11);
                        contentStream2.showText(testName);
                        contentStream2.setFont(PDType1Font.HELVETICA, 11);
                        contentStream2.newLine();
                        contentStream2.showText("----Status: " + status);
                        contentStream2.newLine();
                        contentStream2.showText("--------Comment: " + comment);
                        contentStream2.newLine();
                        contentStream2.showText("____________________________________________________________");
                        contentStream2.newLine();
                        contentStream2.newLine();
                    }

                }
                pageNum++;
            }
            document.save(filename);
            System.out.println("PDF created successfully at " + filename);
            document.close();
        } catch (Exception e) {
            System.err.println("Error creating PDF: " + e.getMessage());
            System.err.println("Error at line: " + e.getStackTrace()[0].getLineNumber());
        }
    }

}