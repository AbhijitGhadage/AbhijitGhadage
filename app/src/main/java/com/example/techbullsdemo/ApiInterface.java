package com.example.techbullsdemo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("http://www.omdbapi.com/")
    Call<MovieResponse> getMoviesList(@Query("s")String batman,
                                            @Query("apikey")String key);
}
