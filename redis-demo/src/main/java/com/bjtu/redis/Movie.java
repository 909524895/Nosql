package com.bjtu.redis;

public class Movie {
    private String MovieId;
    private String like_counter;
    private String watch_counter;
    private String hit_counter;

    public Movie(){}

    public Movie(String MovieId,String like_counter,String watch_counter,String hit_counter)
    {
        this.MovieId = MovieId;
        this.like_counter = like_counter;
        this.watch_counter = watch_counter;
        this.hit_counter =hit_counter;
    }

    public String getLike_counter() {
        return like_counter;
    }

    public String getHit_counter() {
        return hit_counter;
    }

    public void setHit_counter(String hit_counter) {
        this.hit_counter = hit_counter;
    }

    public String getMovieId() {
        return MovieId;
    }

    public String getWatch_counter() {
        return watch_counter;
    }

    public void setLike_counter(String like_counter) {
        this.like_counter = like_counter;
    }

    public void setMovieId(String movieId) {
        MovieId = movieId;
    }

    public void setWatch_counter(String watch_counter) {
        this.watch_counter = watch_counter;
    }
}
