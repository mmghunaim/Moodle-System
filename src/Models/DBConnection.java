package Models;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
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
import org.bson.Document;

public class DBConnection {

    private static DBConnection dbConnection;
    public static Connection connection;
    private ResultSet rs;
    private PreparedStatement prestatement;
    private ResultSet resultSet;
//    static final String IP = "localhost";
//    static final int PORT = 27017;
//    static final String DB_NAME = "registration";
//    static MongoCollection<Document> collection;

    private Map statusMap;
    private String stdId = "";
    private String query;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private ConnectionState connectionState;

    private DBConnection() {
    }

    public ConnectionState getConnectionState() {
        return this.connectionState;
    }

    public void setConnection(Connection connection) {
        DBConnection.connection = connection;
    }

    public void setConnectionState(ConnectionState connectionState) {
        this.connectionState = connectionState;
    }

    public static DBConnection getDbConnection() {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }


    public PreparedStatement getStatement(String stat) throws ClassNotFoundException, SQLException {
        return this.connection.prepareStatement(stat);
    }

    public void setResultSet(ResultSet rs) {
        this.resultSet = rs;
    }

    public ResultSet getResultSet() {
        return this.resultSet;
    }

    //    public static MongoCollection getCollection(String COLLECTION_NAME) {
//        MongoClient client = new MongoClient(IP, PORT);
//        MongoDatabase database = client.getDatabase(DB_NAME);
//        collection = database.getCollection(COLLECTION_NAME);
//        return collection;
//    }
    public void closeConnection() {
        try {
            this.prestatement.close();
            this.connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void writeQuery(String writer, String query) {

        Date date = new Date();
        File file = new File("./src/Models/logFile.txt");
        if (file.exists()) {
            try {
                FileWriter fileWriter = new FileWriter(file, true);
                PrintWriter printWriter = new PrintWriter(fileWriter);
                printWriter.write("Writer of The Query : " + writer + "\n\t"
                        + query + "\n\t\tDATE ::" + dateFormat.format(date)
                        + "\n------------------------------------------------------------------------------------------------------------------------\n");
                printWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String getLoggedStudent() {
        return this.stdId;
    }

    public Map verifyLogin(String id, String password) throws ClassNotFoundException {
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
//                    collection = getCollection("student");
//                    collection.find(new BasicDBObject("id", id));
//                    BasicDBObject mquery = new BasicDBObject("id", id);
//                    mquery.append("password", md5Password(password));
//                    FindIterable<Document> fe = collection.find(Filters.eq("id", id));
//                    for (Document doc : fe) {
//                        System.out.println("RESULT OF FE " + doc.get("name"));
//                    }

                    query = "SELECT * FROM student WHERE id=? AND password = ?";
                    writeQuery("System", query);
                    prestatement = getStatement("SELECT * FROM student WHERE id=? AND password = ?");
                    prestatement.setString(1, id);
                    prestatement.setString(2, md5Password(password));
                    rs = prestatement.executeQuery();
                    if (rs.next()) {
                        stdId = id;
                        statusMap.put("loginType", "student");
                        statusMap.put("failedLogin", false);
                        statusMap.put("studentid", id);
                    } else {
                        System.out.println(md5Password(password));
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
}
