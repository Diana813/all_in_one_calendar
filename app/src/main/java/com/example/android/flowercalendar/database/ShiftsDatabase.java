package com.example.android.flowercalendar.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Shift.class}, version = 1)
public abstract class ShiftsDatabase extends RoomDatabase {

    private static ShiftsDatabase INSTANCE;
    private static final String DB_NAME = "shift.db";

    public static ShiftsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ShiftsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ShiftsDatabase.class, DB_NAME)
                            .allowMainThreadQueries()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Log.d("ShiftDatabase", "populating with data...");
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

    public abstract ShiftsDao shiftsDao();


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final ShiftsDao shiftsDao;

        public PopulateDbAsync(ShiftsDatabase instance) {
            shiftsDao = instance.shiftsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            shiftsDao.deleteAll();
            return null;
        }
    }
}