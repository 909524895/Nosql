package com.bjtu.redis;

public class User {
    private String userid;
    private String name;
    private String like_action;

    public User(){}

    public User(String userid,String name,String like_action) throws Exception {
        Encapsulation test = new Encapsulation();
        test.hash_user(userid,name,like_action);
    }

    public String getUserid() {
        return userid;
    }

    public String getName() {
        return name;
    }

    public String getLike_action() {
        return like_action;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLike_action(String like_action) {
        this.like_action = like_action;
    }

    public void do_action(String like_action) throws Exception {
        this.like_action = like_action;
        Encapsulation test = new Encapsulation();
        test.hash_add(userid, "action", like_action);

        String[] s = getLike_action().split(",");
        System.out.println("用户" + getName());
        for (int i = 0; i < s.length; i++) {
            String[] user_action = s[i].split(" ");
            String action = user_action[0];
            String movie = user_action[1];
            String time = user_action[2];
            System.out.println(action+" "+movie);
            if (action.equals("watch")) {
                System.out.println("观看了" + movie);
                test.incr_watch(movie, 1, time);
            } else {
                System.out.println("点赞了" + movie);
                test.incr_like(movie, 1, time);
            }

        }


    }

}
