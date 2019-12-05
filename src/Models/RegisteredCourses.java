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
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author WH1108
 */
public class RegisteredCourses {

    private String studentid;
    private String coursename;
    private int sectionnumber;
    private String starttime;
    private String endtime;
    private String instructor;
    private String lab;
    private String days;

    String query;

    PreparedStatement prestatement;
    DBConnection connection;

    public RegisteredCourses(String studentid, String coursename, int sectionnumber, String starttime,
            String endtime, String instructor, String lab, String days) {
        this.studentid = studentid;
        this.coursename = coursename;
        this.sectionnumber = sectionnumber;
        this.starttime = starttime;
        this.endtime = endtime;
        this.instructor = instructor;
        this.lab = lab;
        this.days = days;
    }

    public RegisteredCourses(String coursename, int sectionnumber) {
        this.coursename = coursename;
        this.sectionnumber = sectionnumber;
    }

    public RegisteredCourses(DBConnection connection) {
        this.connection = connection;
    }

    public String getStudentid() {
        return studentid;
    }

    public String getCoursename() {
        return coursename;
    }

    public int getSectionnumber() {
        return sectionnumber;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getLab() {
        return lab;
    }

    public String getDays() {
        return days;
    }

    public ObservableList<RegisteredCourses> getRegisteredCourses(String studentId) throws ClassNotFoundException, SQLException {
        ObservableList registeredCoursesList
                = FXCollections.observableArrayList();
        query = "SELECT * FROM regcourses WHERE studentid=?";
        writeQuery(connection.getLoggedStudent(), query);
        prestatement = connection.getStatement("SELECT * FROM regcourses WHERE studentid=?");
        if (studentId == null) {
            prestatement.setString(1, connection.getLoggedStudent());
        } else {
            prestatement.setString(1, studentId);
        }
        prestatement.setString(1, connection.getLoggedStudent());
        connection.setResultSet(prestatement.executeQuery());

        while (connection.getResultSet().next()) {
            if (studentId == null) {
                RegisteredCourses registeredCourses
                        = new RegisteredCourses(
                                connection.getResultSet().getString("studentid"),
                                connection.getResultSet().getString("coursename"),
                                connection.getResultSet().getInt("sectionnumber"),
                                connection.getResultSet().getString("starttime"),
                                connection.getResultSet().getString("endtime"),
                                connection.getResultSet().getString("instructor"),
                                connection.getResultSet().getString("lab"),
                                connection.getResultSet().getString("days")
                        );
                registeredCoursesList.add(registeredCourses);
            } else {
                RegisteredCourses registeredCourses
                        = new RegisteredCourses(
                                connection.getResultSet().getString("coursename"),
                                connection.getResultSet().getInt("sectionnumber"));
                registeredCoursesList.add(registeredCourses);
            }
        }
        return registeredCoursesList;
    }

}
