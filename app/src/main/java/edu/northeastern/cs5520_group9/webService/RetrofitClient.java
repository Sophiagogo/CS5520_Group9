package edu.northeastern.cs5520_group9.webService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private  static final String BASE_URL = "https://api.thecatapi.com/v1/images/";                          // Base URL
    private static RetrofitClient mInstance;
    private final Retrofit retrofit;

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public API getAPI() {
        return retrofit.create(API.class);
    }
}
