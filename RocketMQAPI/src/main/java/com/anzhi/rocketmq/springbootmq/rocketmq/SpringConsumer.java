package com.anzhi.rocketmq.springbootmq.rocketmq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @Package: com.anzhi.rocketmq.mq
 * @ClassName: SpringConsumer
 * @Author: AZ
 * @CreateTime: 2021/8/26 14:10
 * @Description:
 */
@Component
@RocketMQMessageListener(consumerGroup = "MyConsumerGroup", topic = "TestTopic")
public class SpringConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println("Recived message: " + message);
    }
}
