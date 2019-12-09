package com.example.mobiledevelopment;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationEx extends Application {

    private static final String BASE_URL = "http://cloud-tech-lab2.appspot.com/";
    private PanelService mRestService;
    private FirebaseAuth mAuth;

    public void onCreate() {
        super.onCreate();
        mAuth = FirebaseAuth.getInstance();
        mRestService = initRestService();
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    public PanelService getRestService() {
        return mRestService;
    }

    private PanelService initRestService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(PanelService.class);
    }
}