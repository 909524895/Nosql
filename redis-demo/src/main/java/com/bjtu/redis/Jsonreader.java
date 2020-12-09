package com.bjtu.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Jsonreader {
    private String path;
    private String JsonStr;
    private JSONObject jobj;

    public Jsonreader(String path) throws IOException {
        File jsonFile = new File(path);
        FileReader fileReader = new FileReader(jsonFile);
        Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
        int ch = 0;
        StringBuffer sb = new StringBuffer();
        while((ch = reader.read()) != -1) {
            sb.append((char) ch);
        }
        fileReader.close();
        reader.close();
        this.JsonStr = sb.toString();
        this.jobj = JSON.parseObject(this.JsonStr);
        this.path=path;
    }

    public String getStr(){
        return this.JsonStr;
    }

    public JSONObject getJobj(){
        return this.jobj;
    }

    public List<User> getUsers() throws Exception {
        ArrayList<User> Users = new ArrayList<User>();
        Iterator iterator = this.jobj.keySet().iterator();
        while(iterator.hasNext()) {
            User user1 = new User();
            String key = (String) iterator.next();
            JSONObject helper = this.jobj.getJSONObject(key);
            user1.setUserid(key);
            user1.setName(helper.getString("name"));
            user1.setLike_action(helper.getString("Action"));
            Users.add(user1);
        }

        return Users;
    }

    public List<Movie> getMovies() {
        ArrayList<Movie> Movies = new ArrayList<Movie>();
        Iterator iterator = this.jobj.keySet().iterator();
        while(iterator.hasNext()) {
            Movie movie1 = new Movie();
            String key = (String) iterator.next();
            JSONObject helper = this.jobj.getJSONObject(key);
            movie1.setMovieId(key);
            movie1.setLike_counter(helper.getString("like_counter"));
            movie1.setWatch_counter(helper.getString("watch_counter"));
            movie1.setHit_counter(helper.getString("hit_counter"));
            Movies.add(movie1);
        }

        return Movies;
    }
}
