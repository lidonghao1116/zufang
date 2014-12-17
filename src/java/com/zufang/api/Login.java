/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zufang.api;

import com.zufang.DAO.DAO;
import com.zufang.servlet.Auth;
import com.zufang.utils.PostStream2JSON;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author moxun.ljf
 */
public class Login extends HttpServlet {

    String ip = "unknow";
    String from = "";
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Login</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Login at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JSONObject help = new JSONObject();
        try {
            help.put("help", "API用途：用户登入，并保存当前token\n请求方法：POST\n参数格式:{\"username\":\"some_user\",\"password\":\"some_passwd\",\"from\":\"web/mobile\"}");
        } catch (JSONException ex) {
            Logger.getLogger(Logout.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.getWriter().println(help);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JSONObject json = new JSONObject();

        try {
            JSONObject postData = new JSONObject();
            postData = PostStream2JSON.stream2JSONObject(request.getReader());
            String username = postData.getString("username");
            String password = postData.getString("password");
            from = postData.getString("from");
            if (Auth.checkUser(username, password)) {
                json.put("status", Auth.getAuthStatus());
                Token token = Token.getToken(username);
                JSONObject tokenJSON = new JSONObject();
                tokenJSON.put("username", token.getUsername());
                tokenJSON.put("token", token.getTokenString());
                tokenJSON.put("vaildTime", token.getVaildTime());
                json.put("tokenBody", tokenJSON);
                updateLoginLog(username);
            } else {
                json.put("status", Auth.getAuthStatus());
            }
        } catch (JSONException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        ip = getIP(request);
        response.getWriter().println(json);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void updateLoginLog(String username) {
        String sql = "INSERT INTO userlogin VALUES('" + username + "',DEFAULT,'"+ ip +"','"+ from +"')";
        DAO.executeRawSQL(sql);
    }

    private String getIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.length() > 15) {
            ip = ip.split(",")[0];
        }
        return ip;
    }
}
