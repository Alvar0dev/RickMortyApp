package com.example.rickmortyapp.bbdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "rickmorty_gen.db";
    public static final int DB_VERSION = 3; 
    public static final String TABLE = "tabla_elementos";

    public static final String COL_PK = "atriPK";
    public static final String COL_INT1 = "atriInt1";
    public static final String COL_STRING1 = "atriString1";
    public static final String COL_STRING2 = "atriString2";
    public static final String COL_STRING3 = "atriString3";
    public static final String COL_STRING4 = "atriString4";
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