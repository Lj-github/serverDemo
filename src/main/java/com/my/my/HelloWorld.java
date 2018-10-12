package com.my.my;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class HelloWorld  {

    private String message;

    public static void main(String[] args) {
        System.out.println("Hello World!aaaaaaaa");
        for (int i = 0; i < args.length; i++) {
            System.out.println(args);
        }
    }

//
//    public void init() throws ServletException
//    {
//        // 执行必需的初始化
//        message = "test hword";
//
//        System.out.printf("message%s", message);
//
//    }
//
//    public void doGet(HttpServletRequest request,
//                      HttpServletResponse response)
//            throws ServletException, IOException
//    {
//        // 设置响应内容类型
//        response.setContentType("text/html");
//
//        // 实际的逻辑是在这里
//        PrintWriter out = response.getWriter();
//        out.println("<h1>" + message + "</h1>");
//    }
//
//    public void destroy()
//    {
//        // 什么也不做
//    }


}
