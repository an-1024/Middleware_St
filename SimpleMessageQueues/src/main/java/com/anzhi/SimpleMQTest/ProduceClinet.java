package com.anzhi.SimpleMQTest;

import com.anzhi.simplemq.MqClient;

/**
 * @Package: com.anzhi.SimpleMQTest
 * @ClassName: ProduceClinet
 * @Author: AZ
 * @CreateTime: 2021/8/2 14:22
 * @Description:
 */
public class ProduceClinet {
    public static void main(String[] args) throws Exception{
        MqClient.produce("Hello MQ !");
    }
}
