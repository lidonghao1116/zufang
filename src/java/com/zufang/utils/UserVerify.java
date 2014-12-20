/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zufang.utils;

import com.zufang.DAO.DAO;
import java.util.Random;

/**
 *
 * @author Misaku
 */
public class UserVerify {
    private static String url="http://zufang.jd-app.com/Verify?token=";
    private static String seed = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890";
    
    private static String getVerifyCode(){
        String verifyString="";
        Random random = new Random();
        for(int i=0;i<30;i++){
            int index = Math.abs(random.nextInt() % 62);
            verifyString += seed.charAt(index);
        }
        url += verifyString;
        return verifyString;
    }
    
    private static String getSQL(String username,String token){
        String sqlString = "INSERT INTO verify VALUES";
        sqlString +="(DEFAULT,'" + username +
                "','" + token + "',DEFAULT)";
        return sqlString;
    }
    
    private static void record(String username,String token){
        DAO.executeRawSQL(getSQL(username, token));
        DAO.closeDB();
    }
    
    public static String doVerify(String username,String mail){
        record(username, getVerifyCode());
        url += "&username="+username;
        String result = Mail.sendVerifyMail(mail, username, url);
        return result;
    }
    
    public static String getVerifyURL(){
        return url;
    }
}
