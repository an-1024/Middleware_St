package com.anzhi.rocketmq;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Package: PACKAGE_NAME
 * @ClassName: ScRocketMQApplication
 * @Author: AZ
 * @CreateTime: 2021/8/22 20:25
 * @Description:
 */
@SpringBootApplication(scanBasePackages = {"com.anzhi.rocketmq.*"})
public class RocketMQScApplication {
    public static void main(String[] args) {
        SpringApplication.run(RocketMQScApplication.class, args);
    }
}
