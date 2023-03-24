package com.anzhi.biotcpipconnect;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

/**
 * 单线程的 Socket 服务
 */
public class WebSocketServiceDemo {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket accept = null;
        try {
            serverSocket = new ServerSocket();
            // 绑定监听端口
            serverSocket.bind(new InetSocketAddress(20001));
            System.out.println("Server start with Port: 20001");
            int count = 0;
            while (true) {
                // 监听连接, 建立连接实例
                accept = serverSocket.accept();
                System.out.println("accept client socket.....total = " + (++count));

                // 实例化与客户端的输入输出流
                try (ObjectInputStream inputStream = new ObjectInputStream(accept.getInputStream());
                     ObjectOutputStream outputStream = new ObjectOutputStream(accept.getOutputStream())) {
                    
                    // 接收客户端的输出
                    String message = inputStream.readUTF();
                    System.out.println("accept client message:" + message);
                    
                    // 服务端输出信息
                    outputStream.writeUTF("hello" + message);
                    outputStream.flush();
                }

            }
        } catch (Exception e) {
            // doNothing
        }finally {
            try {
                if(!Objects.isNull(serverSocket)) {
                    serverSocket.close();
                }
                
                if (!Objects.isNull(accept)){
                    accept.close();
                }
            }catch (Exception e){
                // doNothing
            }
        }
    }
}
