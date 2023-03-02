package com.anzhi.sentineljedis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SentinelIndexCOntroller {
    private static final Logger logger = LoggerFactory.getLogger(SentinelIndexCOntroller.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 测试节点挂了哨兵重新选举新的master节点，客户端是否能动态感知到
     *
     * @throws InterruptedException
     */
    @RequestMapping("/test_sentinel")
    public void testSentinel() throws InterruptedException {
        int i = 1;
        while (true){
            try{
                stringRedisTemplate.opsForValue().set("master"+i, i+""); //jedis.set(key,value);
                System.out.println("设置key："+ "master" + i);
                Thread.sleep(10000);
                stringRedisTemplate.delete("master" + i);
                i++;
            }catch (Exception e){
                logger.error("错误：", e);
            }
        }
    }
}
