package com.anzhi.clusterjedis.service;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class JedisClusterDemo {
    public static void main(String[] args) throws IOException {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxTotal(20);
        config.setMinIdle(5);

        Set<HostAndPort> jedisClusterNode = new HashSet<HostAndPort>();
        jedisClusterNode.add(new HostAndPort("10.211.55.3", 8001));
        jedisClusterNode.add(new HostAndPort("10.211.55.3", 8002));
        jedisClusterNode.add(new HostAndPort("10.211.55.4", 8003));
        jedisClusterNode.add(new HostAndPort("10.211.55.4", 8004));
        jedisClusterNode.add(new HostAndPort("10.211.55.5", 8005));
        jedisClusterNode.add(new HostAndPort("10.211.55.5", 8006));

        JedisCluster jedisCluster = null;
        try{
            //connectionTimeout:指的是连接一个url的连接等待时间
            //soTimeout:指的是连接上一个url，获取response的返回等待时间
            jedisCluster = new JedisCluster(jedisClusterNode, 6000, 5000, 10, config);
            System.out.println(jedisCluster.set("clusterA", "clusterA"));
            System.out.println(jedisCluster.get("clusterA"));
        }catch (Exception e){
            // doNothing
        }finally {
            if (jedisCluster != null) {
                jedisCluster.close();
            }
        }
    }
}
