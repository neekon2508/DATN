package com.example.flashcard.service;

import com.example.flashcard.dto.CardDTO;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APICard {

    @Multipart
    @POST("card/upload")
    Call<JsonObject> upload(@Part MultipartBody.Part file);

    @GET("card/get/{id}")
    Call<CardDTO> getById(@Path("id") Long id);

    @POST("card/create")
    Call<CardDTO> create(@Body CardDTO cardDTO);

    @PATCH("card/update/{id}")
    Call<CardDTO> update(@Path("id") Long id, @Body CardDTO cardDTO);

    @DELETE("card/delete/{id}")
    Call<Void> delete(@Path("id") Long id);

}
