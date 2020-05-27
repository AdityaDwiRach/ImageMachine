package com.adr.imagemachine.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = MachineDataEntity.class, version = 1)
public abstract class MachineDatabase extends RoomDatabase {

    private static final String DB_NAME = "machine_data_db";
    private static MachineDatabase instance;

    public static synchronized MachineDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), MachineDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract MachineDataDAO machineDataDAO();
}
