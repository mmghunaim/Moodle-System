/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.ArrayList;
import java.util.Map;
import javafx.collections.ObservableList;

/**
 *
 * @author mmghunaim
 */
public class MongoFacade {

    MongoConnection mongoConnection ;
    private static MongoFacade mongoFacade;

    private MongoAdmin admin;
    private MongoStudent student;

    private MongoFacade() {
        mongoConnection = MongoConnection.getMongoConnection();
        ConnectionState openConnection = new OpenConnection();
        mongoConnection.setConnection(openConnection.getStateOfMongoConnection(mongoConnection));
        this.admin = new MongoAdmin();
        this.student=new MongoStudent();
    }

    public static MongoFacade getMongoFacade() {
        if (mongoFacade == null) {
            mongoFacade = new MongoFacade();
        }
        return mongoFacade;
    }
    
    public void closeConnection(){
        ConnectionState closeConnection = new CloseConnection();
       mongoConnection.setConnection(closeConnection.getStateOfMongoConnection(mongoConnection));
    }

    public ObservableList<Student> getStudent() throws ClassNotFoundException {
        return admin.getStudent();
    }

    public Map addStudent(String id, String name, String password, String phone, String address) {
        return admin.addStudent(id, name, password, phone, address);
    }

    public ArrayList getStudentIds() {
        return admin.getStudentIds();
    }

    public boolean deleteStudent(String studentName) {
        return admin.deleteStudent(studentName);
    }

    public Map updateStudent(String stdid, String text, String updateType) {
        return admin.updateStudent(stdid, text, updateType);
    }

    public Student showStudentBasicInfo() {
        return admin.showStudentBasicInfo();
    }
    
    public ObservableList<Course> getCurrentCourses() {
        return student.getCurrentCourses();
    }
    public ObservableList<Course> grades(int semesterNumber){
        return student.grades(semesterNumber);
    }
    public ObservableList<RegisteredCourses> getRegisteredCourses(String studentId) {
        return student.getRegisteredCourses(studentId);
    }
    public ObservableList<Section> getSections(String courseName){
        return student.getSections(courseName);
    }
    public int deleteSection(String sectionName, int sectionNumber){
        return student.deleteSection(sectionName, sectionNumber);
    }
    public Map updateSection(String sectionName, int sectionNumber, int preSectionNumber){
        return student.updateSection(sectionName, sectionNumber, preSectionNumber);
    }
    public Map addSection(String sectionName, int sectionNumber){
        return student.addSection(sectionName, sectionNumber);
    }
    public ArrayList getArrayofSections(String courseName) {
        return student.getArrayofSections(courseName);
    }
}
