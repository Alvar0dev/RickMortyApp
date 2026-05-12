package com.example.rickmortyapp.api;
import java.util.List;
import com.example.rickmortyapp.modelo.Elemento;
import com.google.gson.annotations.SerializedName;
public class ElementoResponse {
    // El nombre "elementos" tiene que ser EXACTO al de la API
    @SerializedName("results")
    private List<Elemento> elementos;
    public List<Elemento> getelementos() {
        return elementos;
    }
}
