package com.anzhi.SimpleMQTest;

import com.anzhi.simplemq.MqClient;

/**
 * @Package: com.anzhi.SimpleMQTest
 * @ClassName: ConsumerClient
 * @Author: AZ
 * @CreateTime: 2021/8/2 14:58
 * @Description:
 */
public class ConsumerClient {
    public static void main(String[] args) {
        MqClient.consume();
        while (true){}
    }
}
