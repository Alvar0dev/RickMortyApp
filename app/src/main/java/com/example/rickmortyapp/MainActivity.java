package com.example.rickmortyapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rickmortyapp.bbdd.ElementoDAO;
import com.example.rickmortyapp.adapter.ElementoAdapter;
import com.example.rickmortyapp.api.ElementoResponse;
import com.example.rickmortyapp.api.RetrofitClient;
import com.example.rickmortyapp.menus.MenuToolbar;
import com.example.rickmortyapp.modelo.Elemento;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends MenuToolbar implements ElementoAdapter.OnItemClickListener {

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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dao = new ElementoDAO(this);
        recyclerView = findViewById(R.id.rv);
        fab = findViewById(R.id.fab);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        adaptador = new ElementoAdapter(listaElementos, this, false);
        recyclerView.setAdapter(adaptador);
        
        cargarDatosDeInternet();

        fab.setOnClickListener(v -> {
            startActivity(new Intent(this, FavoritosActivity.class));
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
                Snackbar.make(findViewById(R.id.main), "Error de red", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onFavoritoClick(Elemento elemento) {
        if (dao.existePorApi(elemento.getAtriInt1())) {
            dao.borrarPorApiId(elemento.getAtriInt1());
            Snackbar.make(findViewById(R.id.main), "Eliminado de favoritos", Snackbar.LENGTH_SHORT).show();
        } else {
            long id = dao.insertar(elemento);
            if (id != -1) {
                Snackbar.make(findViewById(R.id.main), "Guardado en favoritos", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemClick(Elemento elemento) {
        Intent intent = new Intent(this, DetalleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("objeto", elemento);
        bundle.putString("AtriString1", elemento.getAtriString1());
        bundle.putString("AtriString2", elemento.getAtriString2());
        bundle.putString("AtriString3", elemento.getAtriString3());
        bundle.putString("AtriString4", elemento.getAtriString4());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}