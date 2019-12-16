/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.bson.Document;

/**
 *
 * @author mmghunaim
 */
public class MongoConnection {

    private static MongoConnection mongoConnection;
    
    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> collection = null ;
    

    private Map statusMap;
    private String stdId = "";

    private ConnectionState connectionState;

    private MongoConnection() {
    }
    
    public ConnectionState getConnectionState() {
        return this.connectionState;
    }

    public void setConnection(MongoDatabase database) {
        MongoConnection.database = database;
    }
    
    public void setCollection(String name){
        collection = database.getCollection(name);
    }
    public MongoCollection<Document> getCollection(){
        return collection;
    }
    
    public static MongoConnection getMongoConnection() {
        if (mongoConnection == null) {
            mongoConnection = new MongoConnection();
        }
        return mongoConnection;
    }





    public Map verifyLogin(String id, String password) throws ClassNotFoundException {
        FindIterable<Document> documents = null;
        Document document = null;
        MongoCursor<Document> cursor = null;
        statusMap = new HashMap();
        statusMap.put("isNotEmpty", false);
        statusMap.put("loginType", "");
        statusMap.put("failedLogin", false);
        try {
            if (id != null && !id.isEmpty() && password != null && !password.isEmpty()) {
                statusMap.put("isNotEmpty", true);
                if (id.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin")) {
                    statusMap.put("loginType", "admin");
                } else {
                    documents = collection.find(
                            Filters.and(
                                    Filters.eq("id", id),
                                    Filters.eq("password", md5Password(password)))
                    );
                    cursor = documents.iterator();
                    if (cursor.hasNext()) {
                        stdId = id;
                        statusMap.put("loginType", "student");
                        statusMap.put("failedLogin", false);
                        statusMap.put("studentid", id);
                    } else {
                        statusMap.put("failedLogin", true);
                    }
                }
            } else {
                statusMap.put("isNotEmpty", false);
            }
        } catch (Exception ex) {
            ex.addSuppressed(ex);

        }
        return statusMap;
    }

    public static String md5Password(String regularPassword) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashInBytes = md.digest(regularPassword.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public String getLoggedStudent() {
        return this.stdId;
    }
}
