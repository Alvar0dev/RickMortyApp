package com.example.rickmortyapp.menus;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

// Imports corregidos a minúsculas y completos
import com.example.rickmortyapp.R;
import com.example.rickmortyapp.MainActivity;
import com.example.rickmortyapp.FavoritosActivity;
import com.example.rickmortyapp.FormularioActivity;
import com.example.rickmortyapp.UbiActivity;

public abstract class MenuToolbar extends AppCompatActivity {

    protected static int fontSizeLevel = 1; 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            startActivity(new Intent(this, FormularioActivity.class));
            return true;
        } else if (id == R.id.action_exit) {
            confirmarSalida();
            return true;
        } else if (id == R.id.action_font_size) {
            cambiarTamanoLetra();
            return true;
        } else if (id == R.id.action_delete_db) {
            confirmarBorradoBD();
            return true;
        } else if (id == R.id.action_ubi) {
            startActivity(new Intent(this, UbiActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void confirmarSalida() {
        new AlertDialog.Builder(this)
                .setTitle("Salir")
                .setMessage("¿Deseas cerrar la aplicación?")
                .setPositiveButton("Sí", (dialog, which) -> finishAffinity())
                .setNegativeButton("No", null)
                .show();
    }

    private void cambiarTamanoLetra() {
        fontSizeLevel = (fontSizeLevel + 1) % 3;
        String mensaje = "";
        switch (fontSizeLevel) {
            case 0: mensaje = "Pequeño"; break;
            case 1: mensaje = "Normal"; break;
            case 2: mensaje = "Grande"; break;
        }
        Toast.makeText(this, "Tamaño: " + mensaje, Toast.LENGTH_SHORT).show();
        recreate();
    }

    private void confirmarBorradoBD() {
        new AlertDialog.Builder(this)
                .setTitle("Borrar Base de Datos")
                .setMessage("¿Estás seguro de que deseas borrar TODOS los datos?")
                .setPositiveButton("Borrar", (dialog, which) -> {
                    boolean deleted = deleteDatabase("rickmorty_gen.db");
                    if (deleted) {
                        Toast.makeText(this, "Base de datos borrada", Toast.LENGTH_SHORT).show();
                        recreate();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}