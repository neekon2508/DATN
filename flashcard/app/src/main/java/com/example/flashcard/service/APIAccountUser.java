package com.example.flashcard.service;

import com.example.flashcard.dto.AccountUserDTO;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIAccountUser {
    @POST("account_user/login")
    Call<JsonObject> login(@Body AccountUserDTO loginRequest);

    @GET("account_user/get_all")
    Call<JsonObject> get_all();

    @GET("account_user/username")
    Call<AccountUserDTO> getByUsername(@Body String username);
}
