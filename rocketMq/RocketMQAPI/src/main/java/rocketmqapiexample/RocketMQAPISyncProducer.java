package rocketmqapiexample;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @Package: PACKAGE_NAME
 * @Author: AZ
 * @CreateTime: 2021/8/9 17:39
 * @Description: 同步发送消息
 */
public class RocketMQAPISyncProducer {
    public final static String NAMESERVER = "10.211.55.3:9876;10.211.55.4:9876;10.211.55.5:9876";

    public static void main(String[] args) throws Exception{
        // 使用生产者组名称实例化。
        DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
        // 指定nameServer地址
        producer.setNamesrvAddr(NAMESERVER);
        //启动生产者
        producer.start();
        for(int i=0; i<100; i++){
            //创建信息实体，指定主题，目标，和消息正文
            Message msg = new Message("TopicTest" /* Topic */,"TagA" /* Tag */,("Hello RocketMQ " + i)
                    .getBytes(RemotingHelper.DEFAULT_CHARSET)
            );

            //调用 send message 将消息发送给消息处理中心Broker，Broker 会返回一个消息状态给 Producer
            SendResult result = producer.send(msg);
            SendStatus sendStatus = result.getSendStatus();
            System.out.println(sendStatus);
            System.out.println("消息发出"+result);
        }

        Thread.sleep(5000);
        producer.shutdown();
    }
}
