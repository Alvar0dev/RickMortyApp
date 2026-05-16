package com.example.rickmortyapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rickmortyapp.bbdd.ElementoDAO;
import com.example.rickmortyapp.adapter.ElementoAdapter;
import com.example.rickmortyapp.menus.MenuToolbar;
import com.example.rickmortyapp.modelo.Elemento;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class FavoritosActivity extends MenuToolbar implements ElementoAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ElementoAdapter adaptador;
    private ElementoDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favoritos);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dao = new ElementoDAO(this);
        recyclerView = findViewById(R.id.rv2);

        configurarRecyclerView();
        refrescarLista();
    }

    private void configurarRecyclerView() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        adaptador = new ElementoAdapter(new ArrayList<>(), this, true);
        recyclerView.setAdapter(adaptador);
    }

    private void refrescarLista() {
        adaptador.actualizarlista(dao.obtenerTodos());
    }

    @Override
    public void onModificarClick(Elemento elemento) {
        // CORRECCIÓN: Usamos la clave "elemento" que es la que busca FormularioActivity
        Intent intent = new Intent(this, FormularioActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("elemento", elemento); 
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onBorrarClick(Elemento elemento) {
        dao.borrar(elemento.getAtriPK());
        refrescarLista();
        Snackbar.make(findViewById(R.id.main), "Eliminado", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(Elemento elemento) {
        Intent intent = new Intent(this, DetalleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("objeto", elemento);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onFavoritoClick(Elemento elemento) {}

    @Override
    protected void onResume() {
        super.onResume();
        refrescarLista(); // Refresca por si volvimos de modificar
    }
}