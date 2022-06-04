package com.example.kocheng;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String database_name = "kocheng.db";
    public static final String table_name = "cing";

    public static final String row_id = "_id";
    public static final String namapemilik = "namapemilik";
    public static final String nohp = "nohp";
    public static final String namakucing = "namakucing";
    public static final String beratkucing = "beratkucing";
    public static final String jeniskucing = "jeniskucing";
    public static final String tanggaltitip = "tanggaltitip";
    public static final String tanggalngambil = "tanggalngambil";
    public static final String foto = "foto";
    public static final String pesan = "pesan";

    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, database_name, null, 2);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE session(id integer PRIMARY KEY, login text)");
        db.execSQL("CREATE TABLE user(id integer PRIMARY KEY AUTOINCREMENT, username text, password text)");
        db.execSQL("INSERT INTO session(id, login) VALUES(1, 'kosong')");
        String sql = "create table cing(_id integer PRIMARY KEY AUTOINCREMENT, namapemilik text, nohp text, namakucing text, beratkucing text, jeniskucing text, tanggaltitip text, tanggalngambil text, foto text, pesan text);";

        db.execSQL(sql);

        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS session");
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }


    //Get All SQLite Data
    public Cursor allData(){
        Cursor cur = db.rawQuery("SELECT * FROM " + table_name, null);
        return cur;
    }

    //Get 1 Data By ID
    public Cursor oneData(Long id){
        Cursor cur = db.rawQuery("SELECT * FROM " + table_name + " WHERE " + row_id + "=" + id, null);
        return cur;
    }

    //Insert Data to Database
    public void insertData(ContentValues values){
        db.insert(table_name, null, values);
    }

    //Update Data
    public void updateData(ContentValues values, long id){
        db.update(table_name, values, row_id + "=" + id, null);
    }

    //Delete Data
    public void deleteData(long id){
        db.delete(table_name, row_id + "=" + id, null);
    }

    //check session
    public Boolean checkSession(String sessionValues) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM session WHERE login = ?", new String[]{sessionValues});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    //upgrade session
    public Boolean upgradeSession(String sessionValues, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("login", sessionValues);
        long update = db.update("session", contentValues, "id =" + id, null);
        if (update == -1) {
            return false;
        } else {
            return true;
        }
    }

    //insert user
    public Boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long insert = db.insert("user", null, contentValues);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    //check login
    public Boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user WHERE username = ? AND Password = ?", new String[]{username, password});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
}