package com.dianaszczepankowska.AllInOneCalendar.android.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {CalendarEvents.class, Shift.class, PeriodData.class, Event.class, ImagePath.class, BigPlanData.class, StatisticsPersonalGrowth.class}, version = 20)
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
                            .addMigrations(MIGRATION_12_13, MIGRATION_13_14, MIGRATION_15_16, MIGRATION_16_17, MIGRATION_17_18, MIGRATION_18_19, MIGRATION_19_20)
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

    public abstract ShiftsDao shiftsDao();

    public abstract PeriodDataDao periodDataDao();

    public abstract EventsDao eventsDao();

    public abstract ImagePathDao imagePathDao();

    public abstract BigPlanDao bigPlanDao();

    public abstract StatisticsPersonalGrowthDao statisticsPersonalGrowthDao();


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final CalendarEventsDao calendarEventsDao;
        private final ShiftsDao shiftsDao;
        private final PeriodDataDao periodDataDao;
        private final EventsDao eventsDao;
        private final ImagePathDao imagePathDao;
        private final BigPlanDao bigPlanDao;
        private final StatisticsPersonalGrowthDao statisticsPersonalGrowthDao;

        PopulateDbAsync(CalendarDatabase instance) {
            calendarEventsDao = instance.calendarEventsDao();
            shiftsDao = instance.shiftsDao();
            periodDataDao = instance.periodDataDao();
            eventsDao = instance.eventsDao();
            imagePathDao = instance.imagePathDao();
            bigPlanDao = instance.bigPlanDao();
            statisticsPersonalGrowthDao = instance.statisticsPersonalGrowthDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            calendarEventsDao.deleteAll();
            shiftsDao.deleteAll();
            periodDataDao.deleteAll();
            eventsDao.deleteAll();
            imagePathDao.deleteAll();
            statisticsPersonalGrowthDao.deleteAll();
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

    private static final Migration MIGRATION_15_16 = new Migration(15, 16) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE new_Event (" +
                    "id INTEGER PRIMARY KEY NOT NULL," +
                    "event_name TEXT," +
                    "schedule TEXT," +
                    "alarm TEXT," +
                    "event_length INTEGER NOT NULL," +
                    "position INTEGER NOT NULL, " +
                    "picked_day TEXT, " +
                    "eventKind INTEGER NOT NULL, " +
                    "frequency TEXT, " +
                    "term TEXT)");
            database.execSQL("CREATE INDEX index_new_Event_event_name ON  new_Event(event_name)");
            database.execSQL("INSERT INTO new_Event (id, event_name, schedule, alarm, event_length, position, picked_day, eventKind, frequency, term) " +
                    "SELECT id, event_name, schedule, alarm, event_length, position, picked_day, eventKind, frequency, term FROM Event");
            database.execSQL("DROP TABLE Event");
            database.execSQL("ALTER TABLE new_Event RENAME TO Event");
        }
    };


    private static final Migration MIGRATION_16_17 = new Migration(16, 17) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE big_plan_data ADD COLUMN startDate TEXT");
        }
    };

    private static final Migration MIGRATION_17_18 = new Migration(17, 18) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

            database.execSQL("CREATE TABLE `statistics_personal_growth` (id INTEGER NOT NULL, "
                    + "aim_time INTEGER NOT NULL, " + " effectiveness INTEGER NOT NULL " + ", PRIMARY KEY(`id`))");
        }
    };

    private static final Migration MIGRATION_18_19 = new Migration(18, 19) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE statistics_personal_growth ADD COLUMN dateOfAdding TEXT");
        }
    };

    private static final Migration MIGRATION_19_20 = new Migration(19, 20) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "DROP table Colors");
        }
    };
}
