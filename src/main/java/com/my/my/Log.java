package com.my.my;

/**
 * Created by DELL on 2017/9/15.
 */
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Log {
    private static  boolean flag = true;
    public static String gg(String str){
        if ( flag == true ){
            System.out.print("--------");
            System.out.print(str);
        }

        return "";
    }

}
