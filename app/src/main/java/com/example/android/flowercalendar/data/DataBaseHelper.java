package com.example.android.flowercalendar.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.flowercalendar.data.Contract.PeriodDataEntry;
import com.example.android.flowercalendar.data.Contract.EventDataEntry;
import com.example.android.flowercalendar.data.Contract.ColorDataEntry;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "date.db";
    private static final int DATABASE_VERSION = 3;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_PERIOD_TABLE = "CREATE TABLE "
                + PeriodDataEntry.TABLE_NAME + " ("
                + PeriodDataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PeriodDataEntry.COLUMN_START_DATE + " TEXT, "
                + PeriodDataEntry.COLUMN_PERIOD_LENGHT + " INTEGER, "
                + PeriodDataEntry.COLUMN_CYCLE_LENGHT + " INTEGER );";

        String SQL_CREATE_EVENT_TABLE = "CREATE TABLE "
                + EventDataEntry.TABLE_NAME + " ("
                + EventDataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EventDataEntry.COLUMN_MESSAGE + " TEXT NOT NULL, "
                + EventDataEntry.COLUMN_REMINDER + " TEXT NOT NULL, "
                + EventDataEntry.COLUMN_END + "TEXT );";

        String SQL_CREATE_COLOR_TABLE = "CREATE TABLE "
                + ColorDataEntry.TABLE_NAME + " ("
                + ColorDataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ColorDataEntry.COLOR_NUMBER + " INTEGER );";

        db.execSQL(SQL_CREATE_PERIOD_TABLE);
        db.execSQL(SQL_CREATE_EVENT_TABLE);
        db.execSQL(SQL_CREATE_COLOR_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + PeriodDataEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EventDataEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ColorDataEntry.TABLE_NAME);

        onCreate(db);
    }
}
