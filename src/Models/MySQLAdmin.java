/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import static Models.MySQLConnection.md5Password;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author mmghunaim
 */
public class MySQLAdmin {

    private Map statusMap;
    String query;
    PreparedStatement prestatement;
    MySQLConnection connection = MySQLConnection.getDbConnection();
    private String stdId = "";
    boolean firstTime = true;

    public ObservableList<Student> getStudent() throws ClassNotFoundException, SQLException {
        ObservableList studentList
                = FXCollections.observableArrayList();
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

    public ArrayList getStudentIds() throws SQLException, ClassNotFoundException {
        ArrayList<String> studentsId = new ArrayList<>();
        String query = "SELECT id FROM student";
        connection.writeQuery("admin", query);
        prestatement = connection.getStatement(query);
        connection.setResultSet(prestatement.executeQuery());

        while (connection.getResultSet().next()) {
            studentsId.add(connection.getResultSet().getString("id"));
        }
        return studentsId;

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
        System.out.println("Here The MySQLStudent class "+student);
        if (firstTime) {
            firstTime = false;
            query = "SELECT * FROM student WHERE id=?";

            prestatement = connection.getStatement("SELECT * FROM student WHERE id=?");
            prestatement.setString(1, connection.getLoggedStudent());
            connection.setResultSet(prestatement.executeQuery());
            if (connection.getResultSet().next()) {
                student.setStudentId(connection.getResultSet().getString("id"));
                student.setStudentName(connection.getResultSet().getString("name"));
                student.setStudentLastName(connection.getResultSet().getString("lname"));
                student.setStudentEmail(connection.getResultSet().getString("email"));
                student.setStudentAddress(connection.getResultSet().getString("address"));
                student.setStudentPhone(connection.getResultSet().getString("phone"));
            }
        }
        
        return student;
    }
}
