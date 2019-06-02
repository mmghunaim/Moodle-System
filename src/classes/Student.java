/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

/**
 *
 * @author WH1108
 */
public class Student {
    private String studentName;
    private String studentId;
    private String studentPhone;
    private String studentAddress;
    private String studentLastName;
    private String studentEmail;
    
    public Student() {
    }
    
    public Student(String studentName, String studentId, String studentPhone, String studentAddress) {
        this.studentName = studentName;
        this.studentId = studentId;
        this.studentPhone = studentPhone;
        this.studentAddress = studentAddress;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentPhone() {
        return studentPhone;
    }

    public String getStudentAddress() {
        return studentAddress;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setStudentPhone(String studentPhone) {
        this.studentPhone = studentPhone;
    }

    public void setStudentAddress(String studentAddress) {
        this.studentAddress = studentAddress;
    }

    public void setStudentLastName(String studentLastName) {
        this.studentLastName = studentLastName;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }
    
}
