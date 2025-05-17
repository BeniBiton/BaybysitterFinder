package com.example.babysitterfinder.API;

import com.example.babysitterfinder.models.TranslationRequest;
import com.example.babysitterfinder.models.TranslationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TranslationApiService {
    @Headers("Content-Type: application/json")
    @POST("language/translate/v2")
    Call<TranslationResponse> translateText(@Body TranslationRequest request, @Query("key") String apiKey);
}
