/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zufang.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Misaku
 */
public class HelpFactory {
    public static JSONObject getHelp(String using,String methodS,JSONObject body){
        JSONObject help = new JSONObject();
        try {
            help.put("using", using);
            help.put("method", methodS);
            help.put("parameters", body);
        } catch (JSONException ex) {
            Logger.getLogger(HelpFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return help;
    }
}
