package com.example.android.flowercalendar.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {CalendarEvents.class, Colors.class, Shift.class, PeriodData.class, Event.class, ImagePath.class, BigPlanData.class}, version = 15)
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
                            .addMigrations(MIGRATION_12_13, MIGRATION_13_14)
                            //.fallbackToDestructiveMigration()
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

    public abstract ColorsDao colorsDao();

    public abstract ShiftsDao shiftsDao();

    public abstract PeriodDataDao periodDataDao();

    public abstract EventsDao eventsDao();

    public abstract ImagePathDao imagePathDao();

    public abstract BigPlanDao bigPlanDao();


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final CalendarEventsDao calendarEventsDao;
        private final ColorsDao colorsDao;
        private final ShiftsDao shiftsDao;
        private final PeriodDataDao periodDataDao;
        private final EventsDao eventsDao;
        private final ImagePathDao imagePathDao;
        private final BigPlanDao bigPlanDao;

        PopulateDbAsync(CalendarDatabase instance) {
            calendarEventsDao = instance.calendarEventsDao();
            colorsDao = instance.colorsDao();
            shiftsDao = instance.shiftsDao();
            periodDataDao = instance.periodDataDao();
            eventsDao = instance.eventsDao();
            imagePathDao = instance.imagePathDao();
            bigPlanDao = instance.bigPlanDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            calendarEventsDao.deleteAll();
            colorsDao.deleteAll();
            shiftsDao.deleteAll();
            periodDataDao.deleteAll();
            eventsDao.deleteAll();
            imagePathDao.deleteAll();
            return null;
        }
    }

    private static final Migration MIGRATION_12_13 = new Migration(12, 13) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE EVENT ADD COLUMN frequency TEXT");
        }
    };

    private static final Migration MIGRATION_13_14 = new Migration(13, 14) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE EVENT ADD COLUMN term TEXT");
        }
    };


}
