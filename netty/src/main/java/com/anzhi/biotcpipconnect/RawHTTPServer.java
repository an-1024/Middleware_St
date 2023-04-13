package com.anzhi.biotcpipconnect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class RawHTTPServer {
    public static void main(String[] args) throws IOException {
        // 指定服务端口
        ServerSocket socketServer = new ServerSocket(8888);
        
        // 死循环，监听 Socket 连接
        while (true) {
            // Blocking-----
            Socket acceptSocket = socketServer.accept();
            System.out.println("A socket created");
            // 获取 Socket 文件的输入
            InputStream inputStreamSocket = new DataInputStream(acceptSocket.getInputStream());
            BufferedReader bufferedReaderSocket = new BufferedReader(new InputStreamReader(inputStreamSocket));

            // 请求字符定义
            StringBuilder stringBuilderReq = new StringBuilder();
            
            String lienData = "";
            // readLine 以 '\n' 结束，否则会一直阻塞，因为是 HTTP 协议，所以这里不会阻塞
            while (!(lienData = bufferedReaderSocket.readLine()).isEmpty()) {
                stringBuilderReq.append(lienData + '\n');
            }

            String requestString = stringBuilderReq.toString();
            System.out.println(requestString);

            // 响应
            BufferedWriter bufferedWriterSocket = new BufferedWriter(new OutputStreamWriter(acceptSocket.getOutputStream()));
            // 第一个 '\n' 换行，第二个是 HTTP 协议要求：header 与 body 要有 '\n'
            bufferedWriterSocket.write("HTTP/1.1 200 \n\nHello World\n");
            bufferedWriterSocket.flush();
            
            // 释放 socket
            acceptSocket.close();
        }
    }
}
