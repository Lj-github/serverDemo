package com.my.my.mysql;
/**
 * Created by DELL on 2017/9/14.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class DatabaseAccess extends  HttpServlet {
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
    public DatabaseAccess() {
        super();
        // TODO Auto-generated constructor stub
        DB_URL = String.format(DB_URL,MysqlConfig.host,MysqlConfig.port,MysqlConfig.db);
        USER = MysqlConfig.username;
        PASS = MysqlConfig.password;
    }


    public String readReqContent(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        //接收请求XML信息
        req.setCharacterEncoding("UTF-8");
        // 获取输入输出流
        BufferedReader reade = new BufferedReader(new InputStreamReader(req.getInputStream(),"UTF-8"));
        String msg = null;
        StringBuilder message= new StringBuilder();
        while ((msg = reade.readLine()) != null) {
            message.append(msg);
        }
        return message.toString();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        /**
        * 1.获得客户机信息
        */
        String requestUrl = request.getRequestURL().toString();//得到请求的URL地址
        String requestUri = request.getRequestURI();//得到请求的资源
        String queryString = request.getQueryString();//得到请求的URL地址中附带的参数
        String remoteAddr = request.getRemoteAddr();//得到来访者的IP地址
        String remoteHost = request.getRemoteHost();
        int remotePort = request.getRemotePort();
        String remoteUser = request.getRemoteUser();
        String method = request.getMethod();//得到请求URL地址时使用的方法
        String pathInfo = request.getPathInfo();
        String localAddr = request.getLocalAddr();//获取WEB服务器的IP地址
        String localName = request.getLocalName();//获取WEB服务器的主机名
        response.setCharacterEncoding("UTF-8");//设置将字符以"UTF-8"编码输出到客户端浏览器
        //通过设置响应头控制浏览器以UTF-8的编码显示数据，如果不加这句话，那么浏览器显示的将是乱码
        response.setHeader("content-type", "text/html;charset=UTF-8");
        String dd =  request.getParameter("test");  //根据参数名获取参数值（注意，只能获取一个值的参数）
        //request.getParameterValue("参数 名"); //根据参数名获取参数值（可以获取多个值的参数）
        request.getParameterNames();   //获取所有参数名称列表

        Connection conn = null;
        Statement stmt = null;
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
            String sql;
            sql = "SELECT ID, Russion, Chinese FROM CCBReplaceEnglish";
            ResultSet rs = stmt.executeQuery(sql);
            // 这个 out  对应的就是  http 返回值的 responseText  里面的字符串
            // 展开结果集数据库
            while(rs.next()){
                // 通过字段检索
                int id  = rs.getInt("ID");
                String name = rs.getString("Chinese");
                String url = rs.getString("Russion");
                // 输出数据
                out.println("ID: " + id);
                out.println(", 站点名称: " + name);
                out.println(", 站点 URL: " + url);
            }
            // 完成后关闭
            rs.close();
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
