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

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("elemento")) {
            elementoEditar = (Elemento) getIntent().getExtras().getSerializable("elemento");
            if (elementoEditar != null) {
                mostrarDatosElemento();
                btnGuardar.setText("Actualizar");
            }
        } else {
            btnGuardar.setText("Guardar Nuevo");
        }

        btnGuardar.setOnClickListener(v -> guardarCambios());

        etImagenUrl.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                cargarImagenDesdeUrl(etImagenUrl.getText().toString());
            }
        });
    }

    private void mostrarDatosElemento() {
        etNombre.setText(elementoEditar.getAtriString1());
        etEmail.setText(elementoEditar.getAtriString2());
        etImagenUrl.setText(elementoEditar.getAtriString3());
        etTelefono.setText(elementoEditar.getAtriString4());
        cargarImagenDesdeUrl(elementoEditar.getAtriString3());
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
        String atriString2 = etEmail.getText().toString().trim();
        String url = etImagenUrl.getText().toString().trim();
        String atriString4 = etTelefono.getText().toString().trim();

        if (nombre.isEmpty()) {
            Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        Elemento elemento = (elementoEditar != null) ? elementoEditar : new Elemento();
        elemento.setAtriString1(nombre);
        elemento.setAtriString2(atriString2);
        elemento.setAtriString3(url);
        elemento.setAtriString4(atriString4);
        
        if (elementoEditar == null) elemento.setAtriInt1(0);

        long resultado = db.guardar(elemento);

        if (resultado != -1) {
            Toast.makeText(this, "Guardado con éxito", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
        }
    }
}