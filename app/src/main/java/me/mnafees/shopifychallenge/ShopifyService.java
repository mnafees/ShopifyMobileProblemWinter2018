package me.mnafees.shopifychallenge;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ShopifyService {

    @GET("/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6")
    Call<JsonObject> getJSON();

}
