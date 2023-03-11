package com.anzhi.hotjedispool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * jedis线程池
 */
public class HotJedisPoolDemo {
    // 资源池确保最少空闲的连接数
    public static final int MAX_IDLE = 60;
    public static final int MAX_TOTAL = 60;

    private static JedisPool pool = null;

    static {
        //类加载时，即创建JedisPool
        jedisPoolConfig();
        //预热
        hotJedisPool();
    }

    /**
     * 创建JedisPool
     *
     * @return
     */
    private static JedisPool jedisPoolConfig() {
        if (pool == null) {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(MAX_TOTAL);
            jedisPoolConfig.setMaxIdle(MAX_IDLE);
            jedisPoolConfig.setMaxWaitMillis(1000 * 10);
            pool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379, 10000);
        }
        return pool;
    }

    /**
     * 预热连接
     */
    public static void hotJedisPool() {
        List<Jedis> midJedisList = new ArrayList<>();
        Jedis jedis = null;
        //新建Jedispool资源池允许最大空闲的连接数
        for (int i = 0; i < MAX_IDLE; i++) {
            jedis = pool.getResource();
            midJedisList.add(jedis);
            jedis.ping();
        }
        //归还连接
        for (int i = 0; i < MAX_IDLE; i++) {
            jedis = midJedisList.get(i);
            jedis.close();
        }
    }

    /**
     * 从JedisPool连接池中获取一个Jedis
     *
     * @return
     */
    public static Jedis getJedis() {
        Jedis jedis = pool.getResource();
        return jedis;
    }
}
