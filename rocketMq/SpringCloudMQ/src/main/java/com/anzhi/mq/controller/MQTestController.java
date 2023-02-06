package com.anzhi.mq.controller;

import com.anzhi.mq.scproducer.ScProducer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Package: com.anzhi.mq.controller
 * @ClassName: MQTestController
 * @Author: AZ
 * @CreateTime: 2021/8/28 20:52
 * @Description:
 */
@RestController
@RequestMapping("/MQTest")
public class MQTestController {
    @Resource
    private ScProducer producer;

    @RequestMapping("/sendMessage")
    public String sendMessage(String message){
        producer.sendMessage(message);
        return "消息发送完成";
    }
}
