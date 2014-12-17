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
    
    public static boolean checkUser(String username ,String password){
        try {
            String sql = "SELECT password FROM userinfo WHERE username='" + username +"'";
            ResultSet rs = DAO.executeQuery(sql);
            if(!rs.next()){
                authStatus = "user " + username + " is not exist.";
            }else{
                String passwd = rs.getString("password");
                if(!passwd.equals(password)){
                    authStatus = "password of " + username + " is not correct.";
                }else{
                    authStatus = "verify successed.";
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
    
    public static String getAuthStatus(){
        return authStatus;
    }
}
