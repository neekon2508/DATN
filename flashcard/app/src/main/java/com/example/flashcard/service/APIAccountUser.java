package com.example.flashcard.service;

import com.example.flashcard.dto.AccountUserDTO;
import com.example.flashcard.dto.CardSetDTO;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIAccountUser {
    @POST("account_user/login")
    Call<JsonObject> login(@Body AccountUserDTO loginRequest);

    @GET("account_user/get_all")
    Call<JsonObject> get_all();

    @GET("account_user/getById/{id}")
    Call<AccountUserDTO> getById(@Path("id") Long id);

    @GET("account_user/getByUsername/{username}")
    Call<List<AccountUserDTO>> getByUserName(@Path("username") String username);

    @POST("account_user/create")
    Call<AccountUserDTO> createAccountUser(@Body AccountUserDTO accountUserDTO);

    @POST("account_user/create_card_set/{id}")
    Call<AccountUserDTO> createCardSet(@Path("id") Long id, @Body CardSetDTO cardSetDTO);

    @PATCH("account_user/update/{id}")
    Call<AccountUserDTO> update(@Path("id") Long id, @Body AccountUserDTO accountUserDTO);

    @DELETE("delete/{id}")
    Call<Void> delete(@Path("id") Long id);

}
