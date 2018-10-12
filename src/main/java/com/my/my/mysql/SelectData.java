package com.my.my.mysql;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Map;
import java.util.HashMap;

import net.sf.json.JSONObject;

public class SelectData extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static String DB_URL = "jdbc:mysql://%s:%s/%s";
    // 数据库的用户名与密码，需要根据自己的设置
    static String USER = "";
    static String PASS = "";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SelectData() {
        super();
        // TODO Auto-generated constructor stub
        DB_URL = String.format(DB_URL, MysqlConfig.host, MysqlConfig.port, "test");
        USER = MysqlConfig.username;
        PASS = MysqlConfig.password;
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        Statement stmt = null;
        String id = request.getParameter("id");
        String sql;
        sql = "SELECT runoob_id, runoob_title," +
                "runoob_author, submission_date " +
                "FROM runoob_tbl " +
                String.format("WHERE runoob_id='%s'", id);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int ID = rs.getInt("runoob_id");
                String runoob_title = rs.getString("runoob_title");
                String runoob_author = rs.getString("runoob_author");
                String submission_date = rs.getString("submission_date");
                Map map = new HashMap();
                map.put("runoob_id", ID);
                map.put("runoob_title", runoob_title);
                map.put("runoob_author", runoob_author);
                map.put("submission_date", submission_date);
                JSONObject jsonObject = JSONObject.fromObject(map);
                String jsonStr = jsonObject.toString();
                out.print(jsonStr);
            }
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}

