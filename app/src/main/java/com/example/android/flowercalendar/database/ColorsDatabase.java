package com.example.android.flowercalendar.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Colors.class}, version = 2)
public abstract class ColorsDatabase extends RoomDatabase {

    private static ColorsDatabase INSTANCE;
    private static final String DB_NAME = "colors.db";

    public static ColorsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ColorsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ColorsDatabase.class, DB_NAME)
                            .allowMainThreadQueries()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Log.d("ColorsDatabase", "populating with data...");
                                    new PopulateDbAsync(INSTANCE).execute();
                                }
                            })
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    public void clearDb() {
        if (INSTANCE != null) {
            new PopulateDbAsync(INSTANCE).execute();
        }
    }

    public abstract ColorsDao colorsDao();


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final ColorsDao colorsDao;

        public PopulateDbAsync(ColorsDatabase instance) {
            colorsDao = instance.colorsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            colorsDao.deleteAll();
            return null;
        }
    }
}

