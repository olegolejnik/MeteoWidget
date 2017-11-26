package com.example.olego.meteowidget10;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by olego on 20.11.2017.
 */

public interface APIInterface {
    @GET("/")
    Call<MultipleResource> doGetListResources();
}
