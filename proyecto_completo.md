# Código RickMortyApp

## Archivo: `app\build.gradle.kts`

````kotlin
plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.rickmortyapp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.rickmortyapp"
        minSdk = 33
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // ️ PICASSO: Descarga y caché de imágenes desde URL
    implementation("com.squareup.picasso:picasso:2.71828")
// ️ GOOGLE MAPS: Para mapas, marcadores y geolocalización (PMDM07)
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
// 🌐 Retrofit + GSON (Para descargar y leer el JSON de la API)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

## Archivo: `app\src\main\AndroidManifest.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.RickMortyApp">
        <activity
            android:name=".UbiActivity"
            android:exported="false" />
        <activity
            android:name=".FavoritosActivity"
            android:exported="false" />
        <activity
            android:name=".DetalleActivity"
            android:exported="false" />
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="AQUI_PEGAR_LA_CLAVE_QUE_DE_EL_PROFESOR_O_TU_CLAVE" />
    </application>

</manifest>
````

## Archivo: `app\src\main\java\com\example\rickmortyapp\DetalleActivity.java`

```java
package com.example.rickmortyapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
```

## Archivo: `app\src\main\java\com\example\rickmortyapp\FavoritosActivity.java`

```java
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

import com.example.rickmortyapp.bbdd.ElementoDAO;
import com.example.rickmortyapp.adapter.ElementoAdapter;
import com.example.rickmortyapp.modelo.Elemento;

import java.util.ArrayList;

public class FavoritosActivity extends AppCompatActivity implements ElementoAdapter.OnItemClickListener {

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
```

## Archivo: `app\src\main\java\com\example\rickmortyapp\MainActivity.java`

```java
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

import com.example.rickmortyapp.bbdd.ElementoDAO;
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
```

## Archivo: `app\src\main\java\com\example\rickmortyapp\UbiActivity.java`

```java
package com.example.rickmortyapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UbiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ubi);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
```

## Archivo: `app\src\main\java\com\example\rickmortyapp\adapter\ElementoAdapter.java`

```java
package com.example.rickmortyapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rickmortyapp.R;
import com.example.rickmortyapp.modelo.Elemento;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class ElementoAdapter extends RecyclerView.Adapter<ElementoAdapter.MyViewHolder> {
    private ArrayList<Elemento> lista;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onFavoritoClick(Elemento elemento);
        void onItemClick(Elemento elemento); // Cambiado para pasar el objeto directamente
    }

    public ElementoAdapter(ArrayList<Elemento> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener; // ¡IMPORTANTE! Guardar el listener
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_elemento, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Elemento e = lista.get(position);
        holder.tv1.setText(e.getAtriString1()); // Nombre
        holder.tv2.setText(e.getAtriString2()); // Estado

        if (e.getAtriString3() != null && !e.getAtriString3().isEmpty()) {
            Picasso.get().load(e.getAtriString3()).into(holder.iv1);
        }

        holder.btnFavorito.setOnClickListener(v -> {
            if (listener != null) listener.onFavoritoClick(e);
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(e);
        });
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public void actualizarlista(List<Elemento> descargados) {
        this.lista.clear();
        this.lista.addAll(descargados);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View btnFavorito;
        TextView tv1, tv2;
        ImageView iv1;

        public MyViewHolder(@NonNull View v) {
            super(v);
            tv1 = v.findViewById(R.id.tvString1);
            tv2 = v.findViewById(R.id.tvString2);
            iv1 = v.findViewById(R.id.tvString3); // Tu ImageView tiene este ID en el XML
            btnFavorito = v.findViewById(R.id.btnFavorito);
        }
    }
}
```

## Archivo: `app\src\main\java\com\example\rickmortyapp\api\ApiService.java`

```java
package com.example.rickmortyapp.api;

import retrofit2.Call;
import retrofit2.http.GET;
public interface ApiService {
    // Aquí ponemos la parte final de la URL del examen
    @GET("character")
    Call<ElementoResponse> getElementos();
}
```

## Archivo: `app\src\main\java\com\example\rickmortyapp\api\ElementoResponse.java`

```java
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
```

## Archivo: `app\src\main\java\com\example\rickmortyapp\api\RetrofitClient.java`

```java
package com.example.rickmortyapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClient {
    // La URL BASE (hasta la última barra antes del archivo)
    private static final String BASE_URL = "https://rickandmortyapi.com/api/";
    private static Retrofit retrofit = null;
    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)

                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
// Aquí es donde "unimos" el motor con la interfaz
        return retrofit.create(ApiService.class);
    }
}
```

## Archivo: `app\src\main\java\com\example\rickmortyapp\BBDD\DbHelper.java`

```java
package com.example.rickmortyapp.bbdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "rickmorty_gen.db";
    public static final int DB_VERSION = 3; // Incrementamos versión para asegurar que se recrea la tabla
    public static final String TABLE = "tabla_elementos";

    // Nombres de columnas genéricos
    public static final String COL_PK = "atriPK";
    public static final String COL_INT1 = "atriInt1";
    public static final String COL_STRING1 = "atriString1";
    public static final String COL_STRING2 = "atriString2";
    public static final String COL_STRING3 = "atriString3";
    public static final String COL_STRING4 = "atriString4";

    // Columnas para ubicación
    public static final String COL_LAT = "latitud";
    public static final String COL_LON = "longitud";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" +
            COL_PK + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_INT1 + " INTEGER, " +
            COL_STRING1 + " TEXT, " +
            COL_STRING2 + " TEXT, " +
            COL_STRING3 + " TEXT, " +
            COL_STRING4 + " TEXT, " +
            COL_LAT + " REAL, " +
            COL_LON + " REAL)";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
```

## Archivo: `app\src\main\java\com\example\rickmortyapp\BBDD\ElementoDAO.java`

```java
package com.example.rickmortyapp.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.rickmortyapp.modelo.Elemento;

import java.util.ArrayList;

public class ElementoDAO {
    private DbHelper dbHelper;

    public ElementoDAO(Context context) {
        dbHelper = new DbHelper(context);
    }

    public boolean existe(int atriPK) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.TABLE, null, DbHelper.COL_PK + "=?",
                new String[]{String.valueOf(atriPK)}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    private boolean existePorApi(int atriInt1) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.TABLE, null, DbHelper.COL_INT1 + "=?",
                new String[]{String.valueOf(atriInt1)}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public long insertar(Elemento e) {
        if (existePorApi(e.getAtriInt1())) return -1;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbHelper.COL_INT1, e.getAtriInt1());
        values.put(DbHelper.COL_STRING1, e.getAtriString1());
        values.put(DbHelper.COL_STRING2, e.getAtriString2());
        values.put(DbHelper.COL_STRING3, e.getAtriString3());
        values.put(DbHelper.COL_STRING4, e.getAtriString4());
        values.put(DbHelper.COL_LAT, e.getAtriDouble1());
        values.put(DbHelper.COL_LON, e.getAtriDouble2());

        return db.insert(DbHelper.TABLE, null, values);
    }

    // Nuevo método para actualizar ubicación por nombre (atriString1)
    public int actualizarUbicacion(String nombre, double lat, double lon) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbHelper.COL_LAT, lat);
        values.put(DbHelper.COL_LON, lon);

        return db.update(DbHelper.TABLE, values, DbHelper.COL_STRING1 + " = ?", new String[]{nombre});
    }

    public ArrayList<Elemento> obtenerTodos() {
        ArrayList<Elemento> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.TABLE, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Elemento e = new Elemento();
                e.setAtriPK(cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.COL_PK)));
                e.setAtriInt1(cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.COL_INT1)));
                e.setAtriString1(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_STRING1)));
                e.setAtriString2(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_STRING2)));
                e.setAtriString3(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_STRING3)));
                e.setAtriString4(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COL_STRING4)));
                e.setAtriDouble1(cursor.getDouble(cursor.getColumnIndexOrThrow(DbHelper.COL_LAT)));
                e.setAtriDouble2(cursor.getDouble(cursor.getColumnIndexOrThrow(DbHelper.COL_LON)));
                lista.add(e);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    public void borrar(int atriPK) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DbHelper.TABLE, DbHelper.COL_PK + "=?", new String[]{String.valueOf(atriPK)});
    }
}
```

## Archivo: `app\src\main\java\com\example\rickmortyapp\modelo\Elemento.java`

```java
package com.example.rickmortyapp.modelo;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Elemento implements Serializable {

    private int atriPK;

    @SerializedName("id")
    private int atriInt1;

    @SerializedName("name")
    private String atriString1;

    @SerializedName("status")
    private String atriString2;

    @SerializedName("image")
    private String atriString3;

    @SerializedName("species")
    private String atriString4;

    // Atributos genéricos para Latitud y Longitud
    private double atriDouble1;
    private double atriDouble2;

    public Elemento() {}

    public int getAtriPK() { return atriPK; }
    public void setAtriPK(int atriPK) { this.atriPK = atriPK; }

    public int getAtriInt1() { return atriInt1; }
    public void setAtriInt1(int atriInt1) { this.atriInt1 = atriInt1; }

    public String getAtriString1() { return atriString1; }
    public void setAtriString1(String atriString1) { this.atriString1 = atriString1; }

    public String getAtriString2() { return atriString2; }
    public void setAtriString2(String atriString2) { this.atriString2 = atriString2; }

    public String getAtriString3() { return atriString3; }
    public void setAtriString3(String atriString3) { this.atriString3 = atriString3; }

    public String getAtriString4() { return atriString4; }
    public void setAtriString4(String atriString4) { this.atriString4 = atriString4; }

    public double getAtriDouble1() { return atriDouble1; }
    public void setAtriDouble1(double atriDouble1) { this.atriDouble1 = atriDouble1; }

    public double getAtriDouble2() { return atriDouble2; }
    public void setAtriDouble2(double atriDouble2) { this.atriDouble2 = atriDouble2; }
}
```

## Archivo: `app\src\main\res\drawable\ic_launcher_background.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <path
        android:fillColor="#3DDC84"
        android:pathData="M0,0h108v108h-108z" />
    <path
        android:fillColor="#00000000"
        android:pathData="M9,0L9,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,0L19,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M29,0L29,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M39,0L39,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M49,0L49,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M59,0L59,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M69,0L69,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M79,0L79,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M89,0L89,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M99,0L99,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,9L108,9"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,19L108,19"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,29L108,29"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,39L108,39"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,49L108,49"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,59L108,59"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,69L108,69"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,79L108,79"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,89L108,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,99L108,99"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,29L89,29"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,39L89,39"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,49L89,49"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,59L89,59"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,69L89,69"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,79L89,79"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M29,19L29,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M39,19L39,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M49,19L49,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M59,19L59,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M69,19L69,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M79,19L79,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
</vector>
```

## Archivo: `app\src\main\res\drawable\ic_launcher_foreground.xml`

```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:aapt="http://schemas.android.com/aapt"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <path android:pathData="M31,63.928c0,0 6.4,-11 12.1,-13.1c7.2,-2.6 26,-1.4 26,-1.4l38.1,38.1L107,108.928l-32,-1L31,63.928z">
        <aapt:attr name="android:fillColor">
            <gradient
                android:endX="85.84757"
                android:endY="92.4963"
                android:startX="42.9492"
                android:startY="49.59793"
                android:type="linear">
                <item
                    android:color="#44000000"
                    android:offset="0.0" />
                <item
                    android:color="#00000000"
                    android:offset="1.0" />
            </gradient>
        </aapt:attr>
    </path>
    <path
        android:fillColor="#FFFFFF"
        android:fillType="nonZero"
        android:pathData="M65.3,45.828l3.8,-6.6c0.2,-0.4 0.1,-0.9 -0.3,-1.1c-0.4,-0.2 -0.9,-0.1 -1.1,0.3l-3.9,6.7c-6.3,-2.8 -13.4,-2.8 -19.7,0l-3.9,-6.7c-0.2,-0.4 -0.7,-0.5 -1.1,-0.3C38.8,38.328 38.7,38.828 38.9,39.228l3.8,6.6C36.2,49.428 31.7,56.028 31,63.928h46C76.3,56.028 71.8,49.428 65.3,45.828zM43.4,57.328c-0.8,0 -1.5,-0.5 -1.8,-1.2c-0.3,-0.7 -0.1,-1.5 0.4,-2.1c0.5,-0.5 1.4,-0.7 2.1,-0.4c0.7,0.3 1.2,1 1.2,1.8C45.3,56.528 44.5,57.328 43.4,57.328L43.4,57.328zM64.6,57.328c-0.8,0 -1.5,-0.5 -1.8,-1.2s-0.1,-1.5 0.4,-2.1c0.5,-0.5 1.4,-0.7 2.1,-0.4c0.7,0.3 1.2,1 1.2,1.8C66.5,56.528 65.6,57.328 64.6,57.328L64.6,57.328z"
        android:strokeWidth="1"
        android:strokeColor="#00000000" />
</vector>
```

## Archivo: `app\src\main\res\layout\activity_detalle.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp"
    tools:context=".DetalleActivity">

    <ImageView
        android:id="@+id/iv1"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:layout_marginTop="32dp"
        android:scaleType="centerCrop"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        tools:srcCompat="@tools:sample/avatars" />

    <TextView
        android:id="@+id/tv1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:text="Nombre"
        android:textSize="24sp"
        android:textStyle="bold"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/iv1" />

    <TextView
        android:id="@+id/tv2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="Status"
        android:textSize="18sp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/tv1" />

    <TextView
        android:id="@+id/tv3"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:text="Especie"
        android:textSize="18sp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/tv2" />

    <Button
        android:id="@+id/button"
        android:layout_width="156dp"
        android:layout_height="68dp"
        android:text="Escuchar Musica"
        tools:layout_editor_absoluteX="116dp"
        tools:layout_editor_absoluteY="400dp" />

    <Button
        android:id="@+id/button2"
        android:layout_width="241dp"
        android:layout_height="76dp"
        android:text="Exportar JSON"
        tools:layout_editor_absoluteX="74dp"
        tools:layout_editor_absoluteY="484dp" />

    <Button
        android:id="@+id/btn_ver_ubicacion"
        android:layout_width="141dp"
        android:layout_height="84dp"
        android:text="Guardar Ubi"
        tools:layout_editor_absoluteX="125dp"
        tools:layout_editor_absoluteY="583dp" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

## Archivo: `app\src\main\res\layout\activity_favoritos.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">



    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rv2"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:layout_editor_absoluteX="1dp"
        tools:layout_editor_absoluteY="1dp" />


</androidx.constraintlayout.widget.ConstraintLayout>
```

## Archivo: `app\src\main\res\layout\activity_main.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">



    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rv"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:layout_editor_absoluteX="1dp"
        tools:layout_editor_absoluteY="1dp" />

    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@+id/fab"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:clickable="true"
        app:srcCompat="@android:drawable/ic_menu_search"
        tools:layout_editor_absoluteX="340dp"
        tools:layout_editor_absoluteY="647dp" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

## Archivo: `app\src\main\res\layout\activity_ubi.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".UbiActivity">

</androidx.constraintlayout.widget.ConstraintLayout>
```

## Archivo: `app\src\main\res\layout\item_elemento.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/item_layout"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:padding="10dp">

    <ImageView
        android:id="@+id/tvString3"
        android:layout_width="80dp"
        android:layout_height="80dp"
        android:scaleType="centerCrop"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        tools:src="@mipmap/ic_launcher" />

    <TextView
        android:id="@+id/tvString1"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:text="AtriString1"
        android:textColor="@android:color/black"
        android:textSize="18sp"
        android:textStyle="bold"
        app:layout_constraintEnd_toStartOf="@+id/btnFavorito"
        app:layout_constraintStart_toEndOf="@+id/tvString3"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/tvString2"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:text="AtriString2"
        android:textSize="14sp"
        app:layout_constraintEnd_toStartOf="@+id/btnFavorito"
        app:layout_constraintStart_toEndOf="@+id/tvString3"
        app:layout_constraintTop_toBottomOf="@+id/tvString1" />

    <ImageButton
        android:id="@+id/btnFavorito"
        android:layout_width="48dp"
        android:layout_height="48dp"
        android:background="?attr/selectableItemBackgroundBorderless"
        android:src="@android:drawable/btn_star_big_on"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

## Archivo: `app\src\main\res\layout-land\activity_favoritos.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">



    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rv2"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:layout_editor_absoluteX="1dp"
        tools:layout_editor_absoluteY="1dp" />


</androidx.constraintlayout.widget.ConstraintLayout>
```

## Archivo: `app\src\main\res\layout-land\activity_main.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rv"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@+id/fab"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        android:clickable="true"
        android:focusable="true"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:srcCompat="@android:drawable/btn_star_big_on" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

## Archivo: `app\src\main\res\values\colors.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>
</resources>
```

## Archivo: `app\src\main\res\values\strings.xml`

```xml
<resources>
    <string name="app_name">RickMortyApplicacion</string>
</resources>
```

## Archivo: `app\src\main\res\values\themes.xml`

```xml
<resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Base application theme. -->
    <style name="Base.Theme.RickMortyApp" parent="Theme.Material3.DayNight.NoActionBar">
        <!-- Customize your light theme here. -->
        <!-- <item name="colorPrimary">@color/my_light_primary</item> -->
    </style>

    <style name="Theme.RickMortyApp" parent="Base.Theme.RickMortyApp" />
</resources>
```

## Archivo: `app\src\main\res\values-en\strings.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">RickMortyApp</string>
</resources>
```

## Archivo: `app\src\main\res\values-night\themes.xml`

```xml
<resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Base application theme. -->
    <style name="Base.Theme.RickMortyApp" parent="Theme.Material3.DayNight.NoActionBar">
        <!-- Customize your dark theme here. -->
        <!-- <item name="colorPrimary">@color/my_dark_primary</item> -->
    </style>
</resources>
```

## Archivo: `app\src\main\res\xml\backup_rules.xml`

```xml
<?xml version="1.0" encoding="utf-8"?><!--
   Sample backup rules file; uncomment and customize as necessary.
   See https://developer.android.com/guide/topics/data/autobackup
   for details.
   Note: This file is ignored for devices older than API 31
   See https://developer.android.com/about/versions/12/backup-restore
-->
<full-backup-content>
    <!--
   <include domain="sharedpref" path="."/>
   <exclude domain="sharedpref" path="device.xml"/>
-->
</full-backup-content>
```

## Archivo: `app\src\main\res\xml\data_extraction_rules.xml`

```xml
<?xml version="1.0" encoding="utf-8"?><!--
   Sample data extraction rules file; uncomment and customize as necessary.
   See https://developer.android.com/about/versions/12/backup-restore#xml-changes
   for details.
-->
<data-extraction-rules>
    <cloud-backup>
        <!-- TODO: Use <include> and <exclude> to control what is backed up.
        <include .../>
        <exclude .../>
        -->
    </cloud-backup>
    <!--
    <device-transfer>
        <include .../>
        <exclude .../>
    </device-transfer>
    -->
</data-extraction-rules>
```

## Archivo: `app\src\test\java\com\example\rickmortyapp\ExampleUnitTest.java`

```java
package com.example.rickmortyapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}
```
