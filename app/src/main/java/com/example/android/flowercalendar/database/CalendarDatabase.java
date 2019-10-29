package com.example.android.flowercalendar.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {CalendarEvents.class, Colors.class, Shift.class}, version = 3)
public abstract class CalendarDatabase extends RoomDatabase {

    private static CalendarDatabase INSTANCE;
    private static final String DB_NAME = "calendar.db";

    public static CalendarDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CalendarDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CalendarDatabase.class, DB_NAME)
                            .allowMainThreadQueries()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Log.d("CalendarDatabase", "populating with data...");
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

    public abstract CalendarEventsDao calendarEventsDao();


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final CalendarEventsDao calendarEventsDao;

        public PopulateDbAsync(CalendarDatabase instance) {
            calendarEventsDao = instance.calendarEventsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            calendarEventsDao.deleteAll();
            return null;
        }
    }

}
