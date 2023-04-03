package rocketmqapiexample;

import common.Constant;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @Package: PACKAGE_NAME
 * @Author: AZ
 * @CreateTime: 2021/8/10 17:47
 * @Description: 单向发送消息样例
 */
public class RocketMQAPIOnewayProducer {
    public static void main(String[] args) throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("Topic_Oneway_Group");
        producer.setNamesrvAddr(Constant.NAMESERVER);
        producer.start();

        for(int i=0; i<10; i++){
            Message msg = new Message("TopicOneway",
                    "TagA",
                    ("Hello RocketMQ" +i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.sendOneway(msg);
            System.out.println("发送消息完成");
        }
        Thread.sleep(5000);
        System.out.println("关闭 producer");
        producer.shutdown();
    }
}
