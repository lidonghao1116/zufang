/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zufang.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Flags;
import org.json.JSONObject;

/**
 *
 * @author moxun.ljf
 */
public class DAO {

    private static final String DIRVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://10.0.16.16:4066/16154249m_mysql_4gqruiyn";
    private static final String USER = "i6iMJ1eY";
    private static final String PASSWD = "RfT6SBpC0CJn";
    private static Connection connection;
    private static Statement statement;

    private static String DBState = "";
    private static String exceptionString= "";
    
    private static void init() {
        DBState = "";
        try {
            Class.forName(DIRVER);
            connection = DriverManager.getConnection(URL, USER, PASSWD);
        } catch (ClassNotFoundException ex) {
            DBState += "driver load error:" + ex.toString();
        } catch (SQLException ex) {
            DBState += "connection error:" + ex.toString();
        }

        try {
            if (!connection.isClosed()) {
                DBState = "ok";
            }
        } catch (SQLException ex) {
            DBState += "connection error:" + ex.toString();
        }
    }

    private static boolean isConnectionClosed() {
        try {
            if (connection.isClosed()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private static void prepareDB() {
        if (connection == null || isConnectionClosed()) {
            init();
        }
    }

    public static String getDBState() {
        if (DBState.equals("")) {
            init();
            closeDBConnection();
        }

        return DBState;
    }

    public static Connection getDBConnection() {
        prepareDB();
        return connection;
    }

    public static boolean closeDBConnection() {
        try {
            connection.close();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public static boolean colseDBStatement() {
        try {
            statement.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean closeDB(){
        return closeDBConnection() & colseDBStatement();
    }

    public static Statement getDBStatement() {
        prepareDB();
        try {
            statement = connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return statement;
    }

    public static boolean executeUpdate(String sql) {
        prepareDB();
        try {
            getDBStatement().executeUpdate(sql);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            closeDB();
        }
    }

    public static ResultSet executeQuery(String sql) {
        prepareDB();
        try {
            return getDBStatement().executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static boolean executeRawSQL(String sql) {
        prepareDB();
        try {
            getDBStatement().execute(sql);
            closeDB();
            return true;
        } catch (SQLException ex) {
            exceptionString = ex.toString();
            return false;
        }   
    }
    
    public static String getExptionDescribe(){
        return exceptionString;
    }

}
