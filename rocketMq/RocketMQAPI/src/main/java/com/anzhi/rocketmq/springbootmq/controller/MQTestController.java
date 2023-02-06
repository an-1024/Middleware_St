package com.anzhi.rocketmq.springbootmq.controller;

import com.anzhi.rocketmq.springbootmq.rocketmq.SpringProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;

/**
 * @Package: com.anzhi.rocketmq.mq.controller
 * @ClassName: MQTestController
 * @Author: AZ
 * @CreateTime: 2021/8/26 15:12
 * @Description:
 */
@RestController
@RequestMapping("/MQTest")
public class MQTestController {

    @Resource
    private SpringProducer springProducer;

    @GetMapping("/sendMessage")
    public String sendMessage(@RequestParam("message") String message){
        springProducer.sendMessage("TestTopic", message);
        return "消息发送完成";
    }

    @GetMapping("/sendTransMessage")
    public String sendTransMessage(@RequestParam("messgae") String message){
        try {
            springProducer.sendMessageInTransaction("TestTopic", message);
            return "事物消息发送完成";
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "事物消息发送失败";
    }
}
