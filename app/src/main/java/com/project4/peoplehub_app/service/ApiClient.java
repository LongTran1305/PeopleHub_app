package com.project4.peoplehub_app.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.project4.peoplehub_app.model.User;
import com.project4.peoplehub_app.pojos.TokenLogin;

import java.net.HttpCookie;
import java.util.List;

import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class ApiClient {

    private static final String BASE_URL = "http://cc220e9dfab7.sn.mynetname.net:8080/";
    private static ApiClient mInstance;
    private Retrofit retrofit;


    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();


    private ApiClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static Retrofit getRetrofit(){
        Gson gson = new GsonBuilder().create();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        return retrofit;
    }


    public static synchronized ApiClient getInstance(){
        if (mInstance == null){
            mInstance = new ApiClient();
        }
        return mInstance;
    }


    public static Api getApi(){
        Api api = getRetrofit().create(Api.class);
//        return retrofit.create(Api.class);
        return api;

    }
}
