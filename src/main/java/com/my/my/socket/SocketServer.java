package com.my.my.socket;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.my.my.socket.protobuf.Awesome;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint("/socket") // 类似 包名
public class SocketServer {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    Logger logger = Logger.getLogger(SocketServer.class);

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<SocketServer> webSocketSet = new CopyOnWriteArraySet<SocketServer>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 后四个 是消息ID
     *
     * @param message
     * @return
     */


    public int getMsgID(byte[] message) {
        int a = message[4] * (int) Math.pow(256, 3);
        int b = message[5] * (int) Math.pow(256, 2);
        int c = message[6] * (int) Math.pow(256, 1);
        int d = message[7] * (int) Math.pow(256, 0);
        return a + b + c + d;
    }

    /**
     * 前四个 是消息长度
     *
     * @param message
     * @return
     */

    public int getMsgLen(byte[] message) {
        int a = message[0] * (int) Math.pow(256, 3);
        int b = message[1] * (int) Math.pow(256, 2);
        int c = message[2] * (int) Math.pow(256, 1);
        int d = message[3] * (int) Math.pow(256, 0);
        return a + b + c + d;
    }

    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;
    }

    /**
     * 先长度  后id
     *
     * @param msgLen
     * @param msgID
     * @return
     */

    public byte[] makeMsgHead(int msgLen, int msgID) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (int) (msgLen / Math.pow(256, 3));
        bytes[1] = (byte) (int) (msgLen / Math.pow(256, 2));
        bytes[2] = (byte) (int) (msgLen / Math.pow(256, 1));
        bytes[3] = (byte) (int) (msgLen / Math.pow(256, 0));

        bytes[4] = (byte) (int) (msgID / Math.pow(256, 3));
        bytes[5] = (byte) (int) (msgID / Math.pow(256, 2));
        bytes[6] = (byte) (int) (msgID / Math.pow(256, 1));
        bytes[7] = (byte) (int) (msgID / Math.pow(256, 0));
        return bytes;
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(byte[] message, Session session) {
        // 现在值处理一条  后面添加监听等的补充
        int msgID = getMsgID(message);
        int msgLen = getMsgLen(message);
        byte[] bytes2 = Arrays.copyOfRange(message, 8, 8 + msgLen);

        logger.debug("This is debug message.");
        try {
            Awesome.AwesomeMessage gd = Awesome.AwesomeMessage.parseFrom(bytes2);
            System.out.println("client msg:" + gd);  //正确解码 通过


        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        Awesome.AwesomeMessage22.Builder msg = Awesome.AwesomeMessage22.newBuilder();
        msg.setId(1);
        msg.setTest("ddd");
        Awesome.AwesomeMessage22 dsa = msg.build();

        byte[] bytes = dsa.toByteArray();
        byte[] bytesHead = makeMsgHead(bytes.length, 10001);

        byte[] sendMsg = addBytes(bytesHead, bytes);

        System.out.println("来自客户端的消息:" + sendMsg);
        //群发消息
        for (SocketServer item : webSocketSet) {
            try {
                item.sendMessage(sendMsg);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws IOException 发送消息
     */
    public void sendMessage(byte[] message) throws IOException {
        this.session.getAsyncRemote().sendBinary(ByteBuffer.wrap(message));
//        try {
//            //this.session.getBasicRemote().sendBinary(ByteBuffer.wrap(message)); //
//            //this.session.getAsyncRemote().sendBinary(ByteBuffer.wrap(message));
//            //this.session.getBasicRemote().sendText("rerererererer");
//        }catch (IOException e){
//            e.printStackTrace();
//        }
        //this.session.getAsyncRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        SocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        SocketServer.onlineCount--;
    }


}