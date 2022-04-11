package com.example.ecomapplication.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiInterface {

    @GET()
    Call<ProductResponse> getProductList(@Url String url);

}
