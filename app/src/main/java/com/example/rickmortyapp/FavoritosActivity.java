package com.example.rickmortyapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rickmortyapp.BBDD.ElementoDAO;
import com.example.rickmortyapp.adapter.ElementoAdapter;
import com.example.rickmortyapp.modelo.Elemento;

import java.util.ArrayList;

public class FavoritosActivity extends AppCompatActivity implements ElementoAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private ElementoAdapter adaptador;
    private ArrayList<Elemento> listaElementos = new ArrayList<>();
    private ElementoDAO dao;

    // 1. Declaramos el lanzador para recoger el resultado de UbiActivity[cite: 8]



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favoritos);

        dao = new ElementoDAO(this);
        recyclerView = findViewById(R.id.rv2);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        adaptador = new ElementoAdapter(dao.obtenerTodos(), this);
        recyclerView.setAdapter(adaptador);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onFavoritoClick(Elemento elemento) {

    }

    @Override
    public void onItemClick(Elemento elemento) {
        // 1. Creamos la intención hacia la pantalla de detalle
        Intent intent = new Intent(this, DetalleActivity.class);

// 2. Creamos un Bundle (el "contenedor" o paquete)
        Bundle bundle = new Bundle();

// 3. Metemos los datos dentro del paquete usando claves
        bundle.putString("AtriString1", elemento.getAtriString1());
        bundle.putString("atriString2", elemento.getAtriString2());
        bundle.putString("atriString3", elemento.getAtriString3());

// 4. Metemos el paquete completo dentro del Intent
        intent.putExtras(bundle);

// 5. Lanzamos la actividad
        startActivity(intent);
    }
}