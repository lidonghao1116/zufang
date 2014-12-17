/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zufang.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author moxun.ljf
 */
public class PostStream2JSON {
    public static JSONObject stream2JSONObject(BufferedReader br){
        try {
            String inString   = br.readLine();
            JSONObject json = new JSONObject(inString);
            return json;
        } catch (IOException ex) {
            Logger.getLogger(PostStream2JSON.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(PostStream2JSON.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static JSONArray stream2JSONArray(BufferedReader br){
        try {
            String inString   = br.readLine();
            JSONArray json = new JSONArray(inString);
            return json;
        } catch (IOException ex) {
            Logger.getLogger(PostStream2JSON.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(PostStream2JSON.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
