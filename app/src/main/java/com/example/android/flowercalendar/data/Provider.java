package com.example.android.flowercalendar.data;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.data.Contract.PeriodDataEntry;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@SuppressLint("Registered")
public class Provider extends ContentProvider {

    private DataBaseHelper dataBaseHelper;

    public static final String LOG_TAG = Provider.class.getSimpleName();

    private static final int DATES = 100;

    private static final int DATES_ID = 101;

    private static final int EVENTS = 200;

    private static final int EVENTS_ID = 201;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_PERIOD_DATA, DATES);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_PERIOD_DATA + "/#", DATES_ID);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_EVENT_DATA, EVENTS);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_EVENT_DATA + "/#", EVENTS_ID);

    }


    @Override
    public boolean onCreate() {
        dataBaseHelper = new DataBaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = dataBaseHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match) {

            case DATES:
                cursor = database.query(PeriodDataEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case DATES_ID:
                selection = PeriodDataEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(PeriodDataEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case EVENTS:
                cursor = database.query(Contract.EventDataEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case EVENTS_ID:
                selection = Contract.EventDataEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(Contract.EventDataEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DATES:
                return PeriodDataEntry.CONTENT_LIST_TYPE;
            case DATES_ID:
                return PeriodDataEntry.CONTENT_ITEM_TYPE;
            case EVENTS:
                return Contract.EventDataEntry.CONTENT_LIST_TYPE;
            case EVENTS_ID:
                return Contract.EventDataEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DATES:
                assert values != null;
                return insertPeriodData(uri, values);
            case EVENTS:
                assert values != null;
                return insertEventData(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }
    private Uri insertPeriodData(Uri uri, ContentValues values) throws IllegalArgumentException {

        String startDate = values.getAsString(PeriodDataEntry.COLUMN_START_DATE);
        String periodLenght = values.getAsString(PeriodDataEntry.COLUMN_PERIOD_LENGHT);
        Integer cycleLenght = values.getAsInteger(String.valueOf(PeriodDataEntry.COLUMN_CYCLE_LENGHT));



        if (startDate == null) {
            throw new IllegalArgumentException(String.valueOf(R.string.No_start_date));
        }
        if (periodLenght == null) {
            throw new IllegalArgumentException(String.valueOf(R.string.No_period_lenght));
        }
        if (cycleLenght == null) {
            throw new IllegalArgumentException(String.valueOf(R.string.No_cycle_lenght));
        }

        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();

        long id = database.insert(PeriodDataEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertEventData(Uri uri, ContentValues values) throws IllegalArgumentException {

        String message = values.getAsString(Contract.EventDataEntry.COLUMN_MESSAGE);
        String reminder = values.getAsString(Contract.EventDataEntry.COLUMN_REMINDER);
        String end = values.getAsString(Contract.EventDataEntry.COLUMN_END);



        if (message == null) {
            throw new IllegalArgumentException(String.valueOf(R.string.No_message));
        }
        if (reminder == null) {
            throw new IllegalArgumentException(String.valueOf(R.string.No_reminder));
        }
        if (end == null) {
            throw new IllegalArgumentException(String.valueOf(R.string.No_end));
        }

        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();

        long id = database.insert(Contract.EventDataEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }



    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DATES:

                rowsDeleted = database.delete(PeriodDataEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case DATES_ID:

                selection = PeriodDataEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(PeriodDataEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case EVENTS:

                rowsDeleted = database.delete(Contract.EventDataEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case EVENTS_ID:

                selection = Contract.EventDataEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(Contract.EventDataEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DATES:
                assert values != null;
                return updatePeriodData(uri, values, selection, selectionArgs);
            case DATES_ID:
                selection = PeriodDataEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                assert values != null;
                return updatePeriodData(uri, values, selection, selectionArgs);

            case EVENTS:
                assert values != null;
                return updateEventData(uri, values, selection, selectionArgs);

            case EVENTS_ID:
                selection = Contract.EventDataEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                assert values != null;
                return updateEventData(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    private int updatePeriodData(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(PeriodDataEntry.COLUMN_START_DATE)) {
            String startDate = values.getAsString(PeriodDataEntry.COLUMN_START_DATE);
            if (startDate == null) {
                throw new IllegalArgumentException(String.valueOf(R.string.No_start_date));
            }
        }

        if (values.containsKey(PeriodDataEntry.COLUMN_PERIOD_LENGHT)) {
            String periodLenght = values.getAsString(PeriodDataEntry.COLUMN_PERIOD_LENGHT);
            if (periodLenght == null) {
                throw new IllegalArgumentException(String.valueOf(R.string.No_period_lenght));
            }
        }

        if (values.containsKey(PeriodDataEntry.COLUMN_CYCLE_LENGHT)) {
            String cycleLengh = values.getAsString(PeriodDataEntry.COLUMN_CYCLE_LENGHT);
            if (cycleLengh == null) {
                throw new IllegalArgumentException(String.valueOf(R.string.No_cycle_lenght));
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();

        int rowsUpdated = database.update(PeriodDataEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
    private int updateEventData(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(Contract.EventDataEntry.COLUMN_MESSAGE)) {
            String message = values.getAsString(Contract.EventDataEntry.COLUMN_MESSAGE);
            if (message == null) {
                throw new IllegalArgumentException(String.valueOf(R.string.No_message));
            }
        }

        if (values.containsKey(Contract.EventDataEntry.COLUMN_REMINDER)) {
            String reminder = values.getAsString(Contract.EventDataEntry.COLUMN_REMINDER);
            if (reminder == null) {
                throw new IllegalArgumentException(String.valueOf(R.string.No_reminder));
            }
        }

        if (values.containsKey(Contract.EventDataEntry.COLUMN_END)) {
            String end = values.getAsString(Contract.EventDataEntry.COLUMN_END);
            if (end == null) {
                throw new IllegalArgumentException(String.valueOf(R.string.No_end));
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();

        int rowsUpdated = database.update(Contract.EventDataEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
