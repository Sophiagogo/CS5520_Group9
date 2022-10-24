package edu.northeastern.cs5520_group9.webService;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {

    @GET("search?limit=10")
    Call<List<Image>> getImages();
}
