package com.example.lazismuapp.Api;

import android.content.Context;

import com.example.lazismuapp.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            String baseUrl = context.getString(R.string.BASE_URL);
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl) // Ganti dengan base URL API Anda
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

