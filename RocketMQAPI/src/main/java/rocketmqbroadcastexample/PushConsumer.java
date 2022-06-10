package rocketmqbroadcastexample;

import common.Constant;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * @Package: rocketmqbroadcast
 * @ClassName: PushConsumer
 * @Author: AZ
 * @CreateTime: 2021/8/14 18:56
 * @Description:
 */
public class PushConsumer {

    public static void main(String[] args) throws InterruptedException, MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("please_rename_unique_group_name_1");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        // 设置消费者的消费模式
        consumer.setMessageModel(MessageModel.BROADCASTING);
        consumer.subscribe("OrderTopicTest", "*");

        consumer.setNamesrvAddr(Constant.NAMESERVER);

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for(int i=0; i<msgs.size(); i++) {
                    System.out.printf("Recive New Message: %s %n", new String(msgs.get(i).getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.out.printf("Broadcast Consumer Started. %n");
    }
}
