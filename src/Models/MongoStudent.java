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
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.Document;

/**
 *
 * @author mmghunaim
 */
public class MongoStudent {

    MySQLConnection mysqlConnection = MySQLConnection.getDbConnection();
    MongoConnection mongoConnection = MongoConnection.getMongoConnection();
    MongoCollection<Document> collection;
    FindIterable<Document> documents;
    Document document;
    MongoCursor<Document> cursor;

    private Map statusMap;

    public ObservableList<Course> getCurrentCourses() {
        ObservableList courseList
                = FXCollections.observableArrayList();

        Calendar cal = Calendar.getInstance();
        int semesterNumber;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month;
        if (Calendar.getInstance().get(Calendar.MONTH) + 1 < 6) {
            semesterNumber = 2;
        } else {
            semesterNumber = 1;
        }
        mongoConnection.setCollection("semester");
        collection = mongoConnection.getCollection();
        String smesterid = null;
        documents = collection.find(Filters.and(
                Filters.eq("number", semesterNumber), Filters.eq("year", String.valueOf(year))
        ));
        for (Document document : documents) {
            smesterid = document.getString("id");
        }
        mongoConnection.setCollection("section");
        collection = mongoConnection.getCollection();
        documents = collection.find(Filters.and(Filters.eq("semesterid", smesterid), Filters.eq("sectionnumber", 101)));
        String coursename = null;
        List<String> coursesName = new ArrayList<>();
        cursor = documents.iterator();

        while (cursor.hasNext()) {
            document = cursor.next();
            coursesName.add(document.getString("coursename"));
        }

        mongoConnection.setCollection("course");
        collection = mongoConnection.getCollection();
        documents = collection.find();
        cursor = documents.iterator();

        while (cursor.hasNext()) {
            document = cursor.next();
            for (String courseName : coursesName) {
                if (document.getString("name").equalsIgnoreCase(courseName)) {
                    Course course
                            = new Course(
                                    document.getString("name"),
                                    document.getString("id")
                            );
                    courseList.add(course);
                }
            }
        }
        return courseList;
    }
//

    public ObservableList<Course> grades(int semesterNumber) {
        ObservableList grades = FXCollections.observableArrayList();
        String year;
        if (semesterNumber == 1) {
            year = "2017";
        } else if (semesterNumber == 2) {
            year = "2018";
        } else if (semesterNumber == 3) {
            year = "2018";
        } else {
            year = "2019";
        }

        String studentId = mysqlConnection.getLoggedStudent();

        mongoConnection.setCollection("semester");
        collection = mongoConnection.getCollection();
        String smesterid = null;
        documents = collection.find(Filters.and(
                Filters.eq("number", semesterNumber), Filters.eq("year", String.valueOf(year))
        ));
        for (Document document : documents) {
            smesterid = document.getString("id");
        }

        mongoConnection.setCollection("precourses");
        collection = mongoConnection.getCollection();
        documents = collection.find(Filters.and(
                Filters.eq("studentid", studentId), Filters.eq("semesterid", smesterid)
        ));
        cursor = documents.iterator();
        while (cursor.hasNext()) {
            document = cursor.next();
            Course grade
                    = new Course(
                            document.getString("courseid"),
                            document.getDouble("grade")
                    );
            grades.add(grade);

        }
        return grades;
    }
//    

    public ObservableList<RegisteredCourses> getRegisteredCourses(String studentId) {
        ObservableList registeredCoursesList
                = FXCollections.observableArrayList();

        mongoConnection.setCollection("regcourses");
        collection = mongoConnection.getCollection();
        if (studentId.equalsIgnoreCase("")) {
            documents = collection.find(Filters.eq("stdId", mysqlConnection.getLoggedStudent()));
        } else {
            documents = collection.find(Filters.eq("stdId", studentId));
        }
        cursor = documents.iterator();

        while (cursor.hasNext()) {
            document = cursor.next();
            if (studentId == null) {
                RegisteredCourses registeredCourses
                        = new RegisteredCourses(
                                document.getString("studentid"),
                                document.getString("coursename"),
                                document.getInteger("sectionnumber"),
                                document.getString("starttime"),
                                document.getString("endtime"),
                                document.getString("instructor"),
                                document.getString("lab"),
                                document.getString("days")
                        );
                registeredCoursesList.add(registeredCourses);
            } else {
                RegisteredCourses registeredCourses
                        = new RegisteredCourses(
                                document.getString("sectionName"),
                                document.getInteger("sectionNumber"));
                registeredCoursesList.add(registeredCourses);
            }
        }
        return registeredCoursesList;
    }
//    

    public ObservableList<Section> getSections(String courseName) {
        //getStatement();
        ObservableList sectionList = FXCollections.observableArrayList();

        mongoConnection.setCollection("instructor ");
        collection = mongoConnection.getCollection();

        Map<String, String> instructors = new HashMap<>();
        documents = collection.find();
        for (Document document : documents) {
            instructors.put(document.getString("id"), document.getString("name"));
        }

        mongoConnection.setCollection("section");
        collection = mongoConnection.getCollection();
        documents = collection.find(Filters.eq("coursename", courseName));
        cursor = documents.iterator();

        String instructorName = null;

        while (cursor.hasNext()) {
            this.document = cursor.next();
            if (instructors.containsKey(document.getString("instructorid"))) {
                instructorName = document.getString("name");
            }
            Section section
                    = new Section(
                            this.document.getInteger("sectionnumber"),
                            this.document.getString("lab"),
                            instructorName,
                            this.document.getString("days"),
                            this.document.getString("starttime"),
                            this.document.getString("endtime")
                    );
            sectionList.add(section);

        }
        return sectionList;
    }
//

    public int deleteSection(String sectionName, int sectionNumber) {

        mongoConnection.setCollection("regcourses");
        collection = mongoConnection.getCollection();
        DeleteResult dr = collection.deleteOne(Filters.and(
                Filters.eq("coursename", sectionName),
                Filters.eq("sectionnumber", sectionNumber),
                Filters.eq("studentid", mysqlConnection.getLoggedStudent())
        ));
        if (dr.getDeletedCount() != 0) {
            return 1;
        }
        return 0;

    }
//

    public Map updateSection(String sectionName, int sectionNumber, int preSectionNumber) {
        statusMap = new HashMap();
        statusMap.put("updated", false);
        statusMap.put("conflict", false);
        //getStatement();

        mongoConnection.setCollection("section");
        collection = mongoConnection.getCollection();
        documents = collection.find(Filters.and(
                Filters.eq("coursename", sectionName),
                Filters.eq("sectionnumber", sectionNumber)));
        cursor = documents.iterator();

        String days = "";
        String starttime = "";
        String endtime = "";

        while (cursor.hasNext()) {
            document = cursor.next();
            days = document.getString("days");
            starttime = document.getString("starttime");
            endtime = document.getString("endtime");
        }

        mongoConnection.setCollection("regcourses");
        collection = mongoConnection.getCollection();
        documents = collection.find(Filters.and(
                Filters.eq("starttime", starttime),
                Filters.eq("endtime", endtime),
                Filters.eq("days", days),
                Filters.eq("studentid", mysqlConnection.getLoggedStudent())));
        cursor = documents.iterator();

        if (cursor.hasNext()) {
            statusMap.put("conflict", true);
        } else {
            mongoConnection.setCollection("regcourses");
            collection = mongoConnection.getCollection();
            UpdateResult isUpdated = collection.updateOne(
                    Filters.and(
                            Filters.eq("coursename", sectionName),
                            Filters.eq("sectionnumber", preSectionNumber),
                            Filters.eq("studentid", mysqlConnection.getLoggedStudent())),
                    new Document("$set", new Document("sectionnumber", sectionNumber)
                            .append("days", days)));

            statusMap.put("updated", true);
        }

        return statusMap;
    }
//
//    //db courses

    public Map addSection(String sectionName, int sectionNumber){
        statusMap = new HashMap();
        statusMap.put("isAdded", false);
        statusMap.put("isExist", false);
        statusMap.put("conflictExist", false);

        mongoConnection.setCollection("regcourses");
        collection = mongoConnection.getCollection();
        documents = collection.find(Filters.and(
                Filters.eq("coursename", sectionName),
                Filters.eq("studentid", mysqlConnection.getLoggedStudent())));
        cursor = documents.iterator();

        if (cursor.hasNext()) {
            statusMap.put("isExist", true);
        } else {
            String instructor = "";
            String starttime = "";
            String endtime = "";
            String days = "";
            String lab = "";

            mongoConnection.setCollection("instructor");
            collection = mongoConnection.getCollection();

            Map<String, String> instructors = new HashMap<>();
            documents = collection.find();
            for (Document document : documents) {
                instructors.put(document.getString("id"), document.getString("name"));
            }

            mongoConnection.setCollection("section");
            collection = mongoConnection.getCollection();
            documents = collection.find(Filters.and(
                    Filters.eq("coursename", sectionName),
                    Filters.eq("sectionnumber", sectionNumber)));
            cursor = documents.iterator();
            String instructorName = null;
            while (cursor.hasNext()) {
                this.document = cursor.next();
                if (instructors.containsKey(document.getString("instructorid"))) {
                    instructorName = document.getString("name");
                }
                instructor = instructorName;
                starttime = document.getString("starttime");
                endtime = document.getString("endtime");
                days = document.getString("days");
                lab = document.getString("lab");
            }

            mongoConnection.setCollection("regcourses");
            collection = mongoConnection.getCollection();
            documents = collection.find(Filters.and(
                    Filters.eq("starttime", starttime),
                    Filters.eq("endtime", endtime),
                    Filters.eq("days", days),
                    Filters.eq("studentid", mysqlConnection.getLoggedStudent())));
            cursor = documents.iterator();
            if (cursor.hasNext()) {
                statusMap.put("conflictExist", true);
            } else {
                int semesterNumber;
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int month;
                if (Calendar.getInstance().get(Calendar.MONTH) < 6) {
                    semesterNumber = 2;
                } else {
                    semesterNumber = 1;
                }

                mongoConnection.setCollection("regcourses");
                collection = mongoConnection.getCollection();
                System.out.println(sectionName+starttime+endtime);
                
                collection.insertOne(new Document(
                    "stdId", mysqlConnection.getLoggedStudent())
                    .append("sectionName", sectionName)
                                .append("starttime", starttime)
                                .append("endtime", endtime)
                                .append("sectionNumber", sectionNumber)
                                .append("instructor", instructor)
                                .append("lab", lab)
                                .append("days", days)
            );
                statusMap.put("isAdded", true);
            }
        }

        return statusMap;
    }
//
    public ArrayList getArrayofSections(String courseName) {
        ArrayList<Integer> sections = new ArrayList<>();
        
        mongoConnection.setCollection("section");
        collection = mongoConnection.getCollection();
        documents = collection.find(Filters.eq("coursename", courseName));
        cursor = documents.iterator();

        while (cursor.hasNext()) {
            document = cursor.next();
            sections.add(document.getInteger("sectionnumber"));
        }
        return sections;
    }
}
