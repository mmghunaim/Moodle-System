/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import java.sql.Connection;


public interface ConnectionState {

    public Connection getStateOfConnection(MySQLConnection dbConnection);
    public MongoDatabase getStateOfMongoConnection(MongoConnection mongoConnection);
}
