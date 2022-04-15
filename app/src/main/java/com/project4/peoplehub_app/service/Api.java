package com.project4.peoplehub_app.service;

import com.project4.peoplehub_app.model.UserLogin;
import com.project4.peoplehub_app.pojos.AddFriendResponse;
import com.project4.peoplehub_app.pojos.TokenLogin;
import com.project4.peoplehub_app.pojos.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {


    @POST("users/auth/login")
    Call<TokenLogin> login(@Body UserLogin userLogin);

    @GET("users/info")
    Call<UserResponse> getUserInfo(@Header(value = "Authorization") String accessToken);

    @POST("friends/{followerName}")
    Call<AddFriendResponse> sendAddFriend(@Header(value = "Authorization") String accessToken,String followerName);
}
