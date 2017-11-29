package com.J_redis;
/**
 * Created by yu.wang.a on 2017/8/30.
 */

import redis.clients.jedis.JedisCluster;

public class RedisReader {

    private static final String KEY_SEPRATOR = "|+|";
    public static  void main(String args[]) {
        final JedisCluster jedis = javaConnRedis.getJC();

            String student_id="30013";
            String subject="102";
            String key = student_id+KEY_SEPRATOR+subject;

            System.out.println(jedis.hgetAll(key));

    }

}
