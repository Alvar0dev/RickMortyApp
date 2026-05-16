package com.example.rickmortyapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rickmortyapp.bbdd.ElementoDAO;
import com.example.rickmortyapp.modelo.Elemento;
import com.squareup.picasso.Picasso;

public class FormularioActivity extends AppCompatActivity {

    private EditText etNombre, etTelefono, etEmail, etImagenUrl;
    private Button btnGuardar;
    private ImageView imgDetalle;
    private ElementoDAO db;
    private Elemento elementoEditar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_formulario);
        
        // Buscamos el contenedor principal (ID corregido en el XML a 'main_formulario')
        View mainView = findViewById(R.id.main_formulario);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        db = new ElementoDAO(this);

        etNombre = findViewById(R.id.etNombre);
        etTelefono = findViewById(R.id.etTelefono);
        etEmail = findViewById(R.id.etEmailDetalle);
        etImagenUrl = findViewById(R.id.etImagenUrl);
        btnGuardar = findViewById(R.id.btnGuardar);
        imgDetalle = findViewById(R.id.imgDetalle);

        if (getIntent().hasExtra("elemento")) {
            elementoEditar = (Elemento) getIntent().getSerializableExtra("elemento");
            mostrarDatosElemento();
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCambios();
            }
        });

        etImagenUrl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    cargarImagenDesdeUrl(etImagenUrl.getText().toString());
                }
            }
        });
    }

    private void mostrarDatosElemento() {
        if (elementoEditar != null) {
            etNombre.setText(elementoEditar.getAtriString1());
            etEmail.setText(elementoEditar.getAtriString2());
            etImagenUrl.setText(elementoEditar.getAtriString3());
            etTelefono.setText(elementoEditar.getAtriString4());
            cargarImagenDesdeUrl(elementoEditar.getAtriString3());
            btnGuardar.setText("Actualizar");
        }
    }

    private void cargarImagenDesdeUrl(String url) {
        if (url != null && !url.isEmpty()) {
            Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(imgDetalle);
        }
    }

    private void guardarCambios() {
        String nombre = etNombre.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String url = etImagenUrl.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();

        if (nombre.isEmpty()) {
            Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        if (elementoEditar != null) {
            elementoEditar.setAtriString1(nombre);
            elementoEditar.setAtriString2(email);
            elementoEditar.setAtriString3(url);
            elementoEditar.setAtriString4(telefono);

            int filas = db.actualizar(elementoEditar);
            if (filas > 0) {
                Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Elemento nuevo = new Elemento();
            nuevo.setAtriInt1(0);
            nuevo.setAtriString1(nombre);
            nuevo.setAtriString2(email);
            nuevo.setAtriString3(url);
            nuevo.setAtriString4(telefono);

            long id = db.insertar(nuevo);
            if (id != -1) {
                Toast.makeText(this, "Guardado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}