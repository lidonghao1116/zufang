/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zufang.servlet;

import com.zufang.DAO.DAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author moxun.ljf
 */
public class Auth {

    private static String authStatus;
    private static int authResultCode = 0;

    public static boolean checkUser(String username, String password) {
        try {
            String sql = "SELECT * FROM userinfo WHERE username='" + username + "'";
            ResultSet rs = DAO.executeQuery(sql);
            
            if (!rs.next()) {
                authStatus = "user " + username + " is not exist.";
                authResultCode = 4;
            } else {
                String passwd = rs.getString("password");
                boolean verifyed = rs.getBoolean("verifyed");
                if (!passwd.equals(password)) {
                    authStatus = "password of " + username + " is not correct.";
                    authResultCode = 2;
                } else {
                    if (!verifyed) {
                        authStatus = "user is not verifyed";
                        authResultCode = 3;
                        return false;
                    }
                    authStatus = "verify successed.";
                    authResultCode = 1;
                    DAO.closeDB();
                    rs.close();
                    return true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }
        DAO.closeDB();
        return false;
    }

    public static String getAuthStatus() {
        return authStatus;
    }
    
    public static int getAuthResultCode() {
        return authResultCode;
    }
}
