/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import static Models.DBConnection.md5Password;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author WH1108
 */
public class Student {

    private String studentName;
    private String studentLName;
    private String studentId;
    private String studentPhone;
    private String studentAddress;
    private String studentLastName;
    private String studentEmail;
    private Map statusMap;
    String query;
    PreparedStatement prestatement;
    DBConnection connection = DBConnection.getDbConnection();
    private String stdId = "";
    boolean firstTime = true;

    public Student() {
    }

    public void closeConnection() {
        connection.closeConnection();
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

    public ObservableList<Student> getStudent() throws ClassNotFoundException, SQLException {
        ObservableList studentList
                = FXCollections.observableArrayList();
        //getStatement();
        String query = "SELECT * FROM student";
        connection.writeQuery("admin", query);
        prestatement = connection.getStatement("SELECT * FROM student");
        connection.setResultSet(prestatement.executeQuery());
        while (connection.getResultSet().next()) {
            Student student
                    = new Student(
                            connection.getResultSet().getString("name"),
                            connection.getResultSet().getString("id"),
                            connection.getResultSet().getString("phone"),
                            connection.getResultSet().getString("address")
                    );
            studentList.add(student);
        }
        return studentList;
    }

    public Map addStudent(String id, String name, String password, String phone, String address) throws ClassNotFoundException, SQLException {
        boolean isAdded = false;
        statusMap = new HashMap();
        statusMap.put("isAdded", false);
        statusMap.put("isExist", false);
        statusMap.put("enterError", false);
        //getStatement();
        try {
            if (!id.equalsIgnoreCase("") && !name.equalsIgnoreCase("") && !password.equalsIgnoreCase("")
                    && !phone.equalsIgnoreCase("") && !address.equalsIgnoreCase("")) {
                String query = "SELECT id FROM student WHERE id=?";
                connection.writeQuery("admin", query);
                prestatement = connection.getStatement("SELECT id FROM student WHERE id=?");
                prestatement.setString(1, id);
                connection.setResultSet(prestatement.executeQuery());
                if (connection.getResultSet().next()) {
                    statusMap.put("isExist", true);
                } else {
                    query = "INSERT INTO student VALUES(id,name,phone,address,password)";
                    connection.writeQuery("admin", query);
                    prestatement = connection.getStatement("INSERT INTO student(id,name,phone,address,password,email,lname) VALUES(?,?,?,?,?,?,?)");
                    prestatement.setString(1, id);
                    prestatement.setString(2, name);
                    prestatement.setString(3, phone);
                    prestatement.setString(4, address);
                    prestatement.setString(5, md5Password(password));
                    prestatement.setString(6, "email");
                    prestatement.setString(7, "last name");

                    int addedd = prestatement.executeUpdate();
                    if (addedd != 0) {
                        statusMap.put("isAdded", true);
                    }
                }
            } else {
                statusMap.put("enterError", true);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("error");
        }
        return statusMap;
    }

    public String[] getStudentIds() throws SQLException, ClassNotFoundException {
        int length = 0;
        String query = "SELECT id FROM student";
        connection.writeQuery("admin", query);
        prestatement = connection.getStatement("SELECT id FROM student");
        connection.setResultSet(prestatement.executeQuery());
        while (connection.getResultSet().next()) {
            length++;
        }
        connection.getResultSet().beforeFirst();
        int count = 0;
        String[] studentsID = new String[length];
        while (connection.getResultSet().next()) {
            studentsID[count] = connection.getResultSet().getString("id");
            count++;
        }
        return studentsID;

    }

    public boolean deleteStudent(String studentName) throws SQLException, ClassNotFoundException {
        boolean isDeleted = false;
        query = "DELETE FROM student WHERE id=?";
        connection.writeQuery("admin", query);
        prestatement = connection.getStatement("DELETE FROM student WHERE id=?");
        prestatement.setString(1, studentName);
        int deleted = prestatement.executeUpdate();
        if (deleted != 0) {
            isDeleted = true;
        }
        return isDeleted;
    }

    //db student
    public Map updateStudent(String stdid, String text, String updateType) throws SQLException, ClassNotFoundException {
        statusMap = new HashMap();
        statusMap.put("isUpdated", false);
        statusMap.put("duplicatedOccurred", true);

        if (updateType.equalsIgnoreCase("id")) {
            query = "SELECT id FROM student WHERE id=?";
            connection.writeQuery("admin", query);
            prestatement = connection.getStatement("SELECT id FROM student WHERE id=?");
            prestatement.setString(1, text);
            connection.setResultSet(prestatement.executeQuery());
            if (connection.getResultSet().next()) {
                statusMap.put("duplicatedOccurred", true);
            } else {
                statusMap.put("duplicatedOccurred", false);
                query = "UPDATE student SET id=? WHERE id=?";
                connection.writeQuery("admin", query);
                prestatement = connection.getStatement("UPDATE student SET id=? WHERE id=?");
            }

        } else if (updateType.equalsIgnoreCase("name")) {
            statusMap.put("duplicatedOccurred", false);
            query = "UPDATE student SET name=? WHERE id=?";
            connection.writeQuery("admin", query);
            prestatement = connection.getStatement("UPDATE student SET name=? WHERE id=?");
        } else if (updateType.equalsIgnoreCase("phone")) {
            statusMap.put("duplicatedOccurred", false);
            query = "UPDATE student SET phone=? WHERE id=?";
            connection.writeQuery("admin", query);
            prestatement = connection.getStatement("UPDATE student SET phone=? WHERE id=?");
        } else if (updateType.equalsIgnoreCase("address")) {
            statusMap.put("duplicatedOccurred", false);
            query = "UPDATE student SET address=? WHERE id=?";
            connection.writeQuery("admin", query);
            prestatement = connection.getStatement("UPDATE student SET address=? WHERE id=?");
        }
        if (!(boolean) statusMap.get("duplicatedOccurred")) {
            prestatement.setString(1, text);
            prestatement.setString(2, stdid);
            int updated = prestatement.executeUpdate();
            if (updated != 0) {
                statusMap.put("isUpdated", true);
            }
        }
        return statusMap;
    }

    public Student showStudentBasicInfo() throws SQLException, ClassNotFoundException {
        Student student = new Student();
        if (firstTime) {
            firstTime = false;
            query = "SELECT * FROM student WHERE id=?";

            prestatement = connection.getStatement("SELECT * FROM student WHERE id=?");
            prestatement.setString(1, connection.getLoggedStudent());
            connection.setResultSet(prestatement.executeQuery());
            if (connection.getResultSet().next()) {
                studentId = connection.getResultSet().getString("id");
                studentName = connection.getResultSet().getString("name");
                studentLName = connection.getResultSet().getString("lname");
                studentEmail = connection.getResultSet().getString("email");
                studentAddress = connection.getResultSet().getString("address");
                studentPhone = connection.getResultSet().getString("phone");
            }
            connection.writeQuery(studentName, query);
        }
        student.setStudentId(studentId);
        student.setStudentName(studentName);
        student.setStudentLastName(studentLName);
        student.setStudentEmail(studentEmail);
        student.setStudentAddress(studentAddress);
        student.setStudentPhone(studentPhone);
        System.out.println("HERE WE ARE " + this.getStudentName());
        return student;

    }

}
