package com.example.babysitterfinder.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitTranslateClient {
    private static final String BASE_URL = "https://translation.googleapis.com/";
    private static Retrofit retrofit = null;

    public static TranslationApiService getTranslationService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(TranslationApiService.class);
    }
}
