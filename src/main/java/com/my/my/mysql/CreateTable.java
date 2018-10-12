package com.my.my.mysql;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class CreateTable  extends HttpServlet {
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
    public CreateTable() {
        super();
        // TODO Auto-generated constructor stub
        DB_URL = String.format(DB_URL,MysqlConfig.host,MysqlConfig.port,MysqlConfig.db);
        USER = MysqlConfig.username;
        PASS = MysqlConfig.password;
    }
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        Statement stmt = null;
        String tableName = request.getParameter("tableName");
        if ( tableName == null){
            tableName = "test";
        }

        String sql;
        sql = "CREATE DATABASE " + tableName;
        // 设置响应内容类型
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try{
            // 注册 JDBC 驱动器
            Class.forName("com.mysql.jdbc.Driver");
            // 打开一个连接
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行 SQL 查询
            stmt = conn.createStatement();

            if(1 == stmt.executeUpdate(sql)) {
                System.out.println("成功创建表！");
                out.println('{' + "code"+ ':' + "success"+ '}');
            }
            else {
                //
                System.out.println("创建表失败！");
                out.println('{' + "code"+ ':' + "fail"+ '}');
            }
            stmt.close();
            conn.close();
        } catch(SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch(Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 最后是用于关闭资源的块
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

