package com.anzhi.biotcpipconnect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

/**
 *  多线程的 Socket 服务
 */
public class WebSocketServicThreadDemo {
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
                new Thread(new ServerTask(accept)).start();
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
    
    private static class ServerTask implements Runnable{
        
        private Socket socket;
        public ServerTask (Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            ObjectInputStream inputStream = null;
            ObjectOutputStream outputStream = null;
            try {
                inputStream = new ObjectInputStream(socket.getInputStream());
                outputStream = new ObjectOutputStream(socket.getOutputStream());

                // 接收客户端的输出
                String message = inputStream.readUTF();
                System.out.println("accept client message:" + message);

                // 服务端输出信息
                outputStream.writeUTF("hello" + message);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if(!Objects.isNull(inputStream)) {
                        inputStream.close();
                    }
                    
                    if(!Objects.isNull(outputStream)) {
                        outputStream.close();
                    }
                }catch (Exception e){
                    // doNothing
                }
            }
        }
    }
}
