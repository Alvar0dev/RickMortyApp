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
    public boolean existe(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbHelper.TABLE + " WHERE id = ?", new String[]{String.valueOf(id)});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
    public long insertar(Elemento p) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", p.getId());
        values.put("nombre", p.getAtriString1());
        values.put("url", p.getAtriString3());
        values.put("status", p.getAtriString2());
        //values.put("atriInt2", p.getAtriInt2());
        //values.put("latitud", p.getLatitud());
        //values.put("longitud", p.getLongitud());
        db.insert(DbHelper.TABLE, null, values);
        return 0;
    }
    public ArrayList<Elemento> obtenerTodos() {
        ArrayList<Elemento> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DbHelper.TABLE, null);
        if (cursor.moveToFirst()) {
            do {
                Elemento p = new Elemento();
                p.setId(cursor.getInt(0));
                p.setAtriString1(cursor.getString(1));
                p.setAtriString2(cursor.getString(2));
                p.setAtriString3(cursor.getString(3));
                //   p.setAtriInt2(cursor.getInt(4));
                //  p.setLatitud(cursor.getDouble(5));
                //  p.setLongitud(cursor.getDouble(6));
                lista.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }
    public void borrar(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DbHelper.TABLE, "id=?", new String[]{String.valueOf(id)});
    }
}
