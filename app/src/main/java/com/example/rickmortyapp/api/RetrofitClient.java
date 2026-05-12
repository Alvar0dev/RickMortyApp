package com.example.rickmortyapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClient {
    // La URL BASE (hasta la última barra antes del archivo)
    private static final String BASE_URL = "https://rickandmortyapi.com/api/";
    private static Retrofit retrofit = null;
    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)

                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
// Aquí es donde "unimos" el motor con la interfaz
        return retrofit.create(ApiService.class);
    }
}
