package com.anzhi.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;


/**
 * @Package: com.anzhi.mq
 * @ClassName: ScApplication
 * @Author: AZ
 * @CreateTime: 2021/8/28 19:16
 * @Description:
 */
@EnableBinding({Source.class, Sink.class})
@SpringBootApplication(scanBasePackages = "com.anzhi.mq.*")
public class ScRocketMQApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScRocketMQApplication.class,args);
    }
}
