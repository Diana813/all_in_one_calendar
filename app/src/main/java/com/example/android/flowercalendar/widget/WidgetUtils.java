package com.example.android.flowercalendar.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class WidgetUtils {

    public static void updateWidget(Context context) {

        Intent intent = new Intent(context, CalendarWidgetProvider.class);
        intent.setAction("UPDATE_WIDGET_IF_DATA_CHANGED");

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidget = new ComponentName(context, CalendarWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 432, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;

        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime zdt = now.atZone(ZoneId.systemDefault());
        long millis = zdt.toInstant().toEpochMilli();
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, millis,
                pendingIntent);

    }

    public static void updateWidgetAtMidnight(Context context) {

        Intent intent = new Intent(context, CalendarWidgetProvider.class);
        intent.setAction("UPDATE_WIDGET_AT_MIDNIGHT");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidget = new ComponentName(context, CalendarWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 234, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;

        LocalDate todayMidn = LocalDate.now().plusDays(1);
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDateTime todayMidnight = LocalDateTime.of(todayMidn, midnight);
        ZonedDateTime zdt = todayMidnight.atZone(ZoneId.systemDefault());
        long millis = zdt.toInstant().toEpochMilli();
        alarmManager.setRepeating(AlarmManager.RTC, millis, 86400000,
                pendingIntent);
    }
}
