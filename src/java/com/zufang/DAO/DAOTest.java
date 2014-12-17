/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zufang.DAO;

import com.zufang.utils.PostStream2JSON;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class DAOTest extends HttpServlet {

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
            out.println("<title>Servlet DAOTest</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DAOTest at " + request.getContextPath() + "</h1>");
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
        //processRequest(request, response);
        JSONObject json = new JSONObject();
        try {
            JSONObject suggest = new JSONObject();
            suggest.put("row1", "datarow1");
            suggest.put("row2", "datarow2");
            json.put("error", "this is a test API for DAO and not support methord GET,try to POST json data like suggest.");
            json.put("suggest", suggest);
            json.put("help", "API用途：测试DAO帮助类的有效性\n请求方法：POST\n参数格式：参见suggest结点内容");
            response.getWriter().println(json);
        } catch (JSONException ex) {
            Logger.getLogger(DAOTest.class.getName()).log(Level.SEVERE, null, ex);
        }

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
        //processRequest(request, response);
        JSONObject postData = new JSONObject();
        JSONObject responseData = new JSONObject();

        postData = PostStream2JSON.stream2JSONObject(request.getReader());
        try {
            String r1 = postData.getString("row1");
            String r2 = postData.getString("row2");
            String sql = "INSERT INTO fortest VALUES (DEFAULT,'" + r1 + "','" + r2 + "', DEFAULT)";
            String sql2 = "SELECT * FROM fortest";
            DAO.executeRawSQL(sql);

            ResultSet rs = DAO.executeQuery(sql2);
            int linenum = 1;
            while (rs.next()) {
                JSONObject line = new JSONObject();
                line.put("ID", rs.getInt("ID"));
                line.put("row1", rs.getString("row1"));
                line.put("row2", rs.getString("row2"));
                line.put("date", rs.getTimestamp("date"));
                responseData.put("line" + linenum, line);
                linenum++;
            }
            rs.close();
            DAO.closeDB();

            response.getWriter().println(responseData);
        } catch (JSONException ex) {
            Logger.getLogger(DAOTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DAOTest.class.getName()).log(Level.SEVERE, null, ex);
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
