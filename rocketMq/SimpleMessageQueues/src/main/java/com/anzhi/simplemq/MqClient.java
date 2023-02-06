package com.anzhi.simplemq;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @Package: com.anzhi.simplemq
 * @ClassName: MqClient
 * @Author: AZ
 * @CreateTime: 2021/8/2 14:07
 * @Description:
 */
public class MqClient {
    //生产消息
    public static void produce(String message){
        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), BrokerServer.SERVICE_PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println(message);
            out.flush();

            // 加入shutDown是因为客户端发完就关闭了，服务端仍然在读取数据导致报错(TCP的问题):  Connection reset
            socket.shutdownOutput();
        }catch (Exception e){
            System.out.println("MQ生产服务连接异常");
        }
    }

    // 消费消息
    public static void consume() {
        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), BrokerServer.SERVICE_PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            //先向消息队列发送字符串 “CONSUME” 表示消费
            out.println("CONSUME");
            out.flush();

            // 再从消息队列中获取一条消息
            String message = in.readLine();

            socket.shutdownOutput();
        } catch (Exception e) {
            System.out.println("MQ消费服务连接异常");
        }
    }
}
