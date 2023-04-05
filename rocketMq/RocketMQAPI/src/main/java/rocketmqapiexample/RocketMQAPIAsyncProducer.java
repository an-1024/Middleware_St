package rocketmqapiexample;

import common.Constant;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Package: PACKAGE_NAME
 * @Author: AZ
 * @CreateTime: 2021/8/10 11:37
 * @Description: 异步发送消息
 */
public class RocketMQAPIAsyncProducer {
    public static void main(String[] args) throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("As_Sync_Producer");
        producer.setNamesrvAddr(Constant.NAMESERVER);
        producer.start();
        // 消息重试次数
        producer.setRetryTimesWhenSendAsyncFailed(0);
        //设置大的话，会抛错broker busy，因为Broker在追加消息时，持有的锁超过了200ms, 怎么解决？？？
        // 10没问题，100会导致队列中的任务等待时间超过200ms，此时会触发broker端的快速失败
        // 具体详见：https://blog.csdn.net/prestigeding/article/details/102714973
        int messageCount = 10;
        //由于是异步发送，这里引入一个countDownLatch，保证所有Producer发送消息的回调方法都执行完了再停止Producer服务。
        final CountDownLatch countDownLatch = new CountDownLatch(messageCount);
        for(int i=0; i<messageCount; i++) {
            try {
                final int index = i;
                Message msg = new Message("TopicTestAsync",
                        "TagA",
                        "ordId188",
                        "Hello World".getBytes(RemotingHelper.DEFAULT_CHARSET));
                producer.send(msg, new SendCallback() {
                    // 当 Broker 接收到 Producer 消息，表示消息发送成功，此时会回掉 onSuccess
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        countDownLatch.countDown();
                        System.out.printf("%-10d ok %s %n", index, sendResult.getMsgId());
                    }

                    // 失败则回掉 onException 方法
                    @Override
                    public void onException(Throwable e) {
                        countDownLatch.countDown();
                        System.out.printf("%-10d Exception %s %n", index, e);
                        e.printStackTrace();
                    }
                });
                System.out.println("消息发送完成");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        countDownLatch.await(5, TimeUnit.SECONDS);
        producer.shutdown();
    }
}
