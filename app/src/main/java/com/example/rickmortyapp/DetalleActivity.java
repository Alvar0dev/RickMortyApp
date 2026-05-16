package com.example.rickmortyapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rickmortyapp.bbdd.ElementoDAO;
import com.example.rickmortyapp.modelo.Elemento;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.OutputStreamWriter;
import java.util.List;

public class DetalleActivity extends AppCompatActivity {

    private ImageView iv1;
    private TextView tv1, tv2, tv3;
    private ActivityResultLauncher<Intent> lanzadorUbicacion;
    private ElementoDAO dao;
    private String nombreActual; // Para guardar el nombre y usarlo en la actualización

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        // Inicializamos el DAO
        dao = new ElementoDAO(this);

        iv1 = findViewById(R.id.iv1);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        Button bt = findViewById(R.id.button);
        Button bt2 = findViewById(R.id.button2);
        Button btnGuardarConUbi = findViewById(R.id.btn_ver_ubicacion);

        // 1. Recogemos los datos (siguiendo tu lógica de Bundle)
        Bundle datosRecibidos = getIntent().getExtras();

        if (datosRecibidos != null) {
            // Intentamos sacar el objeto completo primero si existe
            Elemento objeto = (Elemento) datosRecibidos.getSerializable("objeto");
            
            if (objeto != null) {
                nombreActual = objeto.getAtriString1();
                tv1.setText(objeto.getAtriString1());
                tv2.setText(objeto.getAtriString2());
                tv3.setText(objeto.getAtriString4());
                if (objeto.getAtriString3() != null && !objeto.getAtriString3().isEmpty()) {
                    Picasso.get().load(objeto.getAtriString3()).into(iv1);
                }
            } else {
                // Si no viene el objeto, probamos por las claves individuales que usaste
                nombreActual = datosRecibidos.getString("AtriString1");
                tv1.setText(nombreActual);
                tv2.setText(datosRecibidos.getString("AtriString2"));
                tv3.setText(datosRecibidos.getString("AtriString4"));
                String imagen = datosRecibidos.getString("AtriString3");
                if (imagen != null && !imagen.isEmpty()) {
                    Picasso.get().load(imagen).into(iv1);
                }
            }
        }

        // 2. Inicializamos el lanzador
        lanzadorUbicacion = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            double lat = result.getData().getDoubleExtra("EXTRA_LATITUD", 0.0);
                            double lon = result.getData().getDoubleExtra("EXTRA_LONGITUD", 0.0);

                            actualizarUbicacionPersonaje(lat, lon);
                        }
                    }
                });

        // Eventos de botones
        btnGuardarConUbi.setOnClickListener(v -> {
            Intent intent = new Intent(DetalleActivity.this, UbiActivity.class);
            lanzadorUbicacion.launch(intent);
        });

        bt2.setOnClickListener(v -> exportarContactosAJson());

        bt.setOnClickListener(v -> {
            String url = "https://open.spotify.com/search/rick%20and%20morty";
            Intent intentSpotify = new Intent(Intent.ACTION_VIEW);
            intentSpotify.setData(Uri.parse(url));
            startActivity(intentSpotify);
        });
    }

    // Corregido: El método debe estar FUERA de onCreate y usar el DAO
    private void actualizarUbicacionPersonaje(double latitud, double longitud) {
        if (nombreActual == null) {
            Toast.makeText(this, "Error: Nombre no disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        // Usamos el método que creamos en el DAO
        int filasAfectadas = dao.actualizarUbicacion(nombreActual, latitud, longitud);

        if (filasAfectadas > 0) {
            Toast.makeText(this, "Ubicación actualizada para " + nombreActual, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error: No se encontró a " + nombreActual + " en favoritos", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportarContactosAJson() {
        List<Elemento> lista = dao.obtenerTodos();
        Gson gson = new Gson();
        String jsonString = gson.toJson(lista);
        try {
            OutputStreamWriter fout = new OutputStreamWriter(openFileOutput("contactos.json", Context.MODE_PRIVATE));
            fout.write(jsonString);
            fout.close();
            Toast.makeText(this, "Exportado correctamente a contactos.json", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(this, "Error al exportar: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}