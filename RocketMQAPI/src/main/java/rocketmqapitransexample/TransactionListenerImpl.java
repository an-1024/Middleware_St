package rocketmqapitransexample;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @Package: rocketmqapitransexample
 * @ClassName: LocalTransactionState
 * @Author: AZ
 * @CreateTime: 2021/8/21 15:43
 * @Description:
 */
public class TransactionListenerImpl implements TransactionListener {
    // 在提交玩事物消息后执行
    // 返回COMMIT_MESAGE状态的消息会立即被消费者消费到
    // 返回ROLLBACK_MESSAGE状态的消息会被丢弃
    // 返回UNKNOW状态的消息会由Broker过一段时间在来回查事物状态
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        String tags = msg.getTags();
        // TagA的消息会立即被消费者消费到
        if(StringUtils.contains(tags, "TagA")){
            return LocalTransactionState.COMMIT_MESSAGE;
            // TagB的消息会被丢弃
        }else if(StringUtils.contains(tags, "TagB")){
            return LocalTransactionState.ROLLBACK_MESSAGE;
            // 其他消息会等待Broker进行事物状态回查
        }else{
            return LocalTransactionState.UNKNOW;
        }
    }

    // 在对UNKNOW状态的消息进行状态回查时执行。返回的结果是一样的。
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        String tags = msg.getTags();
        // TagC 的消息过一段时间会被消费者消费到
        if(StringUtils.contains(tags, "TagC")){
            return LocalTransactionState.COMMIT_MESSAGE;
            // TagD的消息也会在状态回查时被丢弃掉
        }else if(StringUtils.contains(tags, "TagD")){
            return LocalTransactionState.ROLLBACK_MESSAGE;
            // 剩下的TagE消息会在多次状态回查后最终丢弃
        }else{
            return LocalTransactionState.UNKNOW;
        }
    }
}
