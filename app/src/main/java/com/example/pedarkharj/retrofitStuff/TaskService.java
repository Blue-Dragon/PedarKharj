package com.example.pedarkharj.retrofitStuff;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface TaskService {

    @GET("posts")
    Call<List<Post>> getPoosts();
}
