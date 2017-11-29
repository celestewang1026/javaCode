package com.J_redis;

import com.sohu.tv.builder.ClientBuilder;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisCluster;

/**
 * Created by yu.wang.a 2017/09/04
 */
public class RedisCloudFactory {

    private Long appId;
    private static JedisCluster jedisCluster = null;

    private int connTimeout = 1000;
    private int soTimeout = 1000;
    private int maxRedirections = 10;

    public RedisCloudFactory(Long appId) {
        this.appId = appId;
        init();
    }

    public RedisCloudFactory(Long appId, int connTimeout, int soTimeout, int maxRedirections){
        this.appId = appId;
        this.connTimeout = connTimeout;
        this.soTimeout = soTimeout;
        this.maxRedirections = maxRedirections;
        init();
    }

    public JedisCluster getInstance() {
        return jedisCluster;
    }

    private void init() {
        /**
         * 使用自定义配置：
         * 1. setTimeout：redis操作的超时设置；
         * 2. setMaxRedirections：节点定位重试的次数；
         */
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        jedisCluster = ClientBuilder.redisCluster(appId)
                .setJedisPoolConfig(poolConfig)
                .setConnectionTimeout(connTimeout)
                .setSoTimeout(soTimeout)
                .setMaxRedirections(maxRedirections)
                .build();
    }
}
