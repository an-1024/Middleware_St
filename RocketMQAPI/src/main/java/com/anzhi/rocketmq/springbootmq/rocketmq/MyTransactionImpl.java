package com.anzhi.rocketmq.springbootmq.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQUtil;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.StringMessageConverter;

import java.util.concurrent.ConcurrentHashMap;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * @Package: com.anzhi.rocketmq.mq
 * @ClassName: MyTransactionImpl
 * @Author: AZ
 * @CreateTime: 2021/8/26 14:47
 * @Description:
 */
@RocketMQTransactionListener(rocketMQTemplateBeanName = "rocketMQTemplate")
@Slf4j
public class MyTransactionImpl implements RocketMQLocalTransactionListener {

    private ConcurrentHashMap<Object, String> localTrans = new ConcurrentHashMap<>();

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        log.info("executeLocalTransaction开始执行");
        Object id = message.getHeaders().get("id");
        String destination = o.toString();
        localTrans.put(id, destination);
        org.apache.rocketmq.common.message.Message msg = RocketMQUtil.convertToRocketMessage(new StringMessageConverter(), "UTF-8", destination, message);
        String tags = msg.getTags();
        if(StringUtils.contains(tags, "TagA")){
            return RocketMQLocalTransactionState.COMMIT;
        }else if(StringUtils.contains(tags, "TagB")){
            return RocketMQLocalTransactionState.ROLLBACK;
        }else{
            return RocketMQLocalTransactionState.UNKNOWN;
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        //SpringBoot的消息对象中，并没有transactionId这个属性。跟原生API不一样。
        //String destination = localTrans.get(msg.getTransactionId());
        log.info("checkLocalTransaction开始执行");
        return RocketMQLocalTransactionState.COMMIT;
    }
}
