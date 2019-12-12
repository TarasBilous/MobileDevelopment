package com.example.mobiledevelopment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PanelService {
    @GET("panel")
    Call<List<Panel>> getServerPanels();

    @POST("panel")
    Call<Panel> createPanel(@Body Panel panel);
}