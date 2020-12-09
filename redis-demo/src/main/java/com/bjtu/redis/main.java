package com.bjtu.redis;

import java.util.List;

public class main {
    public void main() throws Exception {

        System.out.println("加载原始数据");
        //获取json文件
        Jsonreader reader_user = new Jsonreader(Jsonreader.class.getClassLoader().getResource("user.json").getPath());
        Jsonreader reader_movie = new Jsonreader(Jsonreader.class.getClassLoader().getResource("movie.json").getPath());
        Encapsulation test = new Encapsulation();

        //对读取json文件内容操作,对json内每个用户信息转为user实体，并返回文件中的所有用户的数据
        List<User> users = reader_user.getUsers();
        List<Movie> movies = reader_movie.getMovies();

        test.load(users,movies);

        User user = new User("18301023","王伟安","watch movie1 1207");


        test.FREQ(1201,1215);


    }
}
