package com.cramcat.platform.CRUDapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
public class DB extends SQLiteOpenHelper {

    private static final String DBNAME = "Crud2.db";
    private static final int DBVERSION = 4;
    //Tablas
    public static final String USER = "USER";

    //Constructor sqlite
    public DB(Context context) {
        super(context, DBNAME, null,DBVERSION);
    }


    public boolean delete(int id){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(USER, "ID = ?", new String[]{String.valueOf(id)});
            db.close();
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public void update(int ID,String username, String mail, String password) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("USERNAME", username);
        values.put("MAIL", mail);
        values.put("PASSWORD", password);

        String whereClause = "ID=?";
        String[] whereArgs = {String.valueOf(ID)};

        sqLiteDatabase.update(USER, values, whereClause, whereArgs);
    }

    public void insert(String username, String mail, String password){
        Integer newId = checkId();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        if (newId != null) {
            ContentValues values = new ContentValues();
            values.put("ID", newId);
            values.put("USERNAME", username);
            values.put("MAIL", mail);
            values.put("PASSWORD", password);
            values.put("ISADMIN", 0);
            values.put("ISBLOCK", 0);

            sqLiteDatabase.insert(USER, null, values);
        }
    }

    public boolean searchUser(String mail, String password) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + USER + " WHERE MAIL = ? AND PASSWORD = ?", new String[]{mail, password});

        try {
            return data.moveToNext(); // Return true if there is at least one matching user
        } finally {
            data.close(); // Always close the cursor to release resources
        }
    }


    public List<String> databaseOnList(int ID) {
        List<String> dataList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor data = null;

        try {
            db = getWritableDatabase();
            data = db.rawQuery("SELECT * FROM " + USER + " WHERE ID = ?", new String[]{String.valueOf(ID)});

            while (data.moveToNext()) {
                int id = data.getInt(data.getColumnIndexOrThrow("ID")); // Reemplaza "ID" con el nombre real de tu columna
                String username = data.getString(data.getColumnIndexOrThrow("USERNAME"));
                String mail = data.getString(data.getColumnIndexOrThrow("MAIL"));
                String password = data.getString(data.getColumnIndexOrThrow("PASSWORD"));
                int isAdmin = data.getInt(data.getColumnIndexOrThrow("ISADMIN"));

                // Agregar los valores a la lista
                dataList.add(String.valueOf(id));
                dataList.add(username);
                dataList.add(mail);
                dataList.add(password);
                dataList.add(isAdmin == 1 ? "Si" : "No");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Maneja la excepción de manera apropiada en tu aplicación
        } finally {
            if (data != null) {
                data.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return dataList;
    }
    public Integer encuentraID(String mail) {
        Integer id = null;

        try (SQLiteDatabase db = getWritableDatabase()) {
            Cursor data = db.rawQuery("SELECT ID FROM " + USER + " WHERE MAIL = ?", new String[]{mail});

            // Verificar si hay al menos un resultado antes de intentar obtener el ID
            if (data.moveToFirst()) {
                id = data.getInt(data.getColumnIndex("ID"));
            }

            // Cerrar el cursor (se cierra automáticamente con el bloque try-with-resources)
        } catch (SQLiteException e) {
            // Manejar excepciones relacionadas con la base de datos
            e.printStackTrace();
        }

        return id;
    }

    public String encuentraPass(String mail){
        String pass = null;
        try (SQLiteDatabase db = getWritableDatabase()) {
            Cursor data = db.rawQuery("SELECT PASSWORD FROM " + USER + " WHERE MAIL = ?", new String[]{mail});

            // Verificar si hay al menos un resultado antes de intentar obtener el ID
            if (data.moveToFirst()) {
                pass = data.getString(data.getColumnIndex("PASSWORD"));
            }

            // Cerrar el cursor (se cierra automáticamente con el bloque try-with-resources)
        } catch (SQLiteException e) {
            // Manejar excepciones relacionadas con la base de datos
            e.printStackTrace();
        }

        return pass;
    }


    public Integer checkId(){
        Integer id = 1;
        Boolean reached = true; // Cambiado a true para que el bucle se ejecute al menos una vez
        SQLiteDatabase sqLiteDatabasea = getWritableDatabase();

        while (reached){
            Cursor cursor = sqLiteDatabasea.rawQuery("SELECT * FROM " + USER + " WHERE ID = " + id, null);

            if (cursor.getCount() == 0) {
                reached = false; // Cambiado a false para salir del bucle
                return id;
            } else {
                id = id + 1;
            }

        }
        return null;
    }

    public boolean checkMail(String mail) {
        boolean mailExists = false;

        try (SQLiteDatabase db = getWritableDatabase()) {
            Cursor data = db.rawQuery("SELECT MAIL FROM " + USER + " WHERE MAIL = ?", new String[]{mail});

            // Verificar si hay al menos un resultado
            if (data.moveToFirst()) {
                // El correo electrónico existe en la base de datos
                mailExists = true;
            }

            // Cerrar el cursor (se cierra automáticamente con el bloque try-with-resources)
        } catch (SQLiteException e) {
            // Manejar excepciones relacionadas con la base de datos
            e.printStackTrace();
        }

        return mailExists;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        //sql
        try {
            db.execSQL("CREATE TABLE "+ USER +" (" +
                    "ID INTEGER PRIMARY KEY, " +
                    "USERNAME TEXT UNIQUE NOT NULL, " +
                    "MAIL TEXT UNIQUE NOT NULL, " +
                    "PASSWORD TEXT NOT NULL, " +
                    "ISADMIN INTEGER CHECK (ISADMIN IN (0, 1)), " +
                    "ISBLOCK INTEGER CHECK (ISBLOCK IN (0,1)))"
            );

            // Insertar un usuario administrador por defecto

            ContentValues contentValues = new ContentValues();
            contentValues.put("USERNAME", "admin");
            contentValues.put("MAIL", "admin@gmail.com");
            contentValues.put("PASSWORD", "admin123");
            contentValues.put("ISADMIN", 1);
            contentValues.put("ISBLOCK", 0);

            db.insert(USER, null, contentValues);

        } catch (SQLException e){
            Log.e("App", "Error al insertar datos: " + e.getMessage());
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ USER);
    }
}
