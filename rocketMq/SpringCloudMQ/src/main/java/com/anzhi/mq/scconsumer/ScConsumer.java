package com.anzhi.mq.scconsumer;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

/**
 * @Package: com.anzhi.mq.scconsumer
 * @ClassName: ScConsumer
 * @Author: AZ
 * @CreateTime: 2021/8/28 20:35
 * @Description:
 */
@Component
public class ScConsumer {
    @StreamListener(Sink.INPUT)
    public void onMessage(String message){
        System.out.println("received message:"+message+" from binding:"+
                Sink.INPUT);
    }
}
