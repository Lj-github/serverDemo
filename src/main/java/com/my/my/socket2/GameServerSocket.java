package com.my.my.socket2;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
// 不需要依赖 tomcat

public class GameServerSocket {
    private static final Logger logger = LoggerFactory.getLogger(GameServerSocket.class);

    public static final int port = 0;
    private ServerSocket server = null;

    //搭建服务器端
    public static void main(String[] args) throws IOException {
        GameServerSocket socketService = new GameServerSocket();
        socketService.start();
    }

    public void start() {

        if (isPortAvailable(port)) {
            try {
                isPortAvailable(port);
                this.server = null;
                try {
                    server = new ServerSocket(port);
                    logger.debug("服务器启动成功");
                } catch (Exception e) {
                    logger.debug("没有启动监听：" + e);
                }
                Socket socket = null;
                try {
                    socket = server.accept();
                    //使用accept()阻塞等待客户请求，有客户
                    //请求到来则产生一个Socket对象，并继续执行
                } catch (Exception e) {
                    logger.debug("Error." + e);
                    //出错，打印出错信息
                }
                //3、获取输入流，并读取客户端信息
                String line;
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //由Socket对象得到输入流，并构造相应的BufferedReader对象
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                //由Socket对象得到输出流，并构造PrintWriter对象
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                //由系统标准输入设备构造BufferedReader对象
                logger.debug("Client:" + in.readLine());
                //在标准输出上打印从客户端读入的字符串
                line = br.readLine();
                //从标准输入读入一字符串
                //4、获取输出流，响应客户端的请求
                while (!line.equals("end")) {
                    //如果该字符串为 "bye"，则停止循环
                    writer.println(line);
                    //向客户端输出该字符串
                    writer.flush();
                    //刷新输出流，使Client马上收到该字符串
                    logger.debug("Server:" + line);
                    //在系统标准输出上打印读入的字符串
                    logger.debug("Client:" + in.readLine());
                    //从Client读入一字符串，并打印到标准输出上
                    line = br.readLine();
                    //从系统标准输入读入一字符串
                } //继续循环

                //5、关闭资源
                writer.close(); //关闭Socket输出流
                in.close(); //关闭Socket输入流
                socket.close(); //关闭Socket
                server.close(); //关闭ServerSocket
            } catch (Exception e) {//出错，打印出错信息
                logger.debug("Error." + e);
            }


        } else {
            logger.error("端口被占用");
        }


    }


    /**
     * 判断是否可用端口
     *
     * @param port
     * @return
     */
    private boolean isPortAvailable(int port) {
        try {
            ServerSocket ss = new ServerSocket(port);
            ss.close();
            return true;
        } catch (Exception e) {
            logger.error("端口不可用", e);
            return false;
        }
    }


}



