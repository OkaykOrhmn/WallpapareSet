package com.example.wallpapareset.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.wallpapareset.models.model.Photo;

import java.util.ArrayList;

public class SqlFav extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "fav";

    public SqlFav(@Nullable Context context) {
        super(context, "mySql.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(" CREATE TABLE " + TABLE_NAME +
                "(id INTEGER PRIMARY KEY , " +
                " address INTEGER)" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public ArrayList<Photo> getData(){

        ArrayList<Photo> photos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (c.moveToFirst()){
            do {
                // Passing values
                int id = c.getInt(0);
                int address = c.getInt(1);
                Photo photo = new Photo(id,address);
                photos.add(photo);

                // Do something Here with values
            } while(c.moveToNext());
        }
        c.close();

        return photos;


    }

    public boolean getById(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("Select id from fav where id = ?", new String[]{id+""});
        if (c.moveToFirst()){
            return true;


        }
        c.close();

        return false;
    }

    public void delete(int id)
    {
        SQLiteDatabase database = this.getReadableDatabase();
        database.delete(TABLE_NAME, "id=?", new String[]{id+""});

    }

    public void Insert(Integer id, Integer address) {

        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("address", address);
        database.insert(TABLE_NAME, null, contentValues);

    }



}
