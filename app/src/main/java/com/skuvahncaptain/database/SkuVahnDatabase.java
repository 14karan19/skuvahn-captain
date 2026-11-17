package com.skuvahncaptain.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.skuvahncaptain.module.auth.splash.CheckVersionModel;

@Database(entities = {CheckVersionModel.Data.class}, version = 1, exportSchema = false)
public abstract class SkuVahnDatabase extends RoomDatabase {

    private static final String DB_NAME = "Crystal.db";
    private static volatile SkuVahnDatabase instance;

    public static synchronized SkuVahnDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static SkuVahnDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                SkuVahnDatabase.class,
                DB_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }

}
