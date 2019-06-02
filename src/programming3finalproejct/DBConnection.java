package programming3finalproejct;

import classes.Course;
import classes.RegisteredCourses;
import classes.Section;
import classes.Student;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DBConnection {
    private static  DBConnection dbConnection;
    private Connection connection ;
    private ResultSet rs;
    private PreparedStatement prestatement ;
    boolean firstTime = true;
    String studentId = "";
    String studentName = "";
    String studentLName = "";
    String studentEmail = "";
    String studentAddress = "";
    String studentPhone = "";
        
    private Map statusMap;
    private String stdId="";
    private String query ;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private DBConnection() {
    }
    
    public static DBConnection getConnection(){
        if (dbConnection==null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }
    
    private void getStatement() throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/registration",
                "root", "secret");
    }
    public void closeConnection(){
        try {
            this.prestatement.close();
            this.connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public static void writeQuery(String query){
        
        Date date = new Date();
        File file = new File("./src/programming3finalproejct/logFile.txt");
        if (file.exists()) {
            try {
                FileWriter fileWriter = new FileWriter(file,true);
                PrintWriter printWriter = new PrintWriter(fileWriter);
                printWriter.write(query+"\t\tDATE ::"+dateFormat.format(date)+"\n");
            printWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } 
    }
    public Map verifyLogin(String id, String password) throws ClassNotFoundException{
        statusMap = new HashMap();
        statusMap.put("isNotEmpty", false);
        statusMap.put("loginType", "");
        statusMap.put("failedLogin", false);
        try {
            getStatement();
            if (id!=null && !id.isEmpty() && password!=null && !password.isEmpty()) {
                statusMap.put("isNotEmpty", true);
                if (id.equalsIgnoreCase("admin")&&password.equalsIgnoreCase("admin")) {
                    statusMap.put("loginType", "admin");
                }else{
                    query = "SELECT * FROM student WHERE id=? AND password = ?";
                    writeQuery(query);
                    prestatement = connection.prepareStatement("SELECT * FROM student WHERE id=? AND password = ?");
                    prestatement.setString(1, id);
                    prestatement.setString(2, md5Password(password));
                    rs = prestatement.executeQuery();
                    if (rs.next()) {
                        stdId = id;
                        statusMap.put("loginType", "student");
                        statusMap.put("failedLogin", false);
                        statusMap.put("studentid", id);
                    }else{
                        System.out.println(md5Password(password));
                        statusMap.put("failedLogin", true);
                    }
                }
            }else{
                statusMap.put("isNotEmpty", false);
            }
        } catch (Exception ex) {
            ex.addSuppressed(ex);
        }
        return statusMap;
    }
    
    public ObservableList<Student> getStudent() throws ClassNotFoundException, SQLException{
        ObservableList studentList =
                FXCollections.observableArrayList();
        //getStatement();
        query = "SELECT * FROM student";
        writeQuery(query);
        prestatement = connection.prepareStatement("SELECT * FROM student");
        rs = prestatement.executeQuery();
        while(rs.next()){
            Student student = new Student(rs.getString("name"), rs.getString("id"),
                    rs.getString("phone"), rs.getString("address"));
            studentList.add(student);
        }
        return studentList;
    }
    public String[] getStudentIds() throws SQLException{
        int length = 0 ;
        query = "SELECT id FROM student";
        writeQuery(query);
        prestatement =connection.prepareStatement("SELECT id FROM student");
        rs = prestatement.executeQuery();
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
    public boolean deleteStudent(String studentName) throws SQLException{
        boolean isDeleted = false;
        query = "DELETE FROM student WHERE id=?";
        writeQuery(query);
        prestatement = connection.prepareStatement("DELETE FROM student WHERE id=?");
        prestatement.setString(1, studentName);
        int deleted = prestatement.executeUpdate();
        if (deleted!=0) {
            isDeleted=true;
        }
        return isDeleted;
    }
    public Map updateStudent(String stdid,String text,String updateType) throws SQLException{
        statusMap = new HashMap();
        statusMap.put("isUpdated", false);
        statusMap.put("duplicatedOccurred", true);
        
        if (updateType.equalsIgnoreCase("id")) {
            query = "SELECT id FROM student WHERE id=?";
            writeQuery(query);
            prestatement = connection.prepareStatement("SELECT id FROM student WHERE id=?");
            prestatement.setString(1, text);
            ResultSet result = prestatement.executeQuery();
            if (result.next()) {
                statusMap.put("duplicatedOccurred", true);
            }else{
                statusMap.put("duplicatedOccurred", false);
                query = "UPDATE student SET id=? WHERE id=?";
                writeQuery(query);
                prestatement = connection.prepareStatement("UPDATE student SET id=? WHERE id=?");
            }
            
        }else if(updateType.equalsIgnoreCase("name")){
            statusMap.put("duplicatedOccurred", false);
            query = "UPDATE student SET name=? WHERE id=?";
            writeQuery(query);
            prestatement = connection.prepareStatement("UPDATE student SET name=? WHERE id=?");
        }else if(updateType.equalsIgnoreCase("phone")){
            statusMap.put("duplicatedOccurred", false);
            query = "UPDATE student SET phone=? WHERE id=?";
            writeQuery(query);
            prestatement = connection.prepareStatement("UPDATE student SET phone=? WHERE id=?");
        }else if(updateType.equalsIgnoreCase("address")){
            statusMap.put("duplicatedOccurred", false);
            query = "UPDATE student SET address=? WHERE id=?";
            writeQuery(query);
            prestatement = connection.prepareStatement("UPDATE student SET address=? WHERE id=?");
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
    
    public Map addStudent(String id,String name,String password,String phone,String address) throws ClassNotFoundException, SQLException{
        boolean isAdded = false ;
        statusMap = new HashMap();
        statusMap.put("isAdded", false);
        statusMap.put("isExist",false);
        statusMap.put("enterError",false);
        //getStatement();
        try{
            if (!id.equalsIgnoreCase("")&&!name.equalsIgnoreCase("")&&!password.equalsIgnoreCase("")
                    &&!phone.equalsIgnoreCase("")&&!address.equalsIgnoreCase("")) {
                query = "SELECT id FROM student WHERE id=?";
                writeQuery(query);
                prestatement = connection.prepareStatement("SELECT id FROM student WHERE id=?");
                prestatement.setString(1, id);
                rs = prestatement.executeQuery();
                if (rs.next()) {
                    statusMap.put("isExist",true);
                }else{
                    query = "INSERT INTO student VALUES(id,name,phone,address,password)";
                    writeQuery(query);
                    prestatement = connection.prepareStatement("INSERT INTO student(id,name,phone,address,password,email,lname) VALUES(?,?,?,?,?,?,?)");
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
    
    public Student showStudentBasicInfo() throws SQLException{
        
        Student student = new Student() ;
        if (firstTime) {
            query = "SELECT * FROM student WHERE id=?";
            writeQuery(query);
            prestatement.setString(1, stdId);
            ResultSet re = prestatement.executeQuery();
            if(re.next()) {
                studentId=re.getString("id");
                studentName=re.getString("name");
                studentLName=re.getString("lname");
                studentEmail=re.getString("email");
                studentAddress=re.getString("address");
                studentPhone=re.getString("phone");      
            }
            firstTime = false;
        }
        student.setStudentId(studentId);
        student.setStudentName(studentName);
        student.setStudentLastName(studentLName);
        student.setStudentEmail(studentEmail);
        student.setStudentAddress(studentAddress);
        student.setStudentPhone(studentPhone);
        return student;
        
    }
    public ObservableList<RegisteredCourses> getReisteredCourses(String studentId) throws SQLException{
        ObservableList studentCourses =
                FXCollections.observableArrayList();
        query = "SELECT coursename,sectionnumber FROM regcourses WHERE studentid=?";
        writeQuery(query);
        prestatement = connection.prepareStatement("SELECT coursename,sectionnumber FROM regcourses WHERE studentid=?");
        prestatement.setString(1, studentId);
        rs = prestatement.executeQuery();
        while(rs.next()){
            RegisteredCourses src = new RegisteredCourses(rs.getString("coursename"),rs.getInt("sectionnumber"));
            studentCourses.add(src);
        }
        return studentCourses;
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
                prepareStatement("SELECT c.name,c.id FROM section s ,semester sm , course c "
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
    
    public ObservableList<Section> getSections(String courseName) throws ClassNotFoundException, SQLException{
        //getStatement();
        ObservableList sectionList =FXCollections.observableArrayList();
        query = "SELECT * FROM section s,instructor i WHERE coursename=? AND s.instructorid=i.id";
        writeQuery(query);
        prestatement =
                connection.prepareStatement("SELECT * FROM section s,instructor i WHERE coursename=? AND s.instructorid=i.id");
        prestatement.setString(1, courseName);
        rs = prestatement.executeQuery();
        while(rs.next()){
            Section section = new Section(rs.getInt("sectionnumber"),rs.getString("lab"),rs.getString("name")
                    ,rs.getString("days"),rs.getString("starttime"),rs.getString("endtime"));
            sectionList.add(section);
        }
        return sectionList;
    }
    
    public ObservableList<RegisteredCourses> registeredCourses() throws ClassNotFoundException, SQLException{
        //getStatement();
        ObservableList registeredCoursesList =FXCollections.observableArrayList();
        query = "SELECT * FROM regcourses WHERE studentid=?";
        writeQuery(query);
        prestatement = connection.prepareStatement("SELECT * FROM regcourses WHERE studentid=?");
        prestatement.setString(1, stdId);
        rs = prestatement.executeQuery();
        while(rs.next()){
            RegisteredCourses registeredCourses = new RegisteredCourses(
                    rs.getString("studentid"),rs.getString("coursename"),rs.getInt("sectionnumber"),
                    rs.getString("starttime"),rs.getString("endtime"), rs.getString("instructor"),
                    rs.getString("lab"), rs.getString("days"));
            registeredCoursesList.add(registeredCourses);
        }
        return registeredCoursesList;
    }
    public int deleteSection(String sectionName , int sectionNumber) throws ClassNotFoundException, SQLException{
        //getStatement();
        query = "DELETE FROM regcourses WHERE coursename=? AND sectionnumber=? AND studentid=?";
        writeQuery(query);
        prestatement =connection.prepareStatement("DELETE FROM regcourses WHERE coursename=? AND sectionnumber=? AND studentid=?");
        prestatement.setString(1, sectionName);
        prestatement.setInt(2, sectionNumber);
        prestatement.setString(3, stdId);
        int success = prestatement.executeUpdate();
        if (success!=0) {
            return 1 ;
        }
        return 0 ;
    }
    public Map updateSection(String sectionName , int sectionNumber,int preSectionNumber) throws ClassNotFoundException, SQLException{
        statusMap = new HashMap();
        statusMap.put("updated", false);
        statusMap.put("conflict",false);
        //getStatement();
        query = "SELECT days,starttime,endtime FROM section WHERE coursename=? AND sectionnumber=?";
        writeQuery(query);
        prestatement =connection.prepareStatement("SELECT days,starttime,endtime FROM section WHERE coursename=? AND sectionnumber=?");
        prestatement.setString(1, sectionName);
        prestatement.setInt(2, sectionNumber);
        rs = prestatement.executeQuery();
        String days = "";
        String starttime = "";
        String endtime = "";
        while(rs.next()){
            days = rs.getString("days");
            starttime = rs.getString("starttime");
            endtime = rs.getString("endtime");
        }
        query = "SELECT * FROM regcourses WHERE starttime=? AND endtime=? AND days=? AND studentid=?";
        writeQuery(query);
        prestatement =connection.prepareStatement("SELECT * FROM regcourses WHERE starttime=? AND endtime=? AND days=? AND studentid=?");
        prestatement.setString(1, starttime);
        prestatement.setString(2, endtime);
        prestatement.setString(3, days);
        prestatement.setString(4, stdId);
        rs = prestatement.executeQuery();
        if (rs.next()) {
            statusMap.put("conflict",true);
        }else{
            query = "UPDATE regcourses SET sectionnumber=?,days=?"
                    + " WHERE coursename=? AND sectionnumber=? AND studentid=?";
            writeQuery(query);
            prestatement = connection.prepareStatement("UPDATE regcourses SET sectionnumber=?,days=?"
                    + " WHERE coursename=? AND sectionnumber=? AND studentid=?");
            prestatement.setInt(1, sectionNumber);
            prestatement.setString(2, days);
            prestatement.setString(3, sectionName);
            prestatement.setInt(4, preSectionNumber);
            prestatement.setString(5, stdId);
            prestatement.executeUpdate();
            statusMap.put("updated", true);
        }
        return statusMap;
    }
    public Map addSection(String sectionName , int sectionNumber) throws ClassNotFoundException, SQLException{
        statusMap = new HashMap();
        statusMap.put("isAdded", false);
        statusMap.put("isExist",false);
        statusMap.put("conflictExist",false);
        
        //getStatement();
        query = "SELECT * FROM regcourses WHERE coursename=? AND studentid=?";
        writeQuery(query);
        prestatement = connection.prepareStatement("SELECT * FROM regcourses WHERE coursename=? AND studentid=?");
        prestatement.setString(1, sectionName);
        prestatement.setString(2, stdId);
        rs =prestatement.executeQuery();
        if (rs.next()) {
            statusMap.put("isExist",true);
        }else{
            query = "SELECT * FROM section s,instructor i "
                    + "WHERE coursename=? AND sectionnumber=? AND s.instructorid=i.id";
            writeQuery(query);
            prestatement = connection.prepareStatement("SELECT * FROM section s,instructor i "
                    + "WHERE coursename=? AND sectionnumber=? AND s.instructorid=i.id");
            prestatement.setString(1, sectionName);
            prestatement.setInt(2, sectionNumber);
            
            rs = prestatement.executeQuery();
            String instructor="";
            String starttime="";
            String endtime="";
            String days="";
            String lab="";
            while(rs.next()){
                instructor = rs.getString("name");
                starttime = rs.getString("starttime");
                endtime = rs.getString("endtime");
                days = rs.getString("days");
                lab = rs.getString("lab");
            }
            query = "SELECT * FROM regcourses "
                    + "WHERE starttime=? AND endtime=? AND days=? AND studentid=?";
            writeQuery(query);
            prestatement = connection.prepareStatement("SELECT * FROM regcourses "
                    + "WHERE starttime=? AND endtime=? AND days=? AND studentid=?");
            prestatement.setString(1, starttime);
            prestatement.setString(2, endtime);
            prestatement.setString(3, days);
            prestatement.setString(4, stdId);
            rs = prestatement.executeQuery();
            if (rs.next()) {
                statusMap.put("conflictExist",true);
            }else{
                int semesterNumber ;
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int month  ;
                if (Calendar.getInstance().get(Calendar.MONTH)<6) {
                    semesterNumber = 2 ;
                }else{
                    semesterNumber = 1 ;
                }
                query ="INSERT INTO regcourses "
                                + "VALUES(stdId,sectionName,starttime,endtime,sectionNumber,instructor,lab,days)";
                writeQuery(query);
                prestatement =
                        connection.prepareStatement("INSERT INTO regcourses "
                                + "VALUES(?,?,?,?,?,?,?,?,?,?)");
                prestatement.setString(1, stdId);
                prestatement.setString(2, sectionName);
                prestatement.setString(3, starttime);
                prestatement.setString(4, endtime);
                prestatement.setInt(5, sectionNumber);
                prestatement.setString(6, instructor);
                prestatement.setString(7, lab);
                prestatement.setString(8, days);
                prestatement.setInt(9, semesterNumber);
                prestatement.setInt(10, year);
                
                int success = prestatement.executeUpdate();
                if (success!=0) {
                    statusMap.put("isAdded", true);
                }
            }
        }
        return statusMap ;
    }
    
    public ObservableList<Course> grades(int semesterNumber) throws SQLException{
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
        
        String studentId = stdId;
        query = "SELECT * "
                        + "FROM precourses pc ,semester sm "
                        + "WHERE pc.studentid=? AND pc.semesterid=sm.id AND sm.year=? AND sm.number=?";
        writeQuery(query);
        prestatement = connection.prepareStatement(
                "SELECT * "
                        + "FROM precourses pc ,semester sm "
                        + "WHERE pc.studentid=? AND pc.semesterid=sm.id AND sm.year=? AND sm.number=?");
        prestatement.setString(1, stdId);
        prestatement.setString(2, year);
        prestatement.setInt(3, semesterNumber);
        
        rs = prestatement.executeQuery();
        
        while(rs.next()){
            Course grade = new Course(rs.getString("courseid"),rs.getDouble("grade"));
            grades.add(grade);
        }
        return grades ;
    }
    
    
    public int[] getArrayofSections(String courseName) throws SQLException{
        int length = 0 ;
        query = "SELECT sectionnumber FROM section WHERE coursename=?";
        writeQuery(query);
        prestatement =connection.prepareStatement("SELECT sectionnumber FROM section WHERE coursename=?");
        prestatement.setString(1, courseName);
        rs = prestatement.executeQuery();
        while(rs.next()){
            length++;
        }
        rs.beforeFirst();
        int count =0;
        int[] sections  = new int[length];
        while(rs.next()){
            sections[count]=rs.getInt("sectionnumber");
            count++;
        }
        return sections;
    }
    
    public static String md5Password(String regularPassword) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashInBytes = md.digest(regularPassword.getBytes(StandardCharsets.UTF_8));
        
        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

