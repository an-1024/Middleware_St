package rocketmqapiexample;

import common.Constant;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @Package: PACKAGE_NAME
 * @Author: AZ
 * @CreateTime: 2021/8/10 17:47
 * @Description:
 */
public class RocketMQAPIOnewayProducer {
    public static void main(String[] args) throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
        producer.setNamesrvAddr(Constant.NAMESERVER);
        producer.start();

        for(int i=0; i<100; i++){
            Message msg = new Message("TopicOneway",
                    "TagA",
                    ("Hello RocketMQ" +i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.sendOneway(msg);
        }
        Thread.sleep(5000);
        producer.shutdown();
    }
}
