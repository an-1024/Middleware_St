package com.anzhi.sentineljedis.service;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

public class JedisSentinelDemo {
    public static void main(String[] args) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(10);
        config.setMinIdle(5);

        String masterName = "mymaster";
        Set<String> sentinels = new HashSet<String>();
        sentinels.add(new HostAndPort("10.211.55.3",26379).toString());
        sentinels.add(new HostAndPort("10.211.55.4",26380).toString());
        sentinels.add(new HostAndPort("10.211.55.5",26381).toString());

        //JedisSentinelPool其实本质跟JedisPool类似，都是与redis主节点建立的连接池
        //JedisSentinelPool并不是说与sentinel建立的连接池，而是通过sentinel发现redis主节点并与其建立连接
        JedisSentinelPool jedisSentinelPool = new JedisSentinelPool(masterName, sentinels, config, 3000, null);
        Jedis jedis = null;
        try{
            jedis = jedisSentinelPool.getResource();
            System.out.println(jedis.set("sentinel", "masterA"));
            System.out.println(jedis.get("sentinel"));
            System.out.println(jedis.del("sentinel"));
        }catch (Exception e){
            // doNothing
        }finally {
            //注意这里不是关闭连接，在JedisPool模式下，Jedis会被归还给资源池。
            if (jedis != null){
                jedis.close();
            }
        }
    }
}
