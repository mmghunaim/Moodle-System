/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author mmghunaim
 */
public class Course {

    private String courseName;
    private String courseId;
    private double grade;

    

    public Course() {

    }

    public Course(String courseName, String courseId) {
        this.courseName = courseName;
        this.courseId = courseId;
    }

    public Course(String courseName, double grade) {
        this.courseName = courseName;
        this.courseId = courseId;
        this.grade = grade;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public double getGrade() {
        return grade;
    }
    
}
