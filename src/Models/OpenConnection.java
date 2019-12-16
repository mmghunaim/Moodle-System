/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mmghunaim
 */
public class OpenConnection implements ConnectionState {

    @Override
    public Connection getStateOfConnection(MySQLConnection dbConnection) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            dbConnection.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/registration",
                    "root", "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dbConnection.connection;
    }

    @Override
    public MongoDatabase getStateOfMongoConnection(MongoConnection mongoConnection) {
        mongoConnection.client = new MongoClient("localhost", 27017);
        mongoConnection.database = mongoConnection.client.getDatabase("registration");
        return mongoConnection.database;
        
    }

}
