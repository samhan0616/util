package edu.neu.ccs.util.jedis;

/**
 * Created by Administrator on 2018/3/2.
 */
public class JRedisPoolConfig {
    public static String REDIS_IP = "";
    public static int REDIS_PORT;
    public static String REDIS_PASSWORD;
    public static int maxTotal = 500;
    public static int maxIdle = 5;
    public static long maxWaitMillis = 100000L;
    public static boolean testOnBorrow = true;
    public static Integer dbNum = 0;

    public JRedisPoolConfig() {
    }
}
