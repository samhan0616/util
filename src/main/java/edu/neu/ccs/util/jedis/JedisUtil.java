package edu.neu.ccs.util.jedis;


/**
 * Created by Administrator on 2018/3/2.
 */

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.SortingParams;
import redis.clients.util.SafeEncoder;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class JedisUtil {
    private final int expire = '\uea60';
    public JedisUtil.Keys KEYS;
    public JedisUtil.Strings STRINGS;
    public JedisUtil.Lists LISTS;
    public JedisUtil.Sets SETS;
    public JedisUtil.Hash HASH;
    public JedisUtil.SortSet SORTSET;
    private static JedisPool jedisPool = null;
    private static final JedisUtil jedisUtil;

    private JedisUtil() {
    }

    public JedisPool getPool() {
        return jedisPool;
    }

    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    public static JedisUtil getInstance() {
        return jedisUtil;
    }

    public void returnJedis(Jedis jedis) {
        if(null != jedis && null != jedisPool) {
            jedis.close();
        }

    }

    public static void returnBrokenResource(Jedis jedis) {
        if(null != jedis && null != jedisPool) {
            jedis.close();
        }

    }

    public void expire(String key, int seconds) {
        Jedis jedis = this.getJedis();
        try{
            if(seconds > 0) {
                jedis.expire(key, seconds);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.returnJedis(jedis);
        }

    }

    public void expire(String key) {
        this.expire(key, '\uea60');
    }


    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(JRedisPoolConfig.maxTotal);
        config.setMaxIdle(JRedisPoolConfig.maxIdle);
        config.setMaxWaitMillis(JRedisPoolConfig.maxWaitMillis);
        config.setTestOnBorrow(JRedisPoolConfig.testOnBorrow);
        if(JRedisPoolConfig.REDIS_PASSWORD != null && !"".equals(JRedisPoolConfig.REDIS_PASSWORD)) {
            jedisPool = new JedisPool(config, JRedisPoolConfig.REDIS_IP, JRedisPoolConfig.REDIS_PORT, 10000, JRedisPoolConfig.REDIS_PASSWORD);
        } else {
            jedisPool = new JedisPool(config, JRedisPoolConfig.REDIS_IP, JRedisPoolConfig.REDIS_PORT);
        }
        jedisUtil = new JedisUtil();
    }

    public class Lists {
        public Lists() {
        }

        public long llen(String key) {
            return this.llen(SafeEncoder.encode(key));
        }

        public long llen(byte[] key) {
            Jedis sjedis = JedisUtil.this.getJedis();
            long count;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                count = sjedis.llen(key).longValue();
                return count;
            }catch (Exception e){
                e.printStackTrace();
                count = 0L;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return  0L;
        }

        public String lset(byte[] key, int index, byte[] value) {
            Jedis jedis = JedisUtil.this.getJedis();
            String status;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                status = jedis.lset(key, (long)index, value);
                return status;
            }catch (Exception e){
                e.printStackTrace();
                status =  null;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return status;
        }

        public String lset(String key, int index, String value) {
            return this.lset(SafeEncoder.encode(key), index, SafeEncoder.encode(value));
        }

        public long linsert(String key, LIST_POSITION where, String pivot, String value) {
            return this.linsert(SafeEncoder.encode(key), where, SafeEncoder.encode(pivot), SafeEncoder.encode(value));
        }

        public long linsert(byte[] key, LIST_POSITION where, byte[] pivot, byte[] value) {
            Jedis jedis = JedisUtil.this.getJedis();
            long count;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                count = jedis.linsert(key, where, pivot, value).longValue();
                return count;
            }catch (Exception e){
                e.printStackTrace();
                count = -1L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return count;
        }

        public String lindex(String key, int index) {
            return SafeEncoder.encode(this.lindex(SafeEncoder.encode(key), index));
        }

        public byte[] lindex(byte[] key, int index) {
            Jedis sjedis = JedisUtil.this.getJedis();
            byte[] value;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                value = sjedis.lindex(key, (long)index);
                return value;
            }catch (Exception e){
                value =  null;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return value;

        }

        public String lpop(String key) {
            return SafeEncoder.encode(this.lpop(SafeEncoder.encode(key)));
        }

        public byte[] lpop(byte[] key) {
            Jedis jedis = JedisUtil.this.getJedis();
            byte[] value;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                value = jedis.lpop(key);
                return value;
            }catch (Exception e){
                e.printStackTrace();
                value =  null;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return value;
        }

        public String rpop(String key) {
            Jedis jedis = JedisUtil.this.getJedis();
            String value;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                value = jedis.rpop(key);
                return value;
            }catch (Exception e){
                e.printStackTrace();
                value = null;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return value;
        }

        public long lpush(String key, String value) {
            return this.lpush(SafeEncoder.encode(key), SafeEncoder.encode(value));
        }

        public long rpush(String key, String value) {
            Jedis jedis = JedisUtil.this.getJedis();
            long count;
            try{
                jedis.select(JRedisPoolConfig.dbNum);
                count = jedis.rpush(key, new String[]{value}).longValue();
                return count;
            }catch (Exception e){
                e.printStackTrace();
                count = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return count;
        }

        public long rpush(byte[] key, byte[] value) {
            Jedis jedis = JedisUtil.this.getJedis();
            long count;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                count = jedis.rpush(key, new byte[][]{value}).longValue();
                return count;
            }catch (Exception e){
                e.printStackTrace();
                count = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return count;
        }

        public long lpush(byte[] key, byte[] value) {
            Jedis jedis = JedisUtil.this.getJedis();
            long count;
            try{
                jedis.select(JRedisPoolConfig.dbNum);
                count = jedis.lpush(key, new byte[][]{value}).longValue();
                return count;
            }catch (Exception e){
                e.printStackTrace();
                count = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return count;
        }

        public List<String> lrange(String key, long start, long end) {
            Jedis sjedis = JedisUtil.this.getJedis();
            List list;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                list = sjedis.lrange(key, start, end);
                return list;
            }catch (Exception e){
                e.printStackTrace();
                list = null;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return list;
        }

        public List<byte[]> lrange(byte[] key, int start, int end) {
            Jedis sjedis = JedisUtil.this.getJedis();
            List list;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                list = sjedis.lrange(key, (long)start, (long)end);
                return list;
            }catch (Exception e){
                e.printStackTrace();
                list = null;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return list;

        }

        public long lrem(byte[] key, int c, byte[] value) {
            Jedis jedis = JedisUtil.this.getJedis();
            long count;
            try{
                jedis.select(JRedisPoolConfig.dbNum);
                count = jedis.lrem(key, (long)c, value).longValue();
                return count;
            }catch (Exception e){
                e.printStackTrace();
                count = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return count;
        }

        public long lrem(String key, int c, String value) {
            return this.lrem(SafeEncoder.encode(key), c, SafeEncoder.encode(value));
        }

        public String ltrim(byte[] key, int start, int end) {
            Jedis jedis = JedisUtil.this.getJedis();
            String str;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                str = jedis.ltrim(key, (long)start, (long)end);
                return str;
            }catch (Exception e){
                e.printStackTrace();
                str = null;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return str;

        }

        public String ltrim(String key, int start, int end) {
            return this.ltrim(SafeEncoder.encode(key), start, end);
        }
    }

    public class Strings {
        public Strings() {
        }

        public String get(String key) {
            Jedis sjedis = JedisUtil.this.getJedis();
            String value;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                value = sjedis.get(key);
                return value;
            }catch (Exception e){
                e.printStackTrace();
                value = null;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return value;
        }

        public byte[] get(byte[] key) {
            Jedis sjedis = JedisUtil.this.getJedis();
            byte[] value;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                value = sjedis.get(key);
                return value;
            }catch (Exception e){
                e.printStackTrace();
                value = null;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return value;
        }

        public String setEx(String key, int seconds, String value) {
            Jedis jedis = JedisUtil.this.getJedis();
            String str;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                str = jedis.setex(key, seconds, value);
                return str;
            }catch (Exception e){
                e.printStackTrace();
                str = null;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return str;
        }

        public String setEx(byte[] key, int seconds, byte[] value) {
            Jedis jedis = JedisUtil.this.getJedis();
            String str;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                str = jedis.setex(key, seconds, value);
                return str;
            }catch (Exception e){
                e.printStackTrace();
                str =  null;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return str;
        }

        public long setnx(String key, String value) {
            Jedis jedis = JedisUtil.this.getJedis();
            long str;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                str = jedis.setnx(key, value).longValue();
                return str;
            }catch (Exception e){
                e.printStackTrace();
                str = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return str;
        }

        public String set(String key, String value) {
            return this.set(SafeEncoder.encode(key), SafeEncoder.encode(value));
        }

        public String set(String key, byte[] value) {
            return this.set(SafeEncoder.encode(key), value);
        }

        public String set(byte[] key, byte[] value) {
            Jedis jedis = JedisUtil.this.getJedis();
            String status;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                status = jedis.set(key, value);
                return status;
            }catch (Exception e){
                status = null;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return status;
        }

        public long setRange(String key, long offset, String value) {
            Jedis jedis = JedisUtil.this.getJedis();
            long len;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                len = jedis.setrange(key, offset, value).longValue();
                return len;
            }catch (Exception e){
                e.printStackTrace();
                len = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return len;
        }

        public long append(String key, String value) {
            Jedis jedis = JedisUtil.this.getJedis();
            long len;
            try{
                jedis.select(JRedisPoolConfig.dbNum);
                len = jedis.append(key, value).longValue();
                return len;
            }catch (Exception e){
                len = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return len;
        }

        public long decrBy(String key, long number) {
            Jedis jedis = JedisUtil.this.getJedis();
            long len;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                len = jedis.decrBy(key, number).longValue();
                return len;
            }catch (Exception e){
                e.printStackTrace();
                len = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return len;
        }

        public long incrBy(String key, long number) {
            Jedis jedis = JedisUtil.this.getJedis();
            long len;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                len = jedis.incrBy(key, number).longValue();
                return len;
            }catch (Exception e){
                e.printStackTrace();
                len = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return len;
        }

        public String getrange(String key, long startOffset, long endOffset) {
            Jedis sjedis = JedisUtil.this.getJedis();
            String value;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                value = sjedis.getrange(key, startOffset, endOffset);
                return value;
            }catch (Exception e){
                e.printStackTrace();
                value = null;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return value;
        }

        public String getSet(String key, String value) {
            Jedis jedis = JedisUtil.this.getJedis();
            String str;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                str = jedis.getSet(key, value);
                return str;
            }catch (Exception e){
                e.printStackTrace();
                str = null;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return str;
        }

        public List<String> mget(String... keys) {
            Jedis jedis = JedisUtil.this.getJedis();
            List str;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                str = jedis.mget(keys);
                return str;
            }catch (Exception e){
                e.printStackTrace();
                str = null;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return str;
        }

        public String mset(String... keysvalues) {
            Jedis jedis = JedisUtil.this.getJedis();
            String str;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                str = jedis.mset(keysvalues);
                return str;
            }catch (Exception e){
                e.printStackTrace();
                str = null;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return str;
        }

        public long strlen(String key) {
            Jedis jedis = JedisUtil.this.getJedis();
            long len;
            try{
                jedis.select(JRedisPoolConfig.dbNum);
                len = jedis.strlen(key).longValue();
                return len;
            }catch (Exception e){
                e.printStackTrace();
                len = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return len;
        }
    }

    public class Hash {
        public Hash() {
        }

        public long hdel(String key, String fieid) {
            Jedis jedis = JedisUtil.this.getJedis();
            long s;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                s = jedis.hdel(key, new String[]{fieid}).longValue();
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return s;
        }

        public long hdel(String key) {
            Jedis jedis = JedisUtil.this.getJedis();
            long s;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                s = jedis.del(key).longValue();
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return s;
        }

        public boolean hexists(String key, String fieid) {
            Jedis sjedis = JedisUtil.this.getJedis();
            boolean s;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                s = sjedis.hexists(key, fieid).booleanValue();
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s = false;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return s;
        }

        public String hget(String key, String fieid) {
            Jedis sjedis = JedisUtil.this.getJedis();
            String s;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                s = sjedis.hget(key, fieid);
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s = null;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return s;
        }

        public byte[] hget(byte[] key, byte[] fieid) {
            Jedis sjedis = JedisUtil.this.getJedis();
            byte[] s;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                s = sjedis.hget(key, fieid);
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s =  null;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return s;
        }

        public Map<String, String> hgetAll(String key) {
            Jedis sjedis = JedisUtil.this.getJedis();
            Map map;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                map = sjedis.hgetAll(key);
                return map;
            }catch (Exception e){
                e.printStackTrace();
                map = null;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return map;
        }

        public long hset(String key, String fieid, String value) {
            Jedis jedis = JedisUtil.this.getJedis();
            long s;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                s = jedis.hset(key, fieid, value).longValue();
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s =  0;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return s;
        }

        public long hset(String key, String fieid, byte[] value) {
            Jedis jedis = JedisUtil.this.getJedis();
            long s;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                s = jedis.hset(key.getBytes(), fieid.getBytes(), value).longValue();
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return s;
        }

        public long hsetnx(String key, String fieid, String value) {
            Jedis jedis = JedisUtil.this.getJedis();
            long s;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                s = jedis.hsetnx(key, fieid, value).longValue();
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return s;

        }

        public List<String> hvals(String key) {
            Jedis sjedis = JedisUtil.this.getJedis();
            List list;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                list = sjedis.hvals(key);
                return list;
            }catch (Exception e){
                e.printStackTrace();
                list = null;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return list;
        }

        public long hincrby(String key, String fieid, long value) {
            Jedis jedis = JedisUtil.this.getJedis();
            long s;
            try{
                jedis.select(JRedisPoolConfig.dbNum);
                s = jedis.hincrBy(key, fieid, value).longValue();
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return s;
        }

        public Set<String> hkeys(String key) {
            Jedis sjedis = JedisUtil.this.getJedis();
            Set set;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                set = sjedis.hkeys(key);
                return set;
            }catch (Exception e){
                e.printStackTrace();
                set = null;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return set;
        }

        public long hlen(String key) {
            Jedis sjedis = JedisUtil.this.getJedis();
            long len;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                len = sjedis.hlen(key).longValue();
                return len;
            }catch (Exception e){
                e.printStackTrace();
                len = 0L;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return len;
        }

        public List<String> hmget(String key, String... fieids) {
            Jedis sjedis = JedisUtil.this.getJedis();
            List list;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                list = sjedis.hmget(key, fieids);
                return list;
            }catch (Exception e){
                e.printStackTrace();
                list = null;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return list;

        }

        public List<byte[]> hmget(byte[] key, byte[]... fieids) {
            Jedis sjedis = JedisUtil.this.getJedis();
            List list;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                list = sjedis.hmget(key, fieids);
                return list;
            }catch (Exception e){
                e.printStackTrace();
                list =  null;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return list;
        }

        public String hmset(String key, Map<String, String> map) {
            Jedis jedis = JedisUtil.this.getJedis();
            String s;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                s = jedis.hmset(key, map);
                return s;
            }catch (Exception e){
                s = null;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return s;
        }

        public String hmset(byte[] key, Map<byte[], byte[]> map) {
            Jedis jedis = JedisUtil.this.getJedis();
            String s;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                s = jedis.hmset(key, map);
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s =  null;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return s;
        }
    }

    public class SortSet {
        public SortSet() {
        }

        public long zadd(String key, double score, String member) {
            Jedis jedis = JedisUtil.this.getJedis();
            long s;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                s = jedis.zadd(key, score, member).longValue();
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return s;
        }

        public long zcard(String key) {
            Jedis sjedis = JedisUtil.this.getJedis();
            long len;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                len = sjedis.zcard(key).longValue();
                return len;
            }catch (Exception e){
                e.printStackTrace();
                len =  0;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return len;
        }

        public long zcount(String key, double min, double max) {
            Jedis sjedis = JedisUtil.this.getJedis();
            long len;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                len = sjedis.zcount(key, min, max).longValue();
                return len;
            }catch (Exception e){
                e.printStackTrace();
                len = 0L;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return len;
        }

        public long zlength(String key) {
            long len = 0L;
            Set set = this.zrange(key, 0, -1);
            len = (long)set.size();
            return len;
        }

        public double zincrby(String key, double score, String member) {
            Jedis jedis = JedisUtil.this.getJedis();
            double s;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                s = jedis.zincrby(key, score, member).doubleValue();
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s = 0.0D;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return s;
        }

        public Set<String> zrange(String key, int start, int end) {
            Jedis sjedis = JedisUtil.this.getJedis();
            Set set;
            try{
                sjedis.select(JRedisPoolConfig.dbNum);
                set = sjedis.zrange(key, (long)start, (long)end);
                return set;
            }catch (Exception e){
                e.printStackTrace();
                set =  null;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return set;
        }

        public Set<String> zrangeByScore(String key, double min, double max) {
            Jedis sjedis = JedisUtil.this.getJedis();
            Set set;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                set = sjedis.zrangeByScore(key, min, max);

                return set;
            }catch (Exception e){
                e.printStackTrace();
                set = null;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return set;
        }

        public long zrank(String key, String member) {
            Jedis sjedis = JedisUtil.this.getJedis();
            long index;
            try{
                sjedis.select(JRedisPoolConfig.dbNum);
                index = sjedis.zrank(key, member).longValue();
                return index;
            }catch (Exception e){
                e.printStackTrace();
                index = 0L;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return index;
        }

        public long zrevrank(String key, String member) {
            Jedis sjedis = JedisUtil.this.getJedis();
            long index;
            try{
                sjedis.select(JRedisPoolConfig.dbNum);
                index = sjedis.zrevrank(key, member).longValue();
                return index;
            }catch (Exception e){
                e.printStackTrace();
                index = 0L;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return index;
        }

        public long zrem(String key, String member) {
            Jedis jedis = JedisUtil.this.getJedis();
            long s;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                s = jedis.zrem(key, new String[]{member}).longValue();
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return s;
        }

        public long zrem(String key) {
            Jedis jedis = JedisUtil.this.getJedis();
            long s;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                s = jedis.del(key).longValue();
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return s;
        }

        public long zremrangeByRank(String key, int start, int end) {
            Jedis jedis = JedisUtil.this.getJedis();
            long s;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                s = jedis.zremrangeByRank(key, (long)start, (long)end).longValue();
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return s;
        }

        public long zremrangeByScore(String key, double min, double max) {
            Jedis jedis = JedisUtil.this.getJedis();
            long s;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                s = jedis.zremrangeByScore(key, min, max).longValue();
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return s;
        }

        public Set<String> zrevrange(String key, int start, int end) {
            Jedis sjedis = JedisUtil.this.getJedis();
            Set set;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                set = sjedis.zrevrange(key, (long)start, (long)end);
                return set;
            }catch (Exception e){
                e.printStackTrace();
                set = null;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return set;
        }

        public double zscore(String key, String memebr) {
            Jedis sjedis = JedisUtil.this.getJedis();
            Double score;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                score = sjedis.zscore(key, memebr);
                return score != null?score.doubleValue():0.0D;
            }catch (Exception e){
                e.printStackTrace();
                score = 0.0D;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return score;
        }
    }

    public class Sets {
        public Sets() {
        }

        public long sadd(String key, String member) {
            Jedis jedis = JedisUtil.this.getJedis();
            long s;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                s = jedis.sadd(key, new String[]{member}).longValue();
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return s;
        }

        public long sadd(byte[] key, byte[] member) {
            Jedis jedis = JedisUtil.this.getJedis();
            long s;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                s = jedis.sadd(key, new byte[][]{member}).longValue();
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return s;
        }

        public long scard(String key) {
            Jedis sjedis = JedisUtil.this.getJedis();
            long len;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                len = sjedis.scard(key).longValue();
                return len;
            }catch (Exception e){
                e.printStackTrace();
                len = 0L;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return len;
        }

        public Set<String> sdiff(String... keys) {
            Jedis jedis = JedisUtil.this.getJedis();
            Set set;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                set = jedis.sdiff(keys);
                return set;
            }catch (Exception e){
                e.printStackTrace();
                set = null;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return set;
        }

        public long sdiffstore(String newkey, String... keys) {
            Jedis jedis = JedisUtil.this.getJedis();
            long s;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                s = jedis.sdiffstore(newkey, keys).longValue();
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return s;
        }

        public Set<String> sinter(String... keys) {
            Jedis jedis = JedisUtil.this.getJedis();
            Set set;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                set = jedis.sinter(keys);
                return set;
            }catch (Exception e){
                e.printStackTrace();
                set = null;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return set;
        }

        public long sinterstore(String newkey, String... keys) {
            Jedis jedis = JedisUtil.this.getJedis();
            long s;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                s = jedis.sinterstore(newkey, keys).longValue();
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return s;
        }

        public boolean sismember(String key, String member) {
            Jedis sjedis = JedisUtil.this.getJedis();
            boolean s;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                s = sjedis.sismember(key, member).booleanValue();
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s = false;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return s;
        }

        public Set<String> smembers(String key) {
            Jedis sjedis = JedisUtil.this.getJedis();
            Set set;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                set = sjedis.smembers(key);
                return set;
            }catch (Exception e){
                e.printStackTrace();
                set = null;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return set;
        }

        public Set<byte[]> smembers(byte[] key) {
            Jedis sjedis = JedisUtil.this.getJedis();
            Set set;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                set = sjedis.smembers(key);
                return set;
            }catch (Exception e){
                e.printStackTrace();
                set = null;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return set;
        }

        public long smove(String srckey, String dstkey, String member) {
            Jedis jedis = JedisUtil.this.getJedis();
            long s;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                s = jedis.smove(srckey, dstkey, member).longValue();
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return s;
        }

        public String spop(String key) {
            Jedis jedis = JedisUtil.this.getJedis();
            String s;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                s = jedis.spop(key);
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s = null;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return s;
        }

        public long srem(String key, String member) {
            Jedis jedis = JedisUtil.this.getJedis();
            long s;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                s = jedis.srem(key, new String[]{member}).longValue();
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return s;
        }

        public Set<String> sunion(String... keys) {
            Jedis jedis = JedisUtil.this.getJedis();
            Set set;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                set = jedis.sunion(keys);
                return set;
            }catch (Exception e){
                e.printStackTrace();
                set = null;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return set;
        }

        public long sunionstore(String newkey, String... keys) {
            Jedis jedis = JedisUtil.this.getJedis();
            long s;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                s = jedis.sunionstore(newkey, keys).longValue();
                return s;
            }catch (Exception e){
                e.printStackTrace();
                s = 0;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return s;
        }
    }

    public class Keys {
        public Keys() {
        }

        public String flushAll() {
            Jedis jedis = JedisUtil.this.getJedis();
            String stata;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                stata = jedis.flushAll();
                return stata;
            }catch (Exception e){
                e.printStackTrace();
                stata = null;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return stata;
        }

        public String rename(String oldkey, String newkey) {
            return this.rename(SafeEncoder.encode(oldkey), SafeEncoder.encode(newkey));
        }

        public long renamenx(String oldkey, String newkey) {
            Jedis jedis = JedisUtil.this.getJedis();
            long status;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                status = jedis.renamenx(oldkey, newkey).longValue();
                return status;
            }catch (Exception e){
                e.printStackTrace();
                status = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return status;
        }

        public String rename(byte[] oldkey, byte[] newkey) {
            Jedis jedis = JedisUtil.this.getJedis();
            String status;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                status = jedis.rename(oldkey, newkey);
                return status;
            }catch (Exception e){
                e.printStackTrace();
                status = null;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return status;
        }

        public long expired(String key, int seconds) {
            Jedis jedis = JedisUtil.this.getJedis();
            long count;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                count = jedis.expire(key, seconds).longValue();

                return count;
            }catch (Exception e){
                e.printStackTrace();
                count = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return count;
        }

        public long expireAt(String key, long timestamp) {
            Jedis jedis = JedisUtil.this.getJedis();
            long count;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                count = jedis.expireAt(key, timestamp).longValue();
                return count;
            }catch (Exception e){
                e.printStackTrace();
                count = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return count;
        }

        public long ttl(String key) {
            Jedis sjedis = JedisUtil.this.getJedis();
            long len;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                len = sjedis.ttl(key).longValue();
                return len;
            }catch (Exception e){
                e.printStackTrace();
                len = 0L;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return len;
        }

        public long persist(String key) {
            Jedis jedis = JedisUtil.this.getJedis();
            long count;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                count = jedis.persist(key).longValue();
                return count;
            }catch (Exception e){
                e.printStackTrace();
                count = 0L;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return count;
        }

        public long del(String... keys) {
            Jedis jedis = JedisUtil.this.getJedis();
            long count;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                count = jedis.del(keys).longValue();
                return count;
            }catch (Exception e){
                e.printStackTrace();
                count =  0;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return count;
        }

        public long del(byte[]... keys) {
            Jedis jedis = JedisUtil.this.getJedis();
            long count;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                count = jedis.del(keys).longValue();
                return count;
            }catch (Exception e){
                e.printStackTrace();
                count =  0;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return count;
        }

        public boolean exists(String key) {
            Jedis sjedis = JedisUtil.this.getJedis();
            boolean exis;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                exis = sjedis.exists(key).booleanValue();
                return exis;
            }catch (Exception e){
                e.printStackTrace();
                exis = false;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return exis;
        }

        public List<String> sort(String key) {
            Jedis sjedis = JedisUtil.this.getJedis();
            List list;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
               list = sjedis.sort(key);
                return list;
            }catch (Exception e){
                e.printStackTrace();
                list = null;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return list;
        }

        public List<String> sort(String key, SortingParams parame) {
            Jedis sjedis = JedisUtil.this.getJedis();
            List list;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                list = sjedis.sort(key, parame);
                return list;
            }catch (Exception e){
                e.printStackTrace();
                list = null;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return list;
        }

        public String type(String key) {
            Jedis sjedis = JedisUtil.this.getJedis();
            String type;
            try {
                sjedis.select(JRedisPoolConfig.dbNum);
                type = sjedis.type(key);
                return type;
            }catch (Exception e){
                e.printStackTrace();
                type = null;
            }finally {
                JedisUtil.this.returnJedis(sjedis);
            }
            return type;
        }

        public Set<String> keys(String pattern) {
            Jedis jedis = JedisUtil.this.getJedis();
            Set set;
            try {
                jedis.select(JRedisPoolConfig.dbNum);
                set = jedis.keys(pattern);
                return set;
            }catch (Exception e){
                e.printStackTrace();
                set = null;
            }finally {
                JedisUtil.this.returnJedis(jedis);
            }
            return set;
        }
    }
}
