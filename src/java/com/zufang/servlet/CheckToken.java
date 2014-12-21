/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zufang.servlet;

import com.zufang.DAO.DAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author moxun.ljf
 */
public class CheckToken {
    
    private static String status;
    private static int resultcode=0;
    private static String invaildtime = "unknow";
    
    public static boolean checkToken(String username,String token){
        String sql = "SELECT * FROM token WHERE purpose='" + username + "'";
        try {
            ResultSet rs = DAO.executeQuery(sql);
            if(!rs.next()){
                status = "token of this user is not exist.";
                resultcode = 4;
                return false;
            }else{
                String tokenString = rs.getString("token");
                Timestamp deadline = rs.getTimestamp("failuetime");
                if(!token.equals(tokenString)){
                    status = "token is not correct.";
                    resultcode = 2;
                    return false;
                }
                if(deadline.before(new Date())){
                    status = "token is invaild,it's out of survive time";
                    resultcode = 3;
                    return false;
                }
                status = "verify successed.";
                resultcode = 1;
                invaildtime = rs.getTimestamp("failuetime").toString();
                return true;
            }
        } catch (SQLException ex) {
            status = ex.toString() + " SQL:" + sql;
            resultcode = 5;
        } finally{
            DAO.closeDB();
        }
        return false;
    }
    
    public static String getVerifyStatus(){
        return status;
    }
    
    public static int getVerifyResultCode(){
        return resultcode;
    }
    
    public static String getInvaildTime(){
        return invaildtime;
    }
}
