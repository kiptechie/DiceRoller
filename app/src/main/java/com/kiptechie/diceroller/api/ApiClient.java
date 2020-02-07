package com.kiptechie.diceroller.api;

import android.app.Application;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient extends Application {

    public static final String BASE_URL = "https://api.chucknorris.io/jokes/";
    private static Retrofit retrofit =  null;

    ApiInterface apiInterface;

    public ApiClient() {
        this.apiInterface = getClient().create(ApiInterface.class);
    }


    public static Retrofit getClient(){
        if(retrofit == null){
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.addInterceptor(chain -> {
                Request.Builder requestBuilder = chain.request().newBuilder();
                requestBuilder.header("Content-Type", "application/x-www-form-urlencoded");
                return chain.proceed(requestBuilder.build());
            });

//            --------OKHTTP LOGGING----------
//            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            httpClientBuilder.addInterceptor(interceptor).build();


            OkHttpClient httpClient = httpClientBuilder.build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
        }
        return retrofit;
    }

    public ApiInterface getApiInterface() {
        return apiInterface;
    }

}
