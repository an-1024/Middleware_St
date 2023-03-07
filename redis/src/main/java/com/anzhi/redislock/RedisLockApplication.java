package com.anzhi.redislock;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RedisLockApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedisLockApplication.class, args);
    }

    // 配置 RedisSon
    @Bean
    public Redisson redisson() {
        Config config = new Config();
        config.useClusterServers().addNodeAddress("redis://10.211.55.3:8001", "redis://10.211.55.5:8006")
                .addNodeAddress("redis://10.211.55.4:8003", "redis://10.211.55.3:8002")
                .addNodeAddress("redis://10.211.55.5:8005", "redis://10.211.55.4:8004");

        return (Redisson) Redisson.create(config);
    }
}
