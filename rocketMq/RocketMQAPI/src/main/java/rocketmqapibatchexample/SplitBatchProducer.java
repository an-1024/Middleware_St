package rocketmqapibatchexample;

import common.Constant;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import util.ListSplitter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package: rocketmqapibatchexample
 * @ClassName: SplitBatchProducer
 * @Author: AZ
 * @CreateTime: 2021/8/16 21:53
 * @Description: 批量发送消息
 */
public class SplitBatchProducer {
    public static void main(String[] args) throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("BatchProducerGroupName");
        //producer.setNamesrvAddr(Constant.NAMESERVER);
        producer.setNamesrvAddr("192.168.40.128:9876");
        producer.start();

        // large batch
        String topic = "BatchTest";

        Integer capacity = 100 * 1000;
        List<Message> messages = new ArrayList<>(capacity);
        for(int i=0; i<capacity; i++){
            messages.add(new Message(topic,"Tag", "OrderId_"+i, ("Hello MQ" + i).getBytes()));
        }

        //producer.send(messages);  //直接发送会导致MQClientException，CODE: 13  DESC: the message body size over max value, MAX: 4194304
        //split the large batch into small ones:
        ListSplitter splitter = new ListSplitter(messages);
        while (splitter.hasNext()){
            List<Message> listItem = splitter.next();
            SendResult send = producer.send(listItem);
            System.out.printf("%s%n", send);
        }
        System.out.println("消息发送完毕, 关闭消费者");
        producer.shutdown();
    }
}


