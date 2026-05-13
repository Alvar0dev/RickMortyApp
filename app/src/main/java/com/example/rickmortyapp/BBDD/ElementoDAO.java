package com.example.rickmortyapp.BBDD;

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