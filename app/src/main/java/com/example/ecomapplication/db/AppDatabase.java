package com.example.ecomapplication.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Product.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase mInstance;

    public abstract CartDao cartDao();

    public static synchronized AppDatabase getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = Room.databaseBuilder(mCtx.getApplicationContext(),
                    AppDatabase.class, "product_database").build();
        }
        return mInstance;
    }
}
