package com.anzhi.biotcpipconnect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

/**
 * 单线程的 Socket 服务
 */
public class WebSocketClientDemo {
    public static void main(String[] args) {
        // 创建 socket 对象
        Socket socket = null;
        // 创建输入输出流
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        // 服务器的通讯地址
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 20001);
        try {
            socket = new Socket();
            // 连接服务器
            socket.connect(inetSocketAddress);
            System.out.println("connect Server success");
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            System.out.println("ready send message");
            // 向服务器输出信息
            outputStream.writeUTF("Java");
            outputStream.flush();

            // 接收服务器的信息
            System.out.println("receive message from server: " + inputStream.readUTF());
        } catch (Exception e) {
            // doNothing
        } finally {
            try {
                if (!Objects.isNull(socket)) {
                    socket.close();
                }

                if (!Objects.isNull(outputStream)) {
                    outputStream.close();
                }
                
                if(!Objects.isNull(inputStream)) {
                    inputStream.close();
                }
            } catch (IOException e) {

            }
        }
    }
}
