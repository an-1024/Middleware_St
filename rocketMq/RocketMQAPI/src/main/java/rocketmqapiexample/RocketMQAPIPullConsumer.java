package rocketmqapiexample;

import common.Constant;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Package: PACKAGE_NAME
 * @Author: AZ
 * @CreateTime: 2021/8/10 18:13
 * @Description: 消费模式-拉模式
 */
public class RocketMQAPIPullConsumer {
    private static final Map<MessageQueue, Long> OFFSET_TABLE = new HashMap<>();

    public static void main(String[] args) throws Exception{
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("TopicOneway_Group");
        consumer.setNamesrvAddr(Constant.NAMESERVER);
        consumer.start();

        Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues("TopicOneway");
        for(MessageQueue mq:mqs){
            System.out.printf("Consume from the queue: %s%n", mq);
            SINGLE_MQ:
            while (true){
                try{
                    PullResult pullResult = consumer.pullBlockIfNotFound(mq, null, getMessageQueueOffset(mq), 32);
                    System.out.printf("%s%n", pullResult);
                    putMessageQueueOffset(mq, pullResult.getNextBeginOffset());
                    switch (pullResult.getPullStatus()){
                        case FOUND:
                            break;
                        case NO_MATCHED_MSG:
                            break ;
                        case NO_NEW_MSG:
                            break SINGLE_MQ;
                        case OFFSET_ILLEGAL:
                            break ;
                        default:
                            break ;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        consumer.shutdown();
    }


    private static Long getMessageQueueOffset(MessageQueue mq){
        Long offset = OFFSET_TABLE.get(mq);
        if(offset != null){
            return offset;
        }
        return 0L;
    }

    private static void putMessageQueueOffset(MessageQueue mq, Long offset){
        OFFSET_TABLE.put(mq, offset);
    }
}
