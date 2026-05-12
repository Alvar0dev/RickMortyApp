package com.example.rickmortyapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
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
    private Elemento elementoSeleccionado;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dao = new ElementoDAO(this);
        recyclerView = findViewById(R.id.rv);
        fab = findViewById(R.id.fab);

        // MODO HORIZONTAL EN GRID DE 2 COLUMNAS
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        adaptador = new ElementoAdapter(listaElementos, this);
        recyclerView.setAdapter(adaptador);
        cargarDatosDeInternet();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ejecutarAccionBotonFlotante();
            }
        });
    }

    private void ejecutarAccionBotonFlotante() {
        Toast.makeText(this, "Clic en el botón flotante", Toast.LENGTH_SHORT).show();
    }

    private void cargarDatosDeInternet() {
        RetrofitClient.getApiService().getElementos().enqueue(new Callback<ElementoResponse>() {
            @Override
            public void onResponse(Call<ElementoResponse> call, Response<ElementoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Elemento> descargados = response.body().getelementos();
                    listaElementos.clear();
                    listaElementos.addAll(descargados);
                    adaptador.actualizarlista(descargados);
                }
            }

            @Override
            public void onFailure(Call<ElementoResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMeGustaClick(Elemento elemento) {
        // Implementar lógica de me gusta si es necesario
    }

    @Override
    public void onFavoritoClick(Elemento elemento) {
        long id = dao.insertar(elemento);
        if (id != -1) {
            Toast.makeText(this, "Guardado en favoritos", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ya está en favoritos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(View v) {
        // Nota: Asegúrate de que elementoSeleccionado se asigne en algún lugar o usa el objeto del adaptador
        if (elementoSeleccionado != null) {
            Intent intent = new Intent(this, DetalleActivity.class);
            intent.putExtra("elemento_id", elementoSeleccionado.getId());
            intent.putExtra("elemento_nombre", elementoSeleccionado.getAtriString1());
            intent.putExtra("elemento_status", elementoSeleccionado.getAtriString3());
            startActivity(intent);
        }
    }
}