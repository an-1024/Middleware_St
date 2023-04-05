package rocketmqapidelayexample;

import common.Constant;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

/**
 * @Package: rocketmqapidelayexample
 * @ClassName: ScheduledMessageProducer
 * @Author: AZ
 * @CreateTime: 2021/8/14 20:54
 * @Description: 延迟消息
 */
public class ScheduledMessageProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("ExampleProducerGroup");
        defaultMQProducer.setNamesrvAddr(Constant.NAMESERVER);

        defaultMQProducer.start();
        int totalMessageTosend = 100;

        for(int i=0; i<totalMessageTosend; i++){
            Message message = new Message("TestTopic2", ("Hello scheduled message" + i).getBytes());

            // 设置延时级别
            message.setDelayTimeLevel(3);

            defaultMQProducer.send(message);
        }
        defaultMQProducer.shutdown();
    }
}
