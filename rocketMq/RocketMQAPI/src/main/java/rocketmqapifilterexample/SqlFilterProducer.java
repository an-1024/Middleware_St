package rocketmqapifilterexample;

import common.Constant;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @Package: rocketmqapifilterexample
 * @ClassName: SqlFilterProducer
 * @Author: AZ
 * @CreateTime: 2021/8/21 10:39
 * @Description:
 */
public class SqlFilterProducer {
    public static void main(String[] args) throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
        producer.setNamesrvAddr(Constant.NAMESERVER);
        producer.start();

        String[] tags = new String[] {"TagA", "TagB", "TagC"};
        for(int i=0; i<15; i++){
            Message msg = new Message("SqlFiltertest", tags[i % tags.length], ("hello filter sql").
                    getBytes(RemotingHelper.DEFAULT_CHARSET));
            msg.putUserProperty("a", String.valueOf(i));

            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
        }

        producer.shutdown();
    }
}
