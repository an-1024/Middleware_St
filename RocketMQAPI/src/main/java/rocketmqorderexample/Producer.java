package rocketmqorderexample;

import common.Constant;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @Package: RocketMQOrderExample
 * @ClassName: Producer
 * @Author: AZ
 * @CreateTime: 2021/8/10 20:07
 * @Description:
 */
public class Producer {
    public static void main(String[] args) throws UnsupportedEncodingException {
        DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
        try {
            producer.setNamesrvAddr(Constant.NAMESERVER);
            producer.start();

            for(int i=0; i<10; i++){
                int orderId = i;

                for(int j=0; j<=5; j++){
                    Message msg = new Message("OrderTopicTest", "order_"+orderId, "KEY"+orderId,
                            ("order_"+orderId+" step "+ j).getBytes(RemotingHelper.DEFAULT_CHARSET));
                    System.out.println("发送的消息内容为-》{"+new String(msg.getBody()) + "}");
                    SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                        @Override
                        public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                            // 将相同orderId的消息放入同一个队列
                            Integer id = (Integer) arg;
                            int index = id % mqs.size();
                            return mqs.get(index);
                        }
                    }, orderId);
                    System.out.printf("%s%n", sendResult);
                }
            }
            producer.shutdown();
        }catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
