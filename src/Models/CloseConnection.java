/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hp
 */
public class CloseConnection implements ConnectionState {

    @Override
    public Connection getStateOfConnection(DBConnection dbConnection) {
        try {
            dbConnection.getStatement("").close();
            dbConnection.getResultSet().close();
            dbConnection.connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dbConnection.connection;
    }

}
