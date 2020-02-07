package com.kiptechie.diceroller.api;

import com.kiptechie.diceroller.api.response.Categories;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("random")
    Call<Categories> getRandomJokes();

}
