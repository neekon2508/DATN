package com.example.flashcard.service;

import com.example.flashcard.dto.CardDTO;
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

public interface APICardSet {

    @GET("card_set/getById/{id}")
    Call<CardSetDTO> getById(@Path("id") Long id);

    @GET("card_set/getByName/{name}")
    Call<List<CardSetDTO>> getByName(@Path("name") String name);

    @POST("card_set/create")
    Call<CardSetDTO> create(@Body CardSetDTO cardSetDTO);

    @POST("card_set/create_card/{id}")
    Call<CardSetDTO> createCard(@Path("id") Long id, @Body CardDTO cardDTO);

    @PATCH("card_set/update/{id}")
    Call<CardSetDTO> update(@Path("id") Long id, @Body CardSetDTO cardSetDTOc);

    @DELETE("card_set/delete/{id}")
    Call<Void> delete(@Path("id") Long id);
}
