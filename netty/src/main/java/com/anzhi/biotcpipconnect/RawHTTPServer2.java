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
import java.net.SocketException;
import java.util.function.Function;


/**
 * 代码重构
 */
public class RawHTTPServer2 {

    private ServerSocket serverSocket;
    // 处理请求函数
    Function<String, String> handler;

    public RawHTTPServer2() {}

    public RawHTTPServer2(Function<String, String> handler) {
        this.handler = handler;
    }

    // 监听
    public void listen(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            this.accept();
        }
    }

    // 处理连接逻辑
    public void accept() throws IOException {
        try {
            // Blocking-----
            Socket acceptSocket = serverSocket.accept();
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
            String resp = this.handler.apply(requestString);
            bufferedWriterSocket.write(resp);
            bufferedWriterSocket.flush();

            // 释放 socket
            acceptSocket.close();
        } catch (SocketException e) {
            System.out.println("Socket 异常，异常信息" + e);
        }
    }

    public static void main(String[] args) throws IOException {
        RawHTTPServer2 rawHTTPServer2 = new RawHTTPServer2(req -> {
            try {
                // 模拟实际生产中处理逻辑需要的时间
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // doNothing
            }
            return "HTTP/1.1 200 \n\n Hello World\n";
        });

        rawHTTPServer2.listen(8888);
    }
}