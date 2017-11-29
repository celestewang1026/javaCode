package com.J_redis;

/**
 * Created by yu.wang.a on 2017/8/31.
 * 连接redis服务器
 */
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import java.util.Set;
import java.util.HashSet;


public class RedisInit2 {
    private static JedisCluster jedis;
    public  static JedisCluster Setup() {
        final String RedisAddress = "127.0.0.1：6379";//本地的
        final int RedisTimeout = 2000;
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        final String[] ServeArray = RedisAddress.split(",");
        for (String port:ServeArray)
        {
            String[] Portpair = port.split(":");
            nodes.add(new HostAndPort(Portpair[0].trim(),Integer.valueOf(Portpair[1].trim())));
        }
        jedis = new JedisCluster(nodes,RedisTimeout);

        return jedis;
    }

}
