package com.anzhi.redislock.controller;

import ch.qos.logback.core.encoder.EchoEncoder;
import org.redisson.Redisson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class RedisLockController {
    
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    
    @Resource
    private RedisTemplate redisTemplate;
    
    @Resource
    private Redisson redisson;

    @RequestMapping("/deduct_stock")
    public String deductStock() {
        synchronized (this) {
            // 提前在redis服务上设置 300 个库存
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock > 0) {
                int realStock = stock - 1;
                // 重新设置缓存
                stringRedisTemplate.opsForValue().set("stock", realStock + "");
                System.out.println("扣减库存成功，剩余库存：" + realStock);
            } else {
                System.out.println("扣减库存失败，库存不足");
            }
            return String.valueOf(stock);
        }
    }

    /**
     * redis 实现一个简单的分布式锁
     * @return
     */
    @RequestMapping("/deduct_stock_redislock")
    public String deductStockByRedisLock() {
        // 以商品 id 设置锁, 以及超时时间，防止宕机死锁。setIfAbsent 保证命令的原子性
        // 将商品名称用 UUID 替换，用来当作分布式锁的线程标识
        String clientId = UUID.randomUUID().toString();
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("product_Id_A",clientId, 10, TimeUnit.SECONDS);
        // 如果设置锁失败，表明抢锁失败，业务上可以禁止下单或者返回一个错误码，让前端返回一个友好的提示
        if(Boolean.FALSE.equals(aBoolean)){
            return "error_code";
        }
        
        // 捕获业务异常释放锁，防止死锁
        try {
            // 提前在redis服务上设置 300 个库存
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock > 0) {
                int realStock = stock - 1;
                // 重新设置缓存
                stringRedisTemplate.opsForValue().set("stock", realStock + "");
                System.out.println("扣减库存成功，剩余库存：" + realStock);
            } else {
                System.out.println("扣减库存失败，库存不足");
            }
            return String.valueOf(stock);
        }finally {
            if(clientId.equals(stringRedisTemplate.opsForValue().get("product_Id_A"))){
                redisTemplate.delete("product_Id_A");
            }
        }
    }
}
