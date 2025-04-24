package com.example.flashcard.service;

import com.example.flashcard.dto.AccountUserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIAccountUser {
    @POST("account_user/login")
    Call<String> login(@Body AccountUserDTO loginRequest);
}
