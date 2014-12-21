/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zufang.api;

import com.zufang.DAO.DAO;
import com.zufang.utils.HelpFactory;
import com.zufang.utils.PostStream2JSON;
import com.zufang.utils.UserVerify;
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
 * @author Misaku
 */
public class Register extends HttpServlet {

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
            out.println("<title>Servlet Register</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Register at " + request.getContextPath() + "</h1>");
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
        response.setContentType("text/html;charset=UTF-8");
        JSONObject responseData = new JSONObject();
        try {
            responseData.put("username", "some_user");
            responseData.put("password", "password");
            responseData.put("tel", "13888888888");
            responseData.put("mail", "somemail@mail.com");
        } catch (JSONException ex) {
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.getWriter().println(HelpFactory.getHelp("用户注册，并发送注册验证链接到注册使用的邮箱", "POST", responseData));
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
        JSONObject responseData = new JSONObject();
        
        try {
            JSONObject postData = new JSONObject();
            postData = PostStream2JSON.stream2JSONObject(request.getReader());
            String username = postData.getString("username");
            String password = postData.getString("password");
            String tel  = postData.getString("tel");
            String mail = postData.getString("mail");
            
            String mark = "','";
            String sql = "INSERT INTO userinfo VALUES(DEFAULT,'" + username + mark + password + mark + tel + mark + mail +"',DEFAULT,FALSE,DEFAULT)";
            if(DAO.executeRawSQL(sql)){
                responseData.put("status", "ok");
                responseData.put("user", username);
                String m = UserVerify.doVerify(username, mail);
                responseData.put("verifyMailSended", m);
                responseData.put("verifyURL", UserVerify.getVerifyURL());
            }else{
                responseData.put("status", "fail,user's info was not record to database.");
                responseData.put("errorSQL", sql);
                responseData.put("user", username);
                responseData.put("exception", DAO.getExptionDescribe());
            }
            DAO.closeDB();        
            response.getWriter().println(responseData.toString());
            
        } catch (JSONException ex) {
            response.getWriter().println("{\"exception\":\""+ex.toString()+"\"}");
        }
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

}
