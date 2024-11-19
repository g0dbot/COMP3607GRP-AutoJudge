// package com.hado90.pdfGenerator;
// import java.util.HashMap;  // Import HashMap
// import java.io.IOException;
// import org.apache.pegbox.pdmodel.font.PDType1Font;
// import org.apache.pdfbox.pdmodel.PDDocument;
// import org.apache.pdfbox.pdmodel.PDPage;
// import org.apache.pdfbox.pdmodel.PDPageContentStream;
// import java.util.List;

// public class PDFGenerator {

//     public void generatePDF(Student studentInfo, HashMap<String, HashMap<String, String>> testResults) {
//         // Modified path for saving PDF in Downloads folder
//         String path = "src\\main\\java\\com\\hado90\\";
//         String filename = path + studentInfo.getStudentId() + "_"
//                 + studentInfo.getFullName() + "_"
//                 + studentInfo.getAssignmentNumber() + "_Feedback.pdf";

//         try (PDDocument document = new PDDocument()) {
//             PDPage page = new PDPage();
//             document.addPage(page);

//             // Handles the first page - student info
//             PDPageContentStream contentStream = new PDPageContentStream(document, page);
//             contentStream.beginText();
//             contentStream.setFont(PDType1Font.HELVETICA, 24);
//             contentStream.setLeading(25.5f);
//             contentStream.newLineAtOffset(50, 720);
//             contentStream.showText("Student Name: " + studentInfo.getFullName());
//             contentStream.newLine();
//             contentStream.showText("Student ID: " + studentInfo.getStudentId());
//             contentStream.newLine();
//             contentStream.setFont(PDType1Font.HELVETICA, 16);
//             contentStream.showText("Course Name: " + studentInfo.getCourseTitle());
//             contentStream.newLine();
//             contentStream.showText("Course Code: " + studentInfo.getCourseCode());
//             contentStream.newLine();
//             contentStream.setFont(PDType1Font.HELVETICA, 12);
//             contentStream.showText("Assignment #: " + studentInfo.getAssignmentNumber());
//             contentStream.newLine();
//             contentStream.showText("Score: " + studentInfo.getScore() + "%");
//             contentStream.endText();
//             contentStream.close();

//             // Display the outcomes of all the test cases
//             PDPage page2 = new PDPage();
//             document.addPage(page2);
//             try (PDPageContentStream contentStream2 = new PDPageContentStream(document, page2)) {
//                 contentStream2.beginText();
//                 contentStream2.setFont(PDType1Font.HELVETICA, 20);  // Changed to HELVETICA font
//                 contentStream2.setLeading(25.5f);
//                 contentStream2.newLineAtOffset(50, 730);
//                 contentStream2.showText("Test Results");
//                 contentStream2.endText();

//                 int pageNum = 1;
//                 for (String className : testResults.keySet()) {
//                     if (pageNum > 1) {
//                         PDPage page3 = new PDPage();
//                         document.addPage(page3);
//                     }
//                     try (PDPageContentStream contentStream3 = new PDPageContentStream(document, document.getPage(pageNum))) {
//                         createTable(document, document.getPage(pageNum), testResults.get(className));
//                         contentStream3.beginText();
//                         contentStream3.setFont(PDType1Font.HELVETICA, 28);  // Changed to HELVETICA font
//                         contentStream3.setLeading(25.5f);
//                         contentStream3.newLineAtOffset(50, 650);
//                         contentStream3.showText(String.format("%s", className));
//                         contentStream3.endText();
//                     }
//                     pageNum++;
//                 }
//             }
//             document.save(filename);
//             System.out.println("PDF created successfully at " + filename);
//             document.close();
//         } catch (Exception e) {
//             System.err.println("Error creating PDF: " + e.getMessage());
//             System.err.println("Error at line: " + e.getStackTrace()[0].getLineNumber());
//         }
//     }

//     public static void createTable(PDDocument document, PDPage page, HashMap<String, String> testResults) throws IOException {
//         PDPageContentStream contentStream = new PDPageContentStream(document, page);
//         int columns = 3;
//         int rows = testResults.size() + 1;

//         // Set table headers
//         String[] headers = {"Test Case", "Status", "Comment"};

//         // Set table properties
//         float tableWidth = 500;
//         float tableHeight = 300;
//         float cellWidth = tableWidth / columns;
//         float cellHeight = tableHeight / rows;
//         float startX = 50;
//         float startY = 700;  // Starting point for the table

//         // Draw table grid
//         contentStream.setLineWidth(1f);
//         for (int i = 0; i <= rows; i++) {
//             contentStream.moveTo(startX, startY - i * cellHeight);
//             contentStream.lineTo(startX + tableWidth, startY - i * cellHeight);
//             contentStream.stroke();
//         }
//         for (int i = 0; i <= columns; i++) {
//             contentStream.moveTo(startX + i * cellWidth, startY);
//             contentStream.lineTo(startX + i * cellWidth, startY - tableHeight);
//             contentStream.stroke();
//         }

//         // Add data to table cells
//         contentStream.setFont(PDType1Font.HELVETICA, 12);  // Changed to HELVETICA font

//         // Adjusted Y-position for header row to provide more space from the top
//         float headerY = startY - 30;  // Slightly lower than before for a tiny bit more space

//         // Handle table headers (first row)
//         for (int col = 0; col < columns; col++) {
//             float x = startX + col * cellWidth + 5;  // Add padding inside the cell
//             float y = headerY;                      // Header position adjusted down
//             contentStream.beginText();
//             contentStream.newLineAtOffset(x, y);
//             contentStream.showText(headers[col]);
//             contentStream.endText();
//         }

//         // Handle table data (remaining rows)
//         for (int row = 1; row < rows; row++) {
//             int index = row - 1; // Skip header row
//             String[] testCase = new String[3];
//             testCase[0] = "Test" + (index + 1);
//             testCase[1] = "Pass";  // Example data
//             testCase[2] = "No issues"; // Example data

//             for (int col = 0; col < columns; col++) {
//                 float x = startX + col * cellWidth + 5;  // Padding inside the cell
//                 float y = startY - (row + 1) * cellHeight + 5; // Adjusted Y for correct positioning
//                 contentStream.beginText();
//                 contentStream.newLineAtOffset(x, y);
//                 contentStream.showText(testCase[col]);
//                 contentStream.endText();
//             }
//         }

//         contentStream.close();
//     }

// }

// // public class PDFGenerator {

// //     public void generatePDF(Student student, List<TestResult> testResults) {
// //         PDDocument document = new PDDocument();
// //         PDPage page = new PDPage();
// //         document.addPage(page);

// //         try {
// //             PDPageContentStream contentStream = new PDPageContentStream(document, page);
// //             contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
// //             contentStream.beginText();
// //             contentStream.newLineAtOffset(50, 750);  // Starting point of the text on the page

// //             // Add student info to the PDF
// //             contentStream.showText("Student ID: " + student.getStudentId());
// //             contentStream.newLine();
// //             contentStream.showText("Student Name: " + student.getFullName());
// //             contentStream.newLine();
// //             contentStream.showText("Course Title: Object Oriented Programming I");  // Hardcoded course title
// //             contentStream.newLine();
// //             contentStream.showText("Course Code: COMP3607");  // Hardcoded course code
// //             contentStream.newLine();
// //             contentStream.showText("Assignment Number: 1");  // Hardcoded assignment number
// //             contentStream.newLine();
// //             contentStream.newLine();  // Space before test results

// //             // Add test results
// //             contentStream.setFont(PDType1Font.HELVETICA, 10);
// //             contentStream.showText("Test Results:");
// //             contentStream.newLine();
// //             contentStream.newLine();  // Space before results

// //             // Loop through the test results and print them
// //             int totalScore = 0;
// //             int maxScore = 0;

// //             for (TestResult result : testResults) {
// //                 contentStream.showText("Test Case: " + result.getTestCaseName());
// //                 contentStream.newLine();
// //                 contentStream.showText("Status: " + result.getStatus());
// //                 contentStream.newLine();
// //                 contentStream.showText("Comment: " + result.getComment());
// //                 contentStream.newLine();
// //                 contentStream.showText("Score: " + result.getScore());
// //                 contentStream.newLine();
// //                 contentStream.newLine();  // Space between test results

// //                 // Add the scores for final calculation
// //                 totalScore += result.getScore();
// //                 maxScore += 10; // Assuming each test case is worth 10 points
// //             }

// //             // Add final score summary
// //             contentStream.showText("Total Score: " + totalScore + " out of " + maxScore);
// //             contentStream.newLine();
// //             contentStream.newLine();  // Add some space after final score

// //             contentStream.endText();
// //             contentStream.close();

// //             // Save the PDF document to a file
// //             document.save("TestResults.pdf");
// //             document.close();

// //         } catch (IOException e) {
// //             e.printStackTrace();
// //         }
// //     }
// // }

package com.hado90.pdfGenerator;

import java.util.HashMap; // Import HashMap
import java.io.IOException;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

public class PDFGenerator {

    public void generatePDF(Student studentInfo, HashMap<String, HashMap<String, String>> testResults) {
        // Modified path for saving PDF in Downloads folder
        String path = "src\\main\\java\\com\\hado90\\";
        String filename = path + studentInfo.getStudentId() + "_"
                + studentInfo.getFullName() + "_"
                + studentInfo.getAssignmentNumber() + "_Feedback.pdf";

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Handles the first page - student info
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 24);
            contentStream.setLeading(25.5f);
            contentStream.newLineAtOffset(50, 720);
            contentStream.showText("Student Name: " + studentInfo.getFullName());
            contentStream.newLine();
            contentStream.showText("Student ID: " + studentInfo.getStudentId());
            contentStream.newLine();
            contentStream.setFont(PDType1Font.HELVETICA, 16);
            contentStream.showText("Course Name: " + studentInfo.getCourseTitle());
            contentStream.newLine();
            contentStream.showText("Course Code: " + studentInfo.getCourseCode());
            contentStream.newLine();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.showText("Assignment #: " + studentInfo.getAssignmentNumber());
            contentStream.newLine();
            contentStream.showText("Score: " + studentInfo.getScore() + "%");
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
