package com.example.rickmortyapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rickmortyapp.BBDD.ElementoDAO;
import com.example.rickmortyapp.adapter.ElementoAdapter;
import com.example.rickmortyapp.api.ElementoResponse;
import com.example.rickmortyapp.api.RetrofitClient;
import com.example.rickmortyapp.modelo.Elemento;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ElementoAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ElementoAdapter adaptador;
    private ArrayList<Elemento> listaElementos = new ArrayList<>();
    private ElementoDAO dao;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dao = new ElementoDAO(this);
        recyclerView = findViewById(R.id.rv);
        fab = findViewById(R.id.fab);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        adaptador = new ElementoAdapter(listaElementos, this);
        recyclerView.setAdapter(adaptador);
        
        cargarDatosDeInternet();

        fab.setOnClickListener(view -> {
            Toast.makeText(this, "Botón pulsado", Toast.LENGTH_SHORT).show();
        });
    }

    private void cargarDatosDeInternet() {
        RetrofitClient.getApiService().getElementos().enqueue(new Callback<ElementoResponse>() {
            @Override
            public void onResponse(Call<ElementoResponse> call, Response<ElementoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Elemento> descargados = response.body().getelementos();
                    adaptador.actualizarlista(descargados);
                }
            }

            @Override
            public void onFailure(Call<ElementoResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onFavoritoClick(Elemento elemento) {
        long id = dao.insertar(elemento);
        if (id != -1) {
            Toast.makeText(this, "Guardado correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ya existe en la base de datos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(Elemento elemento) {
        // 1. Creamos la intención hacia la pantalla de detalle
        Intent intent = new Intent(this, DetalleActivity.class);

// 2. Creamos un Bundle (el "contenedor" o paquete)
        Bundle bundle = new Bundle();

// 3. Metemos los datos dentro del paquete usando claves
        bundle.putString("AtriString1", elemento.getAtriString1());
        bundle.putString("AtriString2", elemento.getAtriString2());
        bundle.putString("AtriString3", elemento.getAtriString3());

// 4. Metemos el paquete completo dentro del Intent
        intent.putExtras(bundle);

// 5. Lanzamos la actividad
        startActivity(intent);
    }
}