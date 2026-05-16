package com.example.rickmortyapp.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.rickmortyapp.modelo.Elemento;

import java.util.ArrayList;

public class ElementoDAO {
    private com.example.rickmortyapp.bbdd.DbHelper dbHelper;

    public ElementoDAO(Context context) {
        dbHelper = new com.example.rickmortyapp.bbdd.DbHelper(context);
    }

    public boolean existe(int atriPK) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(com.example.rickmortyapp.bbdd.DbHelper.TABLE, null, com.example.rickmortyapp.bbdd.DbHelper.COL_PK + "=?",
                new String[]{String.valueOf(atriPK)}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean existePorApi(int atriInt1) {
        if (atriInt1 == 0) return false; 
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(com.example.rickmortyapp.bbdd.DbHelper.TABLE, null, com.example.rickmortyapp.bbdd.DbHelper.COL_INT1 + "=?",
                new String[]{String.valueOf(atriInt1)}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public long insertar(Elemento e) {
        if (e.getAtriInt1() != 0 && existePorApi(e.getAtriInt1())) return -1;
        
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(com.example.rickmortyapp.bbdd.DbHelper.COL_INT1, e.getAtriInt1());
        values.put(com.example.rickmortyapp.bbdd.DbHelper.COL_STRING1, e.getAtriString1());
        values.put(com.example.rickmortyapp.bbdd.DbHelper.COL_STRING2, e.getAtriString2());
        values.put(com.example.rickmortyapp.bbdd.DbHelper.COL_STRING3, e.getAtriString3());
        values.put(com.example.rickmortyapp.bbdd.DbHelper.COL_STRING4, e.getAtriString4());
        values.put(com.example.rickmortyapp.bbdd.DbHelper.COL_LAT, e.getAtriDouble1());
        values.put(com.example.rickmortyapp.bbdd.DbHelper.COL_LON, e.getAtriDouble2());
        
        return db.insert(com.example.rickmortyapp.bbdd.DbHelper.TABLE, null, values);
    }

    public int actualizar(Elemento e) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(com.example.rickmortyapp.bbdd.DbHelper.COL_STRING1, e.getAtriString1());
        values.put(com.example.rickmortyapp.bbdd.DbHelper.COL_STRING2, e.getAtriString2());
        values.put(com.example.rickmortyapp.bbdd.DbHelper.COL_STRING3, e.getAtriString3());
        values.put(com.example.rickmortyapp.bbdd.DbHelper.COL_STRING4, e.getAtriString4());
        
        return db.update(com.example.rickmortyapp.bbdd.DbHelper.TABLE, values, com.example.rickmortyapp.bbdd.DbHelper.COL_PK + " = ?",
                new String[]{String.valueOf(e.getAtriPK())});
    }

    public int actualizarUbicacion(String nombre, double lat, double lon) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(com.example.rickmortyapp.bbdd.DbHelper.COL_LAT, lat);
        values.put(com.example.rickmortyapp.bbdd.DbHelper.COL_LON, lon);
        return db.update(com.example.rickmortyapp.bbdd.DbHelper.TABLE, values, com.example.rickmortyapp.bbdd.DbHelper.COL_STRING1 + " = ?", new String[]{nombre});
    }

    public ArrayList<Elemento> obtenerTodos() {
        ArrayList<Elemento> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(com.example.rickmortyapp.bbdd.DbHelper.TABLE, null, null, null, null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                Elemento e = new Elemento();
                e.setAtriPK(cursor.getInt(cursor.getColumnIndexOrThrow(com.example.rickmortyapp.bbdd.DbHelper.COL_PK)));
                e.setAtriInt1(cursor.getInt(cursor.getColumnIndexOrThrow(com.example.rickmortyapp.bbdd.DbHelper.COL_INT1)));
                e.setAtriString1(cursor.getString(cursor.getColumnIndexOrThrow(com.example.rickmortyapp.bbdd.DbHelper.COL_STRING1)));
                e.setAtriString2(cursor.getString(cursor.getColumnIndexOrThrow(com.example.rickmortyapp.bbdd.DbHelper.COL_STRING2)));
                e.setAtriString3(cursor.getString(cursor.getColumnIndexOrThrow(com.example.rickmortyapp.bbdd.DbHelper.COL_STRING3)));
                e.setAtriString4(cursor.getString(cursor.getColumnIndexOrThrow(com.example.rickmortyapp.bbdd.DbHelper.COL_STRING4)));
                e.setAtriDouble1(cursor.getDouble(cursor.getColumnIndexOrThrow(com.example.rickmortyapp.bbdd.DbHelper.COL_LAT)));
                e.setAtriDouble2(cursor.getDouble(cursor.getColumnIndexOrThrow(com.example.rickmortyapp.bbdd.DbHelper.COL_LON)));
                lista.add(e);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    public void borrar(int atriPK) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(com.example.rickmortyapp.bbdd.DbHelper.TABLE, com.example.rickmortyapp.bbdd.DbHelper.COL_PK + "=?", new String[]{String.valueOf(atriPK)});
    }

    public void borrarPorApiId(int atriInt1) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(com.example.rickmortyapp.bbdd.DbHelper.TABLE, com.example.rickmortyapp.bbdd.DbHelper.COL_INT1 + "=?", new String[]{String.valueOf(atriInt1)});
    }
}