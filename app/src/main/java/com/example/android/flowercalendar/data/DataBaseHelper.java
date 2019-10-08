package com.example.android.flowercalendar.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.flowercalendar.data.Contract.PeriodDataEntry;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "date.db";
    private static final int DATABASE_VERSION = 1;

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
                + Contract.EventDataEntry.TABLE_NAME + " ("
                + Contract.EventDataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.EventDataEntry.COLUMN_MESSAGE + " TEXT NOT NULL, "
                + Contract.EventDataEntry.COLUMN_REMINDER + " TEXT NOT NULL, "
                + Contract.EventDataEntry.COLUMN_END + "TEXT );";

        db.execSQL(SQL_CREATE_PERIOD_TABLE);
        db.execSQL(SQL_CREATE_EVENT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }


}
