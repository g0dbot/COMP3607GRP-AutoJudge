package com.hado90.pdfGenerator;

public class Student {
    private String firstName;
    private String lastName;
    private String studentId;
    private String courseTitle;
    private String courseCode;
    private String assignmentNumber;
    private String score;

    // Constructor
    public Student(String firstName, String lastName, String studentId, String courseTitle, String courseCode, String assignmentNumber, String score) {
        setFirstName(firstName);
        setLastName(lastName);
        setStudentId(studentId);
        setCourseTitle(courseTitle);
        setCourseCode(courseCode);
        setAssignmentNumber(assignmentNumber);
        setScore(score);
    }

    // Getters and setters for each field
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getAssignmentNumber() {
        return assignmentNumber;
    }

    public void setAssignmentNumber(String assignmentNumber) {
        this.assignmentNumber = assignmentNumber;
    }

    // Optional: method to return full name for easier reference
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {        
        this.score = score;
    }
}
