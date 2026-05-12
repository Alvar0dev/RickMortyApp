package com.example.rickmortyapp.BBDD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "examen.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "tabla_elementos";
    // SQL siguiendo tu orden: String1 -> Int1 -> Otros
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nombre TEXT, " +
            "atriString2 TEXT, " +
            "atriString3 TEXT, " +
            "atriBoolean1 INTEGER)";
    public DbHelper(Context context) { super(context, DB_NAME, null, DB_VERSION); }
    @Override
    public void onCreate(SQLiteDatabase db) { db.execSQL(CREATE_TABLE); }
    @Override
    public void onUpgrade(SQLiteDatabase db, int old, int n) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
