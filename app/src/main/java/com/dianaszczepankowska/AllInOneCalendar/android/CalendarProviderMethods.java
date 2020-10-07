package com.dianaszczepankowska.AllInOneCalendar.android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Reminders;
import android.view.View;

import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView.BackgroundActivityExpandedDayView;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import androidx.core.app.ActivityCompat;

import static android.provider.CalendarContract.Attendees;
import static android.provider.CalendarContract.Events;

public class CalendarProviderMethods extends BackgroundActivityExpandedDayView {
    private static final int MY_CAL_WRITE_REQ = 1;
    private static final int MY_CAL_REQ = 2;


    public static void addEventToGoogleCalendar(Context context, LocalDate startDate, String schedule, String title, String description, int duration, String eventId, String ownerEmail) {

        if (startDate == null) {
            return;
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }

        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();


        String eventHour;
        String eventMinute;
        Calendar beginTime;
        Calendar endTime;

        if (schedule != null && !schedule.equals("")) {
            String[] split = schedule.split(":");
            eventHour = split[0];
            eventMinute = split[1];
            beginTime = Calendar.getInstance();
            beginTime.set(startDate.getYear(), startDate.getMonthValue() - 1, startDate.getDayOfMonth(), Integer.parseInt(eventHour), Integer.parseInt(eventMinute));

            if (duration != 0) {
                LocalTime time = LocalTime.of(Integer.parseInt(eventHour), Integer.parseInt(eventMinute));
                LocalDateTime startTime = LocalDateTime.of(startDate, time);
                LocalDateTime end = startTime.plusHours(duration);
                endTime = Calendar.getInstance();
                endTime.set(startDate.getYear(), startDate.getMonthValue() - 1, startDate.getDayOfMonth(), end.getHour(), end.getMinute());
            } else {
                endTime = Calendar.getInstance();
                endTime.set(startDate.getYear(), startDate.getMonthValue() - 1, startDate.getDayOfMonth());
            }

            values.put(Events.DTSTART, String.valueOf(beginTime.getTimeInMillis()));
            values.put(Events.DTEND, String.valueOf(endTime.getTimeInMillis()));

        } else {
            beginTime = Calendar.getInstance();
            beginTime.set(startDate.getYear(), startDate.getMonthValue() - 1, startDate.getDayOfMonth());
            endTime = Calendar.getInstance();
            endTime.set(startDate.getYear(), startDate.getMonthValue() - 1, startDate.getDayOfMonth());
            values.put(Events.DTSTART, String.valueOf(beginTime.getTimeInMillis()));
            values.put(Events.DTEND, String.valueOf(endTime.getTimeInMillis()));
            values.put(Events.ALL_DAY, 1);
        }

        values.put(Events._ID, eventId);
        values.put(Events.TITLE, title);
        values.put(Events.DESCRIPTION, description);
        values.put(Events.CALENDAR_ID, getCalendarId(context, ownerEmail));
        values.put(Events.EVENT_COLOR, 0x5c007a);
        values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        values.put(Events.EVENT_LOCATION, Locale.getDefault().getCountry());
        values.put(Events.GUESTS_CAN_INVITE_OTHERS, "1");
        values.put(Events.GUESTS_CAN_SEE_GUESTS, "1");
        values.put(Events.HAS_ALARM, 0);

        cr.insert(Events.CONTENT_URI, values);
        //addReminder(eventId, context);
    }

    public static void addAttendee(String eventId, Context context, String name, String email) {

        if (eventId == null) {
            return;
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Attendees.ATTENDEE_NAME, name);
        values.put(Attendees.ATTENDEE_EMAIL, email);
        values.put(Attendees.ATTENDEE_RELATIONSHIP, Attendees.RELATIONSHIP_ATTENDEE);
        values.put(Attendees.ATTENDEE_TYPE, Attendees.TYPE_OPTIONAL);
        values.put(Attendees._ID, eventId);

        cr.insert(Attendees.CONTENT_URI, values);
    }

    public static void addReminder(String eventID, Context context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }

        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Reminders.MINUTES, 15);
        values.put(Reminders.EVENT_ID, eventID);
        values.put(Reminders.METHOD, Reminders.METHOD_ALERT);
        cr.insert(Reminders.CONTENT_URI, values);
    }


    @SuppressLint({"Recycle", "DefaultLocale"})
    public static void getDataFromEventTable(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_CALENDAR}, MY_CAL_REQ);
        }
        Cursor cursor;
        ContentResolver cr = context.getContentResolver();

        String[] mProjection =
                {
                        "_id",
                        Events.TITLE,
                        Events.EVENT_LOCATION,
                        Events.DTSTART,
                        Events.DTEND,
                };

        Uri uri = Events.CONTENT_URI;

        cursor = cr.query(uri, mProjection, null, null, null);

        assert cursor != null;
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(Events.TITLE));
            String pickedDateInMilis = cursor.getString(cursor.getColumnIndex(Events.DTSTART));
            LocalDate pickedDate = Instant.ofEpochMilli(Long.parseLong(pickedDateInMilis)).atZone(ZoneId.systemDefault()).toLocalDate();

            // UtilsEvents.saveDataEvents(null, String.valueOf(pickedDate), title, "-1", "", 1);
        }

    }


    @SuppressLint("Recycle")
    public static void updateEvent(Context context, LocalDate newStartDate, String newSchedule, String newTitle, int newDuration, String newDescription, String eventId) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }
        if (eventId == null) {
            return;
        }


        String eventHour;
        String eventMinute;
        Calendar beginTime;
        Calendar endTime;


        if (newSchedule != null && !newSchedule.equals("")) {
            String[] split = newSchedule.split(":");
            eventHour = split[0];
            eventMinute = split[1];
            beginTime = Calendar.getInstance();
            beginTime.set(newStartDate.getYear(), newStartDate.getMonthValue() - 1, newStartDate.getDayOfMonth(), Integer.parseInt(eventHour), Integer.parseInt(eventMinute));

            if (newDuration != 0) {
                LocalTime time = LocalTime.of(Integer.parseInt(eventHour), Integer.parseInt(eventMinute));
                LocalDateTime startTime = LocalDateTime.of(newStartDate, time);
                LocalDateTime end = startTime.plusHours(newDuration);
                endTime = Calendar.getInstance();
                endTime.set(newStartDate.getYear(), newStartDate.getMonthValue() - 1, newStartDate.getDayOfMonth(), end.getHour(), end.getMinute());
            } else {
                endTime = Calendar.getInstance();
                endTime.set(newStartDate.getYear(), newStartDate.getMonthValue() - 1, newStartDate.getDayOfMonth());
            }

        } else {
            beginTime = Calendar.getInstance();
            beginTime.set(newStartDate.getYear(), newStartDate.getMonthValue() - 1, newStartDate.getDayOfMonth());
            endTime = Calendar.getInstance();
            endTime.set(newStartDate.getYear(), newStartDate.getMonthValue() - 1, newStartDate.getDayOfMonth());
        }


        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, String.valueOf(beginTime.getTimeInMillis()));
        values.put(Events.DTEND, String.valueOf(endTime.getTimeInMillis()));
        values.put(Events.TITLE, newTitle);
        values.put(Events.DESCRIPTION, newDescription);
        values.put(Events.GUESTS_CAN_INVITE_OTHERS, "1");
        values.put(Events.GUESTS_CAN_SEE_GUESTS, "1");


        Uri uri = Events.CONTENT_URI;
        String mSelectionClause = Events._ID + " = ?";
        String[] mSelectionArgs = {eventId};

        //check if the event exist
        Uri event = ContentUris.withAppendedId(uri, Long.parseLong(eventId));
        Cursor cursor = context.getContentResolver().query(event, null, null, null);
        assert cursor != null;
        if (cursor.getCount() == 1) {
            //update event
            context.getContentResolver().update(uri, values, mSelectionClause, mSelectionArgs);
        }

    }

    @SuppressLint("Recycle")
    public static void deleteEvent(Context context, String eventId) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }
        if (eventId == null) {
            return;
        }


        Uri uri = Events.CONTENT_URI;

        String mSelectionClause = Events._ID + " = ?";
        String[] mSelectionArgs = {eventId};

        //check if the event exist
        Uri event = ContentUris.withAppendedId(uri, Long.parseLong(eventId));
        Cursor cursor = context.getContentResolver().query(event, null, null, null);
        assert cursor != null;
        if (cursor.getCount() == 1) {
            //delete event
            context.getContentResolver().delete(uri, mSelectionClause, mSelectionArgs);
        }
    }


    public void updateAttendee(View view) {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(Attendees.ATTENDEE_TYPE, Attendees.TYPE_OPTIONAL);

        Uri uri = Attendees.CONTENT_URI;

        String mSelectionClause = Attendees.ATTENDEE_STATUS + " = ? AND ";
        mSelectionClause = mSelectionClause + Attendees.EVENT_ID + " = ?";
        String[] mSelectionArgs = {"" + Attendees.ATTENDEE_STATUS_INVITED, "1"};

        int updCount = getContext().getContentResolver().update(uri, contentValues, mSelectionClause, mSelectionArgs);

    }

    public void deleteAttendee(View view) {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }

        Uri uri = Attendees.CONTENT_URI;

        String mSelectionClause = Attendees.ATTENDEE_STATUS + " = ?";
        String[] mSelectionArgs = {"" + Attendees.ATTENDEE_STATUS_DECLINED};

        int updCount = getContext().getContentResolver().delete(uri, mSelectionClause, mSelectionArgs);

    }

    @SuppressLint("Recycle")
    public static long getCalendarId(Context context, String ownerEmail) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_CALENDAR}, MY_CAL_REQ);
        }

        String[] projection = new String[]{
                Calendars._ID,
                Calendars.ACCOUNT_NAME};
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(Uri.parse("content://com.android.calendar/calendars"), projection,
                Calendars.ACCOUNT_NAME + "=? and (" +
                        Calendars.NAME + "=? or " +
                        Calendars.CALENDAR_DISPLAY_NAME + "=?)",
                new String[]{ownerEmail, ownerEmail,
                        ownerEmail}, null);

        assert cursor != null;
        if (cursor.moveToFirst()) {

            if (cursor.getString(1).equals(ownerEmail))
                return cursor.getInt(0);
        }
        return -1;

    }


}
