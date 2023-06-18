package com.example.waregarage.request;

public class UtilsApi {
    public static final String BASE_URL_API = "http://10.0.2.2:1325/";

    public static RetrofitInterface getApiService() {
        return RetroFitClient.getClient(BASE_URL_API).create(RetrofitInterface.class);
    }
}
