package com.anzhi.rocketmq.springbootmq.rocketmq;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Package: com.anzhi.rocketmq.mq
 * @ClassName: SpringProducer
 * @Author: AZ
 * @CreateTime: 2021/8/24 9:39
 * @Description:
 */
@Component
public class SpringProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    // 消息发送方法
    public void sendMessage(String topic, String msg){
        this.rocketMQTemplate.convertAndSend(topic, msg);
    }

    // 事物消息发送
    public void sendMessageInTransaction(String topic, String msg) throws InterruptedException{
        String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD", "TagE"};
        for(int i=0; i<10; i++){
            Message<String> message = MessageBuilder.withPayload(msg).build();
            String destination = topic + ":" + tags[i % tags.length];
            SendResult sendResult = rocketMQTemplate.sendMessageInTransaction(destination, message, destination);
            System.out.printf("%s%n", sendResult);

            Thread.sleep(10);
        }
    }
}
