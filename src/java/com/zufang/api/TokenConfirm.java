/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zufang.api;

import com.zufang.servlet.CheckToken;
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
 * @author Misaku
 */
public class TokenConfirm extends HttpServlet {

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
            out.println("<title>Servlet TokenConfirm</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TokenConfirm at " + request.getContextPath() + "</h1>");
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
        processRequest(request, response);
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
        response.setContentType("text/html;charset=UTF-8");
        JSONObject responseData = new JSONObject();
        try {
            JSONObject postData = new JSONObject();
            postData = PostStream2JSON.stream2JSONObject(request.getReader());
            String username = postData.getString("username");
            String token = postData.getString("token");
            if(CheckToken.checkToken(username, token)){
                responseData.put("status", CheckToken.getVerifyStatus());
                responseData.put("resultCode", CheckToken.getVerifyResultCode());
                responseData.put("invaildTime", CheckToken.getInvaildTime());
            }else{
                responseData.put("status", CheckToken.getVerifyStatus());
                responseData.put("resultCode", CheckToken.getVerifyResultCode());
            }
        } catch (JSONException ex) {
            try {
                responseData.put("exception", ex.toString());
            } catch (JSONException ex1) {
                Logger.getLogger(TokenConfirm.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        
        response.getWriter().println(responseData);
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
