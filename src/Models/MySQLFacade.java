/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import javafx.collections.ObservableList;

/**
 *
 * @author mmghunaim
 */
public class MySQLFacade {

    private MySQLAdmin admin;
    private MySQLStudent student;

    private static MySQLFacade databaseFacade;
    private MySQLConnection dbConnection = MySQLConnection.getDbConnection();

    private MySQLFacade() {
        ConnectionState openConnection = new OpenConnection();
        dbConnection.setConnection(openConnection.getStateOfConnection(dbConnection));
        
        this.admin = new MySQLAdmin();
        this.student = new MySQLStudent();
    }

    public static MySQLFacade getDatabaseFacade() {
        if (databaseFacade == null) {
            databaseFacade = new MySQLFacade();
        }
        return databaseFacade;
    }

    public void closeConnection() {
        ConnectionState closeConnection = new CloseConnection();
        dbConnection.setConnection(closeConnection.getStateOfConnection(dbConnection));

    }

    public ObservableList<Student> getStudent() throws ClassNotFoundException, SQLException {
        return admin.getStudent();
    }

    public Map addStudent(String id, String name, String password, String phone, String address) throws ClassNotFoundException, SQLException {
        return admin.addStudent(id, name, password, phone, address);
    }

    public ArrayList getStudentIds() throws SQLException, ClassNotFoundException {
        return admin.getStudentIds();
    }

    public boolean deleteStudent(String studentName) throws SQLException, ClassNotFoundException {
        return admin.deleteStudent(studentName);
    }

    public Map updateStudent(String stdid, String text, String updateType) throws SQLException, ClassNotFoundException {
        return admin.updateStudent(stdid, text, updateType);
    }

    public Student showStudentBasicInfo() throws SQLException, ClassNotFoundException {
        return admin.showStudentBasicInfo();
    }

    public ObservableList<Course> getCurrentCourses() throws ClassNotFoundException, SQLException {
        return student.getCurrentCourses();
    }

    public ObservableList<Course> grades(int semesterNumber) throws SQLException, ClassNotFoundException {
        return student.grades(semesterNumber);
    }

    public ObservableList<Section> getSections(String courseName) throws ClassNotFoundException, SQLException {
        return student.getSections(courseName);
    }

    public int deleteSection(String sectionName, int sectionNumber) throws ClassNotFoundException, SQLException {
        return student.deleteSection(sectionName, sectionNumber);
    }

    public Map updateSection(String sectionName, int sectionNumber, int preSectionNumber) throws ClassNotFoundException, SQLException {
        return student.updateSection(sectionName, sectionNumber, preSectionNumber);
    }

    public Map addSection(String sectionName, int sectionNumber) throws ClassNotFoundException, SQLException {
        return student.addSection(sectionName, sectionNumber);
    }

    public int[] getArrayofSections(String courseName) throws SQLException, ClassNotFoundException {
        return student.getArrayofSections(courseName);
    }

    public ObservableList<RegisteredCourses> getRegisteredCourses(String studentId) throws ClassNotFoundException, SQLException {
        return student.getRegisteredCourses(studentId);
    }
}
