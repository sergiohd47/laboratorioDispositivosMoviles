package com.sergiohernandezdominguez.practicabd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.sql.Blob;

public class SQLiteHelper extends SQLiteOpenHelper {
    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }
    public void consultaDatos(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }
    public void insertarDatos(String nombreSerie, String numeroTemporada, String numeroCapitulo, byte[] imagenPortada, String valoracion ){
        SQLiteDatabase database = getWritableDatabase();
        String sql="INSERT INTO SERIES VALUES (NULL,?,?,?,?,?)";
        SQLiteStatement statement=database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,nombreSerie);
        statement.bindString(2,numeroTemporada);
        statement.bindString(3,numeroCapitulo);
        statement.bindBlob(4,imagenPortada);
        statement.bindString(5,valoracion);
        statement.executeInsert();
    }
    public void actualizarDatos(String nombreSerie, String numeroTemporada, String numeroCapitulo, byte[] imagenPortada, String valoracion,
                                int id){
        SQLiteDatabase database=getWritableDatabase();
        String sql="UPDATE SERIES SET nombreSerie=?,numeroTemporada=?,numeroCapitulo=?,imagenPortada=?,valoracion=? WHERE id=?";
        SQLiteStatement statement=database.compileStatement(sql);
        statement.bindString(1,nombreSerie);
        statement.bindString(2,numeroTemporada);
        statement.bindString(3,numeroCapitulo);
        statement.bindBlob(4,imagenPortada);
        statement.bindString(5,valoracion);
        statement.bindDouble(6,(double)id);
        statement.execute();
        database.close();

    }
    public void borrarDatos(int id){
        SQLiteDatabase database=getWritableDatabase();
        String sql="DELETE FROM SERIES WHERE id=?";
        SQLiteStatement statement=database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1,(double)id);
        statement.execute();
        database.close();

    }
    public Cursor getDatos(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
