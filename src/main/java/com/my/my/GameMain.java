package com.my.my;

import com.my.my.socket.SocketServer;
import org.apache.log4j.Logger;

public class GameMain {
    Logger logger = Logger.getLogger(SocketServer.class);
    private static final GameMain instance = new GameMain();
    private static SocketServer socketServer ;

    public static GameMain getInstance() {
        return instance;
    }
    public void start(){
        try {
            //开始socket
            socketServer = new SocketServer();
            logger.debug("dsadsads");







        }catch (Exception e){
            logger.error(e);
        }

    }


    public static void main(String... args) {
        GameMain.getInstance().start();
    }
}
