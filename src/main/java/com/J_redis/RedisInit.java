package com.J_redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by yu.wang on 2017/8/30.
 */
public class RedisInit {
    public static Jedis Setup() {
        final String RedisAddress = "127.0.0.1";
        final int RedisPort = 6379;
        final int RedisTimeout = 2000;
        /**
         * 初始化连接池
         */
        JedisPoolConfig config = new JedisPoolConfig();
        JedisPool pool = new JedisPool(new JedisPoolConfig(), RedisAddress, RedisPort, RedisTimeout);
        Jedis jedis = pool.getResource();
        return jedis;
    }
}
