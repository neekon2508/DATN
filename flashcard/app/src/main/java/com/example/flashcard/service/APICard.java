package com.example.flashcard.service;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APICard {

    @Multipart
    @POST("card/upload")
    Call<JsonObject> upload(@Part MultipartBody.Part file);

}
