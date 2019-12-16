/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import static Models.MySQLConnection.md5Password;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
public class MongoAdmin {

    private Map statusMap;

    private String stdId = "";
    boolean firstTime = true;

    MongoConnection mongoConnection = MongoConnection.getMongoConnection();
    MongoCollection<Document> collection;
    FindIterable<Document> documents;
    Document document;
    MongoCursor<Document> cursor;

    
    public MongoAdmin() {
    }

    public ObservableList<Student> getStudent() throws ClassNotFoundException {
        ObservableList studentList
                = FXCollections.observableArrayList();
        mongoConnection.setCollection("student");
        collection = mongoConnection.getCollection();
        documents = collection.find();
        cursor = documents.iterator();
        while (cursor.hasNext()) {
            document = cursor.next();
            Student student
                    = new Student(
                            document.getString("name"),
                            document.getString("id"),
                            document.getString("phone"),
                            document.getString("address")
                    );
            studentList.add(student);
        }
        
//        mongoConnection.setCollection("course");
//        collection = mongoConnection.getCollection();
//        FindIterable<Document> output = (FindIterable)collection.
//                aggregate(Arrays.asList(
//                        Aggregates.match(Filters.eq("name", "Programming 3")),
//                        Aggregates.lookup("section","name","coursename","matchedSections")                       
//                ));
//        cursor = output.iterator();
//        ArrayList<Object> list = new ArrayList<>();
//        while(cursor.hasNext())
//            document = cursor.next();
////        document.
////            list.add(new GsonBuilder().create()
////                    .fromJson(document.toJson(), Section.class));
//        for(int i =0; i<list.size();i++){
//            document = (Document)list.get(i);
//            document = (Document)document.get("matchedSections");
//            for(int j =0; j<list.size();j++){
//                System.out.println(document);
//            }   
//        }
        return studentList;

        
    }

    public Map addStudent(String id, String name, String password, String phone, String address) {
        boolean isAdded = false;
        statusMap = new HashMap();
        statusMap.put("isAdded", false);
        statusMap.put("isExist", false);
        statusMap.put("enterError", false);
        mongoConnection.setCollection("student");
        collection = mongoConnection.getCollection();
        try {
            if (!id.equalsIgnoreCase("") && !name.equalsIgnoreCase("") && !password.equalsIgnoreCase("")
                    && !phone.equalsIgnoreCase("") && !address.equalsIgnoreCase("")) {

                documents = collection.find(Filters.eq("id", id));
                cursor = documents.iterator();
                if (cursor.hasNext()) {
                    statusMap.put("isExist", true);
                } else {
                    collection.insertOne(new Document("id", id)
                            .append("name", name)
                            .append("phone", phone)
                            .append("address", address)
                            .append("password", md5Password(password))
                            .append("email", "email"));
                    statusMap.put("isAdded", true);
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

    public ArrayList getStudentIds() {
        ArrayList<String> studentsId = new ArrayList<>();
        int length = 0;

        mongoConnection.setCollection("student");
        collection = mongoConnection.getCollection();

        documents = collection.find();
        cursor = documents.iterator();

        while (cursor.hasNext()) {

            document = cursor.next();
            studentsId.add(document.getString("id"));
        }
        System.out.println("StudentMongo getStudentIds");
        return studentsId;

    }

    public boolean deleteStudent(String studentId) {
        boolean isDeleted = false;
        mongoConnection.setCollection("student");
        collection = mongoConnection.getCollection();
        DeleteResult deleteResult = collection.deleteOne(Filters.eq("id", studentId));

        if (deleteResult.getDeletedCount() != 0) {
            isDeleted = true;
        }
        return isDeleted;
    }

    //db student
    public Map updateStudent(String stdid, String text, String updateType) {
        statusMap = new HashMap();
        statusMap.put("isUpdated", false);
        statusMap.put("duplicatedOccurred", false);
        UpdateResult updateResult = null;
        mongoConnection.setCollection("student");
        collection = mongoConnection.getCollection();
        if (updateType.equalsIgnoreCase("name")) {
            updateResult = collection.updateOne(Filters.eq("id", stdid), new Document("$set", new Document("name", text)));
        } else if (updateType.equalsIgnoreCase("phone")) {
            updateResult = collection.updateOne(Filters.eq("id", stdid), new Document("$set", new Document("phone", text)));
        } else if (updateType.equalsIgnoreCase("address")) {
            updateResult = collection.updateOne(Filters.eq("id", stdid), new Document("$set", new Document("address", text)));
        }

        if (updateResult.getMatchedCount() != 0) {
            statusMap.put("isUpdated", true);
        }

        return statusMap;
    }

    public Student showStudentBasicInfo() {
        Student student = new Student();
        mongoConnection.setCollection("student");
        collection = mongoConnection.getCollection();
        if (firstTime) {
            firstTime = false;

            documents = collection.find(Filters.eq("id", mongoConnection.getLoggedStudent()));
            cursor = documents.iterator();

            if (cursor.hasNext()) {
                document = cursor.next();
                student.setStudentId(document.getString("id"));
                student.setStudentName(document.getString("name"));
                student.setStudentLastName(document.getString("lname"));
                student.setStudentEmail(document.getString("email"));
                student.setStudentAddress(document.getString("address"));
                student.setStudentPhone(document.getString("phone"));
            }
        }
        return student;

    }

}
