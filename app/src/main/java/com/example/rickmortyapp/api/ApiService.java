package com.example.rickmortyapp.api;

import retrofit2.Call;
import retrofit2.http.GET;
public interface ApiService {
    // Aquí ponemos la parte final de la URL del examen
    @GET("character")
    Call<ElementoResponse> getElementos();
}