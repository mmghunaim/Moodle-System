/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import static Models.DBConnection.md5Password;
import static Models.DBConnection.writeQuery;
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
    String studentLName;
    private String studentId;
    private String studentPhone;
    private String studentAddress;
    private String studentLastName;
    private String studentEmail;
    
    private Map statusMap;
    
    String query;
    PreparedStatement prestatement;
    DBConnection connection = DBConnection.getConnection();
    private String stdId="";
    boolean firstTime = true;
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
    
    public ObservableList<Student> getStudent() throws ClassNotFoundException, SQLException{
        DBConnection connection = DBConnection.getConnection();
        ObservableList studentList =
                FXCollections.observableArrayList();
        //getStatement();
        String query = "SELECT * FROM student";
        writeQuery(query);
        PreparedStatement prestatement = connection.getStatement("SELECT * FROM student");
        ResultSet rs = prestatement.executeQuery();
        while(rs.next()){
            Student student = new Student(rs.getString("name"), rs.getString("id"),
                    rs.getString("phone"), rs.getString("address"));
            studentList.add(student);
        }
        return studentList;
    }
    
    public Map addStudent(String id,String name,String password,String phone,String address) throws ClassNotFoundException, SQLException{
        DBConnection connection = DBConnection.getConnection();
        boolean isAdded = false ;
        statusMap = new HashMap();
        statusMap.put("isAdded", false);
        statusMap.put("isExist",false);
        statusMap.put("enterError",false);
        //getStatement();
        try{
            if (!id.equalsIgnoreCase("")&&!name.equalsIgnoreCase("")&&!password.equalsIgnoreCase("")
                    &&!phone.equalsIgnoreCase("")&&!address.equalsIgnoreCase("")) {
                String query = "SELECT id FROM student WHERE id=?";
                writeQuery(query);
                PreparedStatement prestatement = connection.getStatement("SELECT id FROM student WHERE id=?");
                prestatement.setString(1, id);
                ResultSet rs = prestatement.executeQuery();
                if (rs.next()) {
                    statusMap.put("isExist",true);
                }else{
                    query = "INSERT INTO student VALUES(id,name,phone,address,password)";
                    writeQuery(query);
                    prestatement = connection.getStatement("INSERT INTO student(id,name,phone,address,password,email,lname) VALUES(?,?,?,?,?,?,?)");
                    prestatement.setString(1, id);
                    prestatement.setString(2, name);
                    prestatement.setString(3, phone);
                    prestatement.setString(4, address);
                    prestatement.setString(5, md5Password(password));
                    prestatement.setString(6, "email");
                    prestatement.setString(7, "last name");
                    
                    int addedd = prestatement.executeUpdate();
                    if (addedd!=0) {
                        statusMap.put("isAdded", true);
                    }
                }
            }else{
                statusMap.put("enterError",true);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("error");
        }
        return statusMap;
    }
    

    public String[] getStudentIds() throws SQLException, ClassNotFoundException{
        DBConnection connection = DBConnection.getConnection();
        int length = 0 ;
        String query = "SELECT id FROM student";
        writeQuery(query);
        PreparedStatement prestatement =connection.getStatement("SELECT id FROM student");
        ResultSet rs = prestatement.executeQuery();
        while(rs.next()){
            length++;
        }
        rs.beforeFirst();
        int count =0;
        String[] studentsID  = new String[length];
        while(rs.next()){
            studentsID[count]=rs.getString("id");
            count++;
        }
        return studentsID;
        
    }
    
    
    public boolean deleteStudent(String studentName) throws SQLException, ClassNotFoundException{
        boolean isDeleted = false;
        query = "DELETE FROM student WHERE id=?";
        writeQuery(query);
        prestatement = connection.getStatement("DELETE FROM student WHERE id=?");
        prestatement.setString(1, studentName);
        int deleted = prestatement.executeUpdate();
        if (deleted!=0) {
            isDeleted=true;
        }
        return isDeleted;
    }
    
    //db student
    public Map updateStudent(String stdid,String text,String updateType) throws SQLException, ClassNotFoundException{
        statusMap = new HashMap();
        statusMap.put("isUpdated", false);
        statusMap.put("duplicatedOccurred", true);
        
        if (updateType.equalsIgnoreCase("id")) {
            query = "SELECT id FROM student WHERE id=?";
            writeQuery(query);
            prestatement = connection.getStatement("SELECT id FROM student WHERE id=?");
            prestatement.setString(1, text);
            ResultSet result = prestatement.executeQuery();
            if (result.next()) {
                statusMap.put("duplicatedOccurred", true);
            }else{
                statusMap.put("duplicatedOccurred", false);
                query = "UPDATE student SET id=? WHERE id=?";
                writeQuery(query);
                prestatement = connection.getStatement("UPDATE student SET id=? WHERE id=?");
            }
            
        }else if(updateType.equalsIgnoreCase("name")){
            statusMap.put("duplicatedOccurred", false);
            query = "UPDATE student SET name=? WHERE id=?";
            writeQuery(query);
            prestatement = connection.getStatement("UPDATE student SET name=? WHERE id=?");
        }else if(updateType.equalsIgnoreCase("phone")){
            statusMap.put("duplicatedOccurred", false);
            query = "UPDATE student SET phone=? WHERE id=?";
            writeQuery(query);
            prestatement = connection.getStatement("UPDATE student SET phone=? WHERE id=?");
        }else if(updateType.equalsIgnoreCase("address")){
            statusMap.put("duplicatedOccurred", false);
            query = "UPDATE student SET address=? WHERE id=?";
            writeQuery(query);
            prestatement = connection.getStatement("UPDATE student SET address=? WHERE id=?");
        }
        if (!(boolean)statusMap.get("duplicatedOccurred")) {
            prestatement.setString(1, text);
            prestatement.setString(2, stdid);
            int updated = prestatement.executeUpdate();
            if (updated!=0) {
                statusMap.put("isUpdated", true);
            }
        }
        return statusMap;
    }
    
    public Student showStudentBasicInfo() throws SQLException, ClassNotFoundException{
        Student student = new Student() ;
        if (firstTime) {
            firstTime = false;
            query = "SELECT * FROM student WHERE id=?";
            writeQuery(query);
            
            PreparedStatement prestatement =connection.getStatement("SELECT * FROM student WHERE id=?");
            prestatement.setString(1, connection.getLoggedStudent());
            ResultSet re = prestatement.executeQuery();
            if(re.next()) {
                studentId=re.getString("id");
                studentName=re.getString("name");
                studentLName=re.getString("lname");
                studentEmail=re.getString("email");
                studentAddress=re.getString("address");
                studentPhone=re.getString("phone");      
            } 
        }
        student.setStudentId(studentId);
        student.setStudentName(studentName);
        student.setStudentLastName(studentLName);
        student.setStudentEmail(studentEmail);
        student.setStudentAddress(studentAddress);
        student.setStudentPhone(studentPhone);
        System.out.println("HERE WE ARE "+this.getStudentName());
        return student;
        
    }
    
}
