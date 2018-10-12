package com.my.my.mysql;

import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UpdateData  extends  HttpServlet{
    private static final long serialVersionUID = 1L;
    // JDBC 驱动名及数据库 URL
    //final  这是不可变的
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static  String DB_URL = "jdbc:mysql://%s:%s/%s";

    // 数据库的用户名与密码，需要根据自己的设置
    static  String USER = "";
    static  String PASS = "";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateData() {
        super();
        // TODO Auto-generated constructor stub
        DB_URL = String.format(DB_URL,MysqlConfig.host,MysqlConfig.port,"test");
        USER = MysqlConfig.username;
        PASS = MysqlConfig.password;
    }
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Connection conn = null;
        Statement stmt = null;
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String id = request.getParameter("id");
        String sql;
        sql = String.format("UPDATE runoob_tbl SET runoob_title='%s', runoob_author = '%s' WHERE runoob_id= %s",title,author,id) ;
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            if(1 == stmt.executeUpdate(sql)) {
                System.out.println("数据修改成功！");
                //println  应该是表示  输出line
                out.print('{' + "code"+ ':' + "success"+ '}');
            }
            else {
                System.out.println("数据修改失败！");
                //out.println();
                out.print('{' + "code"+ ':' + "fail"+ '}');
            }
            stmt.close();
            conn.close();
        } catch(SQLException se) {
            se.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }finally{
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
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

