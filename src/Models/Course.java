/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import static Models.DBConnection.writeQuery;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author WH1108
 */
public class Course {
    private String courseName;
    private String courseId ; 
    private double grade ;
    
    String query;
    PreparedStatement prestatement;
    DBConnection connection = DBConnection.getConnection();
    ResultSet rs;
    
    public Course(){
        
    }
    public Course(String courseName, String courseId) {
        this.courseName = courseName;
        this.courseId = courseId;
    }
    public Course(String courseName,double grade) {
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
    
    
    public ObservableList<Course> getCurrentCourses() throws ClassNotFoundException, SQLException{
        //getStatement();
        Calendar cal = Calendar.getInstance(); 
        int semesterNumber ;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month  ;
        if (Calendar.getInstance().get(Calendar.MONTH)+1<6) {
            semesterNumber = 2 ;
        }else{
            semesterNumber = 1 ;
        }
        ObservableList courseList =
                FXCollections.observableArrayList();
        query = "SELECT c.name,c.id FROM section s ,semester sm , course c "
                        + "WHERE s.coursename=c.name AND s.semesterid=sm.id "
                        + "AND sm.number= ? AND sm.year=? "
                        + "AND s.sectionnumber='101' ORDER BY c.name";
        writeQuery(query);
        prestatement = connection.
                getStatement("SELECT c.name,c.id FROM section s ,semester sm , course c "
                        + "WHERE s.coursename=c.name AND s.semesterid=sm.id "
                        + "AND sm.number= ? AND sm.year=? "
                        + "AND s.sectionnumber='101' ORDER BY c.name");
        prestatement.setInt(1, semesterNumber);
        prestatement.setString(2, String.valueOf(year));
        rs = prestatement.executeQuery();
        while(rs.next()){
            Course course = new Course(rs.getString("name"),rs.getString("id"));
            courseList.add(course);
        }
        
        return courseList;
    }
    
    public ObservableList<Course> grades(int semesterNumber) throws SQLException, ClassNotFoundException{
        ObservableList grades =FXCollections.observableArrayList();
        String year ;
        if (semesterNumber==1) {
            year = "2017";
        }else if(semesterNumber==2) {
            year = "2018";
        }else if(semesterNumber==3) {
            year = "2018";
        }else{
            year = "2019";
        }
        
        String studentId = connection.getLoggedStudent();
        query = "SELECT * "
                        + "FROM precourses pc ,semester sm "
                        + "WHERE pc.studentid=? AND pc.semesterid=sm.id AND sm.year=? AND sm.number=?";
        writeQuery(query);
        prestatement = connection.getStatement(
                "SELECT * "
                        + "FROM precourses pc ,semester sm "
                        + "WHERE pc.studentid=? AND pc.semesterid=sm.id AND sm.year=? AND sm.number=?");
        prestatement.setString(1, connection.getLoggedStudent());
        prestatement.setString(2, year);
        prestatement.setInt(3, semesterNumber);
        
        rs = prestatement.executeQuery();
        
        while(rs.next()){
            Course grade = new Course(rs.getString("courseid"),rs.getDouble("grade"));
            grades.add(grade);
        }
        return grades ;
    }
    
    
}
