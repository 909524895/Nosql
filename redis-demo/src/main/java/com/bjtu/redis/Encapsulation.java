package com.bjtu.redis;

import redis.clients.jedis.Jedis;

import java.util.*;

public class Encapsulation {

    public Jedis connection() throws Exception{
        Jedis jedis = JedisInstance.getInstance().getResource();
        return jedis;
    }


    public void load(List<User> users,List<Movie> movies) throws Exception {
        Jedis jedis = connection();
        jedis.flushAll();
        for(Movie m:movies){
            jedis.lpush("movie",m.getMovieId());
            hash_movie(m.getMovieId(),m.getLike_counter(),m.getWatch_counter(),m.getHit_counter());
            System.out.println(m.getMovieId()+",观看次数"+m.getWatch_counter()+",点赞次数"+m.getLike_counter()+",热度"+m.getHit_counter());
        }

        for(User u:users){
            hash_user(u.getUserid(),u.getName(),u.getLike_action());

        }

        rank();
        FREQ(1201,1215);

    }


    public void STR(String key_name,String key_value) throws Exception {
        Jedis jedis = connection();
        jedis.set(key_name,key_value);
        String val = jedis.get(key_name);
    }

    public void create_Key(String key_name,String key_value) throws Exception {
        Jedis jedis = connection();
        jedis.set(key_name,key_value);
        String val = jedis.get(key_name);
    }

    public void create_Key(String key_name,int ex,String key_value) throws Exception {
        Jedis jedis = connection();
        jedis.setex(key_name, ex,key_value);
        String val = jedis.get(key_name);
    }

    public void set_expire(String key_name,int ex) throws Exception {
        Jedis jedis = connection();
        jedis.expire(key_name,ex);
    }

    public void delete_Key(String key_name) throws Exception {
        Jedis jedis = connection();
        jedis.del(key_name);
    }

    public void list_lpush(String key_name,String key_value) throws Exception {
        Jedis jedis = connection();
        jedis.lpush(key_name,key_value);
    }

    public void list_lpop(String key_name) throws Exception {
        Jedis jedis = connection();
        String pop = jedis.lpop(key_name);
    }


    public void set_add(String set,String member) throws Exception {
        Jedis jedis = connection();
        jedis.sadd(set,member);
    }

    public void set_rem(String set,String member) throws Exception {
        Jedis jedis = connection();
        jedis.srem(set,member);
    }

    public void zset_add(String set, String score, String member) throws Exception {
        Jedis jedis = connection();
        jedis.zadd(set, Double.parseDouble(score),member);
    }

    public void zset_rem(String set,String member) throws Exception {
        Jedis jedis = connection();
        jedis.zrem(set,member);
    }

    public void hash_add(String key,String field,String value) throws Exception {
        Jedis jedis = connection();
        jedis.hset(key,field,value);
    }

    public void hash_user(String userid,String name) throws Exception {
        Jedis jedis = connection();
        HashMap<String,String> map = new HashMap<>();
        map.put("name",name);
        jedis.hmset(userid,map);
    }
    public void hash_user(String userid,String name,String action) throws Exception {
        Jedis jedis = connection();
        HashMap<String,String> map = new HashMap<>();
        map.put("name",name);
        map.put("action",action);
        jedis.hmset(userid,map);

        String[] s = action.split(",");
        System.out.println("用户"+name);
        for(int i = 0; i<s.length; i++) {
            String[] user_action = s[i].split(" ");
            String action1 = user_action[0];
            String movie = user_action[1];
            String time = user_action[2];
            if(action1.equals("watch")) {
                System.out.println("观看了"+movie);
                incr_watch(movie,1,time);
            }
            else{
                System.out.println("点赞了"+movie);
                incr_like(movie,1,time);
            }
        }
    }

    public void hash_movie(String movieid,String like_counter,String watch_counter,String hit_counter) throws Exception {
        Jedis jedis = connection();
        HashMap<String,String> map = new HashMap<>();
        map.put("like_counter",like_counter);
        map.put("watch_counter",watch_counter);
        map.put("hit_counter",hit_counter);
        jedis.hmset(movieid,map);
    }

    public void incr_watch(String key,int value,String time) throws Exception {
        Jedis jedis = connection();
        jedis.lpush(key+"_watch",time);
        jedis.hincrBy(key,"watch_counter",value);
        jedis.hincrBy(key,"hit_counter",value);
    }

    public void incr_like(String key,int value,String time) throws Exception {
        Jedis jedis = connection();
        jedis.lpush(key+"_like",time);
        jedis.hincrBy(key,"hit_counter",2*value);
        jedis.hincrBy(key,"like_counter",value);
    }

    public void rank() throws Exception {
        Jedis jedis = connection();
        //提取所有的movie
        List<String> list = jedis.lrange("movie",0,-1);
        for (int i = 0; i < list.size(); i++) {
            zset_add("movie_rank",jedis.hget(list.get(i),"hit_counter"),list.get(i));
        }

        Set<String> set = jedis.zrevrange("movie_rank",0,-1);
        Iterator<String> it = set.iterator();
        System.out.println("视频热度排行榜");
        System.out.println("------------------------");
        int i = 1;
        while(it.hasNext()){
            String temp = it.next();
            System.out.println("No."+(i++)+" "+temp+" 热度："+jedis.hget(temp,"hit_counter"));
        }
    }
    String trick = "99";
    public void FREQ(int min,int max) throws Exception {
        Jedis jedis = connection();
        //提取所有的movie
        List<String> list = jedis.lrange("movie",0,-1);

        //提取movie_watch中的数据到zset
        for (int i = 0; i < list.size(); i++) {
            //提取所有的movie[]_watch
            List<String> list_watch = jedis.lrange(list.get(i)+"_watch",0,-1);
            for (int j = 0; j < list_watch.size(); j++) {
                jedis.zadd(list.get(i) + "_watched", Double.parseDouble(list_watch.get(j)),trick);
                trick += "9";
            }
            //提取所有的movie[]_like
            List<String> list_like = jedis.lrange(list.get(i)+"_like",0,-1);
            for (int j = 0; j < list_like.size(); j++) {
                jedis.zadd(list.get(i) + "_liked", Double.parseDouble(list_watch.get(j)),trick);
                trick += "9";
            }
        }
        System.out.println("在"+min+"到"+max+"期间内");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i)+"被观看了"+jedis.zcount(list.get(i)+"_watched",min,max)+"次，");
            System.out.print("被点赞了"+jedis.zcount(list.get(i)+"_liked",min,max)+"次,");
            System.out.println("热度上升"+(jedis.zcount(list.get(i)+"_watched",min,max)+2*jedis.zcount(list.get(i)+"_liked",min,max)));
        }
        jedis.del("movie1_watched","movie2_watched","movie3_watched","movie4_watched","movie1_liked","movie2_liked","movie3_liked","movie4_liked");
    }
}
