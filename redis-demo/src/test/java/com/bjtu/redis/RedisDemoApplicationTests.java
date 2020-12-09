package com.bjtu.redis;

import com.bjtu.redis.jedis.JedisDemo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisDemoApplicationTests {

    @Test
    public void contextLoads() {
        JedisDemo demo = new JedisDemo();
        demo.flushAll();
        demo.create_Key("wwa","3");
        demo.list_lpush("haha","7");
        demo.list_lpush("haha","6");
        demo.set_expire("haha",20);
        demo.FREQ();
        demo.create_Key("hehe","1");
        demo.incr("hehe");
        System.out.println(demo.NUM());




    }

}
