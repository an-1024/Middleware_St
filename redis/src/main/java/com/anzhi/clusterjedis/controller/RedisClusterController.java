package com.anzhi.clusterjedis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisClusterController {
    private static final Logger logger = LoggerFactory.getLogger(RedisClusterController.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/rediscluster")
    public String testCluster() throws InterruptedException {
        stringRedisTemplate.opsForValue().set("clusterA", "clusterA");
        System.out.println(stringRedisTemplate.opsForValue().get("clusterA"));
        return stringRedisTemplate.opsForValue().get("clusterA");
    }
}
