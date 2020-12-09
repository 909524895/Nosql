package com.bjtu.redis.jedis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class JedisDemo {

    public Jedis jedis;

    public JedisDemo()
    {
        jedis = new Jedis("127.0.0.1", 6379);
        System.out.println("服务正在运行: " + jedis.ping());
    }
    /**
     * 基本使用
     */
    @Test
    public void basicUse() {
        jedis.setex("namewithttl", 20,"lizai");
        String val = jedis.get("namewithttl");
        System.out.println(val);
        jedis.close();
    }

    /**
     * 使用连接池
     */
    @Test
    public void poolUse() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(30);
        config.setMaxIdle(10);

        JedisPool jedisPool = new JedisPool(config, "127.0.0.1", 6379);
        Jedis jedis = jedisPool.getResource();
        jedis.set("date", "5/18");
        String val = jedis.get("name");
        System.out.println(val);
        jedis.close();
        jedisPool.close();
    }

    /**
     * 使用数据结构List
     */
    @Test
    public void RedisList() {
        //连接本地的 Redis 服务
//        Jedis jedis = new Jedis("localhost");
        //存储数据到列表中
        jedis.lpush("site-list", "Runoob");
        jedis.lpush("site-list", "Google");
        jedis.lpush("site-list", "Taobao");
        // 获取存储的数据并输出
        List<String> list = jedis.lrange("site-list", 0, 2);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

    /**
     * 使用命令Keys
     */
    @Test
    public void RedisKey() {
        //连接本地的 Redis 服务
//        Jedis jedis = new Jedis("localhost");
        // 获取数据并输出
        Set<String> keys = jedis.keys("*");
        Iterator<String> it=keys.iterator() ;
        while(it.hasNext()){
            String key = it.next();
            System.out.println(key);
        }
    }

    /**
     * 使用命令Set
     */
    @Test
    public void RedisSet() {
        //连接本地的 Redis 服务
//        Jedis jedis = new Jedis("localhost");
        //存储数据到集合中
        jedis.sadd("set","hello");
        jedis.sadd("set","world");
        // 获取存储的数据并输出
        Set<String> set = jedis.smembers("set");
        Iterator<String> it = set.iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }
    }

    /**
     * 使用命令Hash
     */
    @Test
    public void RedisHash() {
        //连接本地的 Redis 服务
//        Jedis jedis = new Jedis("localhost");
        //存储数据到hash中
        jedis.hset("wwa","name","wwa");
        jedis.hset("wwa","age","20");
        jedis.hset("wwa","sex","male");
        // 获取存储的数据并输出
        System.out.println(jedis.hmget("wwa","name","age","sex"));
    }

    /**
     * 使用命令Zset
     */
    @Test
    public void RedisZset() {
        //连接本地的 Redis 服务
//        Jedis jedis = new Jedis("localhost");
        //存储数据到Zset中
        jedis.zadd("wwa_grade",90,"math");
        jedis.zadd("wwa_grade",80,"english");
        jedis.zadd("wwa_grade",100,"computer");
        // 获取存储的数据并输出
        System.out.println(jedis.zrange("wwa_grade",0,-1));
    }

    /**
     * 统计所有key的生命周期
     */
    @Test
    public void FREQ() {
        //连接本地的 Redis 服务
//        Jedis jedis = new Jedis("localhost");
        //获取所有key
        Set<String> keys = jedis.keys("*");
        Iterator<String> it=keys.iterator() ;
        //将key
        while(it.hasNext()){
            String key = it.next();
            zset_add("freq",jedis.ttl(key),key);
        }
//        System.out.println(jedis.zrange("freq",0,-1));

        // 获取存储的数据并输出
        Set<String> set = jedis.zrange("freq",0,-1);
        Iterator<String> it2 = set.iterator();
//        jedis.zrem("freq");
        while(it2.hasNext()){
            String temp = it2.next();
            System.out.println(temp+" "+jedis.ttl(temp));
        }
        delete_Key("freq");
    }

    /**
     * 使用命令flushall
     */
    @Test
    public void flushAll() {
        jedis.flushAll();
    }

    /**
     * 创建string类
     */
    public void STR(String key_name,String key_value) {

        jedis.set(key_name,key_value);
        String val = jedis.get(key_name);
        System.out.println("生成string:"+val);
    }


    /**
     * 创建Keys
     */
    public void create_Key(String key_name,String key_value) {

        jedis.set(key_name,key_value);
        String val = jedis.get(key_name);
        System.out.println("生成key:"+val);
    }

    /**
     * 创建Keys有生命周期
     */
    public void create_Key(String key_name,int ex,String key_value) {

        jedis.setex(key_name, ex,key_value);
        String val = jedis.get(key_name);
        System.out.println("生成key:"+val);
    }

    /**
     * 给key设置过期时间
     */
    public void set_expire(String key_name,int ex) {

        jedis.expire(key_name,ex);
        System.out.println("设置"+key_name+"的过期时间为:"+ex);
    }

    /**
     * 删除Keys
     */
    public void delete_Key(String key_name) {

        jedis.del(key_name);
        System.out.println("删除key:"+key_name);
    }

    /**
     * 展示所有key
     */
    public void show_Key() {
        Set<String> keys = jedis.keys("*");
        Iterator<String> it=keys.iterator() ;
        while(it.hasNext()){
            String key = it.next();
            System.out.println(key);
        }
    }


    /**
     * list左插入
     */
    public void list_lpush(String key_name,String key_value) {

        jedis.lpush(key_name,key_value);
        System.out.println("列表"+key_name+"头部添加元素:"+key_value);
    }

    /**
     * list右插入
     */
    public void list_rpush(String key_name,String key_value) {

        jedis.rpush(key_name,key_value);
        System.out.println("列表"+key_name+"尾部添加元素:"+key_value);
    }

    /**
     * list左弹出
     */
    public void list_lpop(String key_name) {
        String pop = jedis.lpop(key_name);
        System.out.println("列表"+key_name+"弹出元素:"+pop);
    }

    /**
     * list右弹出
     */
    public void list_rpop(String key_name) {
        String pop = jedis.rpop(key_name);
        System.out.println("列表"+key_name+"弹出元素:"+pop);
    }

    /**
     * 展示列表
     */
    public void show_List(String List) {
        System.out.println("展示列表"+List+":");
        List<String> list = jedis.lrange(List, 0, -1);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

    /**
     * Set插入元素
     */
    public void set_add(String set,String member) {
        jedis.sadd(set,member);
        System.out.println("集合"+set+"添加元素:"+member);
    }

    /**
     * Set移除元素
     */
    public void set_rem(String set,String member) {
        jedis.srem(set,member);
        System.out.println("集合"+set+"移除元素:"+member);
    }

    /**
     * Zset插入元素
     */
    public void zset_add(String set,Long score,String member) {
        jedis.zadd(set,score,member);
        System.out.println("有序集合"+set+"添加元素:"+member);
    }

    /**
     * Zset移除元素
     */
    public void zset_rem(String set,String member) {
        jedis.zrem(set,member);
        System.out.println("有序集合"+set+"移除元素:"+member);
    }

    /**
     * NUM
     */
    public Long NUM() {
        Long l;
        l = 0L;
        Set<String> keys = jedis.keys("*");
        Iterator<String> it=keys.iterator();
        while(it.hasNext()){
            it.next();
            l = l+1;
        }
        return l;
    }

    /**
     * incr
     */
    public void incr(String key_name) {
        jedis.incr(key_name);
    }
}
