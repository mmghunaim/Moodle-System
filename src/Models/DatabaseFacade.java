/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.sql.SQLException;
import java.util.Map;
import javafx.collections.ObservableList;

/**
 *
 * @author hp
 */
public class DatabaseFacade {
    private Student dbStudent;
    private Course dbCourse;
    private RegisteredCourses dbRegisteredCourses;
    private Section dbSection;
    
    private static DatabaseFacade databaseFacade;
    
    private DatabaseFacade(){
        this.dbStudent = new Student();
        this.dbSection = new Section();
        this.dbCourse = new Course();
        this.dbRegisteredCourses = new RegisteredCourses();
    }
    
    public static DatabaseFacade getDatabaseFacade(){
        if (databaseFacade == null) {
            databaseFacade = new DatabaseFacade();
        }
        return databaseFacade;
    }
    
    public ObservableList<Student> getStudent() throws ClassNotFoundException, SQLException{
        return dbStudent.getStudent();
    }
    
    public Map addStudent(String id, String name, String password, String phone, String address) throws ClassNotFoundException, SQLException{
        return dbStudent.addStudent(id, name, password, phone, address);
    }
    public String[] getStudentIds() throws SQLException, ClassNotFoundException{
        return dbStudent.getStudentIds();
    }
    public boolean deleteStudent(String studentName) throws SQLException, ClassNotFoundException{
        return dbStudent.deleteStudent(studentName);
    }
    public Map updateStudent(String stdid,String text,String updateType) throws SQLException, ClassNotFoundException{
        return dbStudent.updateStudent(stdid, text, updateType);
    }
    public Student showStudentBasicInfo() throws SQLException, ClassNotFoundException{
        return dbStudent.showStudentBasicInfo();
    }
    
    public ObservableList<Course> getCurrentCourses() throws ClassNotFoundException, SQLException{
        return dbCourse.getCurrentCourses();
    }
    public ObservableList<Course> grades(int semesterNumber) throws SQLException, ClassNotFoundException{
        return dbCourse.grades(semesterNumber);
    }
    
    public ObservableList<Section> getSections(String courseName) throws ClassNotFoundException, SQLException{
        return dbSection.getSections(courseName);
    }
    public int deleteSection(String sectionName , int sectionNumber) throws ClassNotFoundException, SQLException{
        return dbSection.deleteSection(sectionName, sectionNumber);
    }
    public Map updateSection(String sectionName , int sectionNumber,int preSectionNumber) throws ClassNotFoundException, SQLException{
        return dbSection.updateSection(sectionName, sectionNumber, preSectionNumber);
    }
    public Map addSection(String sectionName , int sectionNumber) throws ClassNotFoundException, SQLException{
        return dbSection.addSection(sectionName, sectionNumber);
    }
    public int[] getArrayofSections(String courseName) throws SQLException, ClassNotFoundException{
        return dbSection.getArrayofSections(courseName);
    }
    
    public ObservableList<RegisteredCourses> getRegisteredCourses(String studentId) throws ClassNotFoundException, SQLException{
        return dbRegisteredCourses.getRegisteredCourses(studentId);
    }
}
