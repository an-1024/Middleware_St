package com.anzhi.simplemq;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Package: com.anzhi.simplemq
 * @ClassName: BrokerServer
 * @Author: AZ
 * @CreateTime: 2021/8/2 10:53
 * @Description:
 */
public class BrokerServer implements Runnable{
    public static int SERVICE_PORT = 9999;

    private final Socket socket;

    public BrokerServer(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            while (true) {
                String str = in.readLine();
                if (str == null) {
                    continue;
                }
                System.out.println("接收到原始数据: " + str);

                if (str.equals("CONSUME")) {   //CONSUME表示要消费一条消息
                    //从消息队列中消费一条消息
                    String message = Broker.consume();
                    out.println(message);
                    out.flush();
                } else {
                    //其他情况都表示生产消息放到消息队列中
                    Broker.produce(str);
                }
            }
        }catch (Exception e){
            System.out.println("消息队列服务异常" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(SERVICE_PORT);
            while (true) {
                BrokerServer brokerServerv = new BrokerServer(server.accept());
                new Thread(brokerServerv).start();
            }
        }catch (Exception e){
            System.out.println("调用消息服务异常: " + e.getMessage());
        }
    }
}
