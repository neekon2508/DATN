package com.example.flashcard.service;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

public interface APICardSet {

    @GET("account_user/username")
    Call<JsonObject> getCardSetByUsername(@Body String username);
}
