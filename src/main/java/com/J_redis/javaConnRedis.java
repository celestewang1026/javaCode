package com.J_redis;
/**
 * Created by yu.wang.a on 2017/8/30.
 */
import com.google.common.io.LineProcessor;
import org.elasticsearch.common.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ResourceBundle;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.Jedis;

public class javaConnRedis {
    private static final Logger logger = LoggerFactory.getLogger(javaConnRedis.class);
    private static final String KEY_SEPRATOR = "|+|";

    public static  void main(String args[]) {



        //String fileName = "F:/project/wrong_topic_book_test.txt";
        String fileName = args[0];
        long start = System.currentTimeMillis();
        System.out.println(start);
        logger.info("### file: {} ,start", fileName);

        logger.info("### file: {} end, spent {} ms", fileName, (System.currentTimeMillis() - start));
        /*
        连接池
         */
        final JedisCluster jedis = RedisInit2.Setup();
        final Jedis jc = RedisInit.Setup();
        final JedisCluster gjc = getJC();

        try {


            Files.readLines(new File(fileName), Charset.defaultCharset(), new LineProcessor<String>() {

                public boolean processLine(String line) throws IOException {
                    if (Strings.isNullOrEmpty(line)) {
                        return false;
                    }
                    try {
                        String[] split = line.split("\t");
                        String studentId = split[0];
                        String questionId = split[1];
                        String sortTime = split[2];
                        String subject = split[3];
                        String status = split[4];
                        String unitId = split[5];
                        if ("null".equals(unitId)) unitId = "unknown";
                        String userAnswers = split[6];
                        if ("null".equals(userAnswers)) userAnswers = "[]";
                        String subGrasp = split[7];
                        if ("null".equals(subGrasp)) subGrasp = "[]";
                        String learningType = split[8];
                        String key = studentId + KEY_SEPRATOR + subject;
                        String subKey = questionId;
                        String subValue = unitId + KEY_SEPRATOR + status + KEY_SEPRATOR + sortTime + KEY_SEPRATOR + userAnswers + KEY_SEPRATOR + subGrasp + KEY_SEPRATOR + learningType;
                        System.out.println(key.getBytes());
                        System.out.println(subKey.getBytes());
                        System.out.println(subValue.getBytes());

                        Long hset = gjc.hset(key.getBytes(), subKey.getBytes(), subValue.getBytes());

                       // Long hset = jc.hset(key.getBytes(), subKey.getBytes(), subValue.getBytes());

                        if (hset == 0) {
                            logger.warn("返回为0, line:{}", line);
                        }

                       // System.out.println(key+"\t"+subKey+"\t"+subValue);
                    } catch (Exception e1) {
                        logger.error("line: {} with exception: {}", line, e1);
                        return false;
                    }

                    return true;
                }


                public String getResult() {
                    return null;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("data load successed ! ");

    }


    /**
     * g根据appid索引集群
     * @return
     */
    public static JedisCluster getJC() {
        ResourceBundle rb = ResourceBundle.getBundle("database");
        String redisAppIdName = "redis.appid";
        RedisCloudFactory redisCloudFactory = new RedisCloudFactory(Long.parseLong(rb.getString(redisAppIdName)));
        return redisCloudFactory.getInstance();
    }

}
