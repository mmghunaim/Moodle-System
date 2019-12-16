/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import java.util.Arrays;
import java.util.Calendar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.Document;

/**
 *
 * @author mmghunaim
 */
public class MongoStudent {
    MongoConnection mongoConnection = MongoConnection.getMongoConnection();
    MongoCollection<Document> collection;
    FindIterable<Document> documents;
    Document document;
    MongoCursor<Document> cursor;
    
//    public ObservableList<Course> getCurrentCourses() {
//        
//        DBObject lookupFields = new BasicDBObject("from", "semester");
//        lookupFields.put("localField", "companyId");
//        lookupFields.put("foreignField", "_id");
//        lookupFields.put("as", "company");
//        DBObject lookup = new BasicDBObject("$lookup", lookupFields);
//
//        Calendar cal = Calendar.getInstance();
//        int semesterNumber;
//        int year = Calendar.getInstance().get(Calendar.YEAR);
//        int month;
//        if (Calendar.getInstance().get(Calendar.MONTH) + 1 < 6) {
//            semesterNumber = 2;
//        } else {
//            semesterNumber = 1;
//        }
//        ObservableList courseList
//                = FXCollections.observableArrayList();
//
//        AggregateIterable<Document> output = mConnection.getCollection().
//                aggregate(Arrays.asList(
//                        Aggregates.lookup("section","coursename","name","matchedSections")
//                ));
//        
//        prestatement = connection.
//                getStatement("SELECT c.name,c.id FROM section s ,semester sm , course c "
//                        + "WHERE s.coursename=c.name AND s.semesterid=sm.id "
//                        + "AND sm.number= ? AND sm.year=? "
//                        + "AND s.sectionnumber='101' ORDER BY c.name");
//        
//        prestatement.setInt(1, semesterNumber);
//        prestatement.setString(2, String.valueOf(year));
//
//        while (connection.getResultSet().next()) {
//            Course course
//                    = new Course(
//                            connection.getResultSet().getString("name"),
//                            connection.getResultSet().getString("id")
//                    );
//            courseList.add(course);
//        }
//
//        return courseList;
//    }
//
//    public ObservableList<Course> grades(int semesterNumber) throws SQLException, ClassNotFoundException {
//        ObservableList grades = FXCollections.observableArrayList();
//        String year;
//        if (semesterNumber == 1) {
//            year = "2017";
//        } else if (semesterNumber == 2) {
//            year = "2018";
//        } else if (semesterNumber == 3) {
//            year = "2018";
//        } else {
//            year = "2019";
//        }
//
//        String studentId = mConnection.getLoggedStudent();
//        
//        connection.writeQuery(connection.getLoggedStudent(), query);
//        prestatement = connection.getStatement(
//                "SELECT * "
//                + "FROM precourses pc ,semester sm "
//                + "WHERE pc.studentid=? AND pc.semesterid=sm.id AND sm.year=? AND sm.number=?");
//        prestatement.setString(1, connection.getLoggedStudent());
//        prestatement.setString(2, year);
//        prestatement.setInt(3, semesterNumber);
//
//        connection.setResultSet(prestatement.executeQuery());
//        while (connection.getResultSet().next()) {
//            Course grade
//                    = new Course(
//                            connection.getResultSet().getString("courseid"),
//                            connection.getResultSet().getDouble("grade")
//                    );
//            grades.add(grade);
//        }
//        return grades;
//    }
//    
//    public ObservableList<RegisteredCourses> getRegisteredCourses(String studentId) {
//        ObservableList registeredCoursesList
//                = FXCollections.observableArrayList();
//        
//        
//        if (studentId == null) {
//            documents = mConnection.getCollection().find(Filters.eq("id", mConnection.getLoggedStudent()));
//        } else {
//            documents = mConnection.getCollection().find(Filters.eq("id", studentId));
//        }
//        cursor = documents.iterator();
//
//        while (cursor.hasNext()) {
//            document = cursor.next();
//            if (studentId == null) {
//                RegisteredCourses registeredCourses
//                        = new RegisteredCourses(
//                                document.getString("studentid"),
//                                document.getString("coursename"),
//                                document.getInteger("sectionnumber"),
//                                document.getString("starttime"),
//                                document.getString("endtime"),
//                                document.getString("instructor"),
//                                document.getString("lab"),
//                                document.getString("days")
//                        );
//                registeredCoursesList.add(registeredCourses);
//            } else {
//                RegisteredCourses registeredCourses
//                        = new RegisteredCourses(
//                                document.getString("coursename"),
//                                document.getInteger("sectionnumber"));
//                registeredCoursesList.add(registeredCourses);
//            }
//        }
//        return registeredCoursesList;
//    }
//    
//    public ObservableList<Section> getSections(String courseName) throws ClassNotFoundException, SQLException {
//        //getStatement();
//        ObservableList sectionList = FXCollections.observableArrayList();
//        query = "SELECT * FROM section s,instructor i WHERE coursename=? AND s.instructorid=i.id";
//        writeQuery(connection.getLoggedStudent(), query);
//        prestatement
//                = connection.getStatement("SELECT * FROM section s,instructor i WHERE coursename=? AND s.instructorid=i.id");
//        prestatement.setString(1, courseName);
//        connection.setResultSet(prestatement.executeQuery());
//        while (connection.getResultSet().next()) {
//            Section section
//                    = new Section(
//                            connection.getResultSet().getInt("sectionnumber"),
//                            connection.getResultSet().getString("lab"),
//                            connection.getResultSet().getString("name"),
//                            connection.getResultSet().getString("days"),
//                            connection.getResultSet().getString("starttime"),
//                            connection.getResultSet().getString("endtime")
//                    );
//            sectionList.add(section);
//        }
//        return sectionList;
//    }
//
//    public int deleteSection(String sectionName, int sectionNumber) throws ClassNotFoundException, SQLException {
//        //getStatement();
//        query = "DELETE FROM regcourses WHERE coursename=? AND sectionnumber=? AND studentid=?";
//        writeQuery(connection.getLoggedStudent(), query);
//        prestatement = connection.getStatement("DELETE FROM regcourses WHERE coursename=? AND sectionnumber=? AND studentid=?");
//        prestatement.setString(1, sectionName);
//        prestatement.setInt(2, sectionNumber);
//        prestatement.setString(3, connection.getLoggedStudent());
//        int success = prestatement.executeUpdate();
//        if (success != 0) {
//            return 1;
//        }
//        return 0;
//    }
//
//    public Map updateSection(String sectionName, int sectionNumber, int preSectionNumber) throws ClassNotFoundException, SQLException {
//        statusMap = new HashMap();
//        statusMap.put("updated", false);
//        statusMap.put("conflict", false);
//        //getStatement();
//        query = "SELECT days,starttime,endtime FROM section WHERE coursename=? AND sectionnumber=?";
//        writeQuery(connection.getLoggedStudent(), query);
//        prestatement = connection.getStatement("SELECT days,starttime,endtime FROM section WHERE coursename=? AND sectionnumber=?");
//        prestatement.setString(1, sectionName);
//        prestatement.setInt(2, sectionNumber);
//        connection.setResultSet(prestatement.executeQuery());
//        String days = "";
//        String starttime = "";
//        String endtime = "";
//        while (connection.getResultSet().next()) {
//            days = connection.getResultSet().getString("days");
//            starttime = connection.getResultSet().getString("starttime");
//            endtime = connection.getResultSet().getString("endtime");
//        }
//        query = "SELECT * FROM regcourses WHERE starttime=? AND endtime=? AND days=? AND studentid=?";
//        writeQuery(connection.getLoggedStudent(), query);
//        prestatement = connection.getStatement("SELECT * FROM regcourses WHERE starttime=? AND endtime=? AND days=? AND studentid=?");
//        prestatement.setString(1, starttime);
//        prestatement.setString(2, endtime);
//        prestatement.setString(3, days);
//        prestatement.setString(4, connection.getLoggedStudent());
//        connection.setResultSet(prestatement.executeQuery());
//        if (connection.getResultSet().next()) {
//            statusMap.put("conflict", true);
//        } else {
//            query = "UPDATE regcourses SET sectionnumber=?,days=?"
//                    + " WHERE coursename=? AND sectionnumber=? AND studentid=?";
//            writeQuery(connection.getLoggedStudent(), query);
//            prestatement = connection.getStatement("UPDATE regcourses SET sectionnumber=?,days=?"
//                    + " WHERE coursename=? AND sectionnumber=? AND studentid=?");
//            prestatement.setInt(1, sectionNumber);
//            prestatement.setString(2, days);
//            prestatement.setString(3, sectionName);
//            prestatement.setInt(4, preSectionNumber);
//            prestatement.setString(5, connection.getLoggedStudent());
//            prestatement.executeUpdate();
//            statusMap.put("updated", true);
//        }
//        return statusMap;
//    }
//
//    //db courses
//    public Map addSection(String sectionName, int sectionNumber) throws ClassNotFoundException, SQLException {
//        statusMap = new HashMap();
//        statusMap.put("isAdded", false);
//        statusMap.put("isExist", false);
//        statusMap.put("conflictExist", false);
//
//        //getStatement();
//        query = "SELECT * FROM regcourses WHERE coursename=? AND studentid=?";
//        writeQuery(connection.getLoggedStudent(), query);
//        prestatement = connection.getStatement("SELECT * FROM regcourses WHERE coursename=? AND studentid=?");
//        prestatement.setString(1, sectionName);
//        prestatement.setString(2, connection.getLoggedStudent());
//        connection.setResultSet(prestatement.executeQuery());
//        if (connection.getResultSet().next()) {
//            statusMap.put("isExist", true);
//        } else {
//            query = "SELECT * FROM section s,instructor i "
//                    + "WHERE coursename=? AND sectionnumber=? AND s.instructorid=i.id";
//            writeQuery(connection.getLoggedStudent(), query);
//            prestatement = connection.getStatement("SELECT * FROM section s,instructor i "
//                    + "WHERE coursename=? AND sectionnumber=? AND s.instructorid=i.id");
//            prestatement.setString(1, sectionName);
//            prestatement.setInt(2, sectionNumber);
//
//            connection.setResultSet(prestatement.executeQuery());
//            String instructor = "";
//            String starttime = "";
//            String endtime = "";
//            String days = "";
//            String lab = "";
//            while (connection.getResultSet().next()) {
//                instructor = connection.getResultSet().getString("name");
//                starttime = connection.getResultSet().getString("starttime");
//                endtime = connection.getResultSet().getString("endtime");
//                days = connection.getResultSet().getString("days");
//                lab = connection.getResultSet().getString("lab");
//            }
//            query = "SELECT * FROM regcourses "
//                    + "WHERE starttime=? AND endtime=? AND days=? AND studentid=?";
//            writeQuery(connection.getLoggedStudent(), query);
//            prestatement = connection.getStatement("SELECT * FROM regcourses "
//                    + "WHERE starttime=? AND endtime=? AND days=? AND studentid=?");
//            prestatement.setString(1, starttime);
//            prestatement.setString(2, endtime);
//            prestatement.setString(3, days);
//            prestatement.setString(4, connection.getLoggedStudent());
//            connection.setResultSet(prestatement.executeQuery());
//            if (connection.getResultSet().next()) {
//                statusMap.put("conflictExist", true);
//            } else {
//                int semesterNumber;
//                int year = Calendar.getInstance().get(Calendar.YEAR);
//                int month;
//                if (Calendar.getInstance().get(Calendar.MONTH) < 6) {
//                    semesterNumber = 2;
//                } else {
//                    semesterNumber = 1;
//                }
//                query = "INSERT INTO regcourses "
//                        + "VALUES(stdId,sectionName,starttime,endtime,sectionNumber,instructor,lab,days)";
//                writeQuery(connection.getLoggedStudent(), query);
//                prestatement
//                        = connection.getStatement("INSERT INTO regcourses "
//                                + "VALUES(?,?,?,?,?,?,?,?,?,?)");
//                prestatement.setString(1, connection.getLoggedStudent());
//                prestatement.setString(2, sectionName);
//                prestatement.setString(3, starttime);
//                prestatement.setString(4, endtime);
//                prestatement.setInt(5, sectionNumber);
//                prestatement.setString(6, instructor);
//                prestatement.setString(7, lab);
//                prestatement.setString(8, days);
//                prestatement.setInt(9, semesterNumber);
//                prestatement.setInt(10, year);
//
//                int success = prestatement.executeUpdate();
//                if (success != 0) {
//                    statusMap.put("isAdded", true);
//                }
//            }
//        }
//        return statusMap;
//    }
//
//    public int[] getArrayofSections(String courseName) throws SQLException, ClassNotFoundException {
//        int length = 0;
//        query = "SELECT sectionnumber FROM section WHERE coursename=?";
//        writeQuery(connection.getLoggedStudent(), query);
//        prestatement = connection.getStatement("SELECT sectionnumber FROM section WHERE coursename=?");
//        prestatement.setString(1, courseName);
//        connection.setResultSet(prestatement.executeQuery());
//        while (connection.getResultSet().next()) {
//            length++;
//        }
//        connection.getResultSet().beforeFirst();
//        int count = 0;
//        int[] sections = new int[length];
//        while (connection.getResultSet().next()) {
//            sections[count] = connection.getResultSet().getInt("sectionnumber");
//            count++;
//        }
//        return sections;
//    }
}
