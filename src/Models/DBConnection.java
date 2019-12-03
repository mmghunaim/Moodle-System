package Models;

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
    
    public PreparedStatement getStatement(String stat) throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/registration",
                "root", "");
        return this.connection.prepareStatement(stat);
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
    
    public String getLoggedStudent(){
        return this.stdId;
    }


    public Map verifyLogin(String id, String password) throws ClassNotFoundException{
        statusMap = new HashMap();
        statusMap.put("isNotEmpty", false);
        statusMap.put("loginType", "");
        statusMap.put("failedLogin", false);
        try {
            if (id!=null && !id.isEmpty() && password!=null && !password.isEmpty()) {
                statusMap.put("isNotEmpty", true);
                if (id.equalsIgnoreCase("admin")&&password.equalsIgnoreCase("admin")) {
                    statusMap.put("loginType", "admin");
                }else{
                    query = "SELECT * FROM student WHERE id=? AND password = ?";
                    writeQuery(query);
                    prestatement = getStatement("SELECT * FROM student WHERE id=? AND password = ?");
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

