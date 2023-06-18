package com.example.waregarage.request;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitClient {
    private static Retrofit retrofit = null;

    /**
     * Gets the Retrofit client.
     * @param baseUrl
     * @return Retrofit
     *  @author Muhammad Rizky Utomo
     *  @version 11/12/2022
     */
    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().
                    baseUrl(baseUrl).
                    addConverterFactory(GsonConverterFactory.create()).
                    build();
        }
        return retrofit;
    }
}
