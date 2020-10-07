package com.dianaszczepankowska.AllInOneCalendar.android.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.dianaszczepankowska.AllInOneCalendar.android.alarm.AlarmReceiver;
import com.dianaszczepankowska.AllInOneCalendar.android.alarm.Notification;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Shift;

import java.security.cert.CertPathValidatorException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class AlarmUtils {

    private static long uniqueRequestCodeForEachAlarm(LocalDate pickedDate, String alarmHour, String alarmMinute) {

        String findId;
        if (alarmHour != null & pickedDate != null) {
            int day = pickedDate.getDayOfMonth();
            int month = pickedDate.getMonthValue();
            int year = pickedDate.getYear();

            findId = day + "" + month + "" + year + "" + alarmHour + "" + alarmMinute;
        } else {
            findId = "-52";
        }

        return Long.parseLong(findId);
    }

    public static void setAlarmToPickedDay(String hour, String minutes, LocalDate pickedDate, Context context, String action, String eventName) {

        if (uniqueRequestCodeForEachAlarm(pickedDate, hour, minutes) == -52) {
            return;
        }

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(action);
        intent.putExtra("title", eventName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, (int) uniqueRequestCodeForEachAlarm(pickedDate, hour, minutes), intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, pickedDateToMilis(hour, minutes, pickedDate),
                pendingIntent);

    }

    public static void setCyclicalNotification(String hour, String minutes, LocalDate pickedDate, Context context, String action, long intervalMilis, String eventName) {

        if (uniqueRequestCodeForEachAlarm(pickedDate, hour, minutes) == -52) {
            return;
        }

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(action);
        intent.putExtra("title", eventName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, (int) uniqueRequestCodeForEachAlarm(pickedDate, hour, minutes), intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, pickedDateToMilis(hour, minutes, pickedDate), intervalMilis,
                pendingIntent);

    }

    public static void setUpcomingEventNotification(String hour, String minutes, LocalDate pickedDate, Context context, String eventName) {

        if (uniqueRequestCodeForEachAlarm(pickedDate, hour, minutes) == -52) {
            return;
        }

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(Notification.ACTION_OPEN_NOTIFICATION_CLASS);
        intent.putExtra("title", eventName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, (int) uniqueRequestCodeForEachAlarm(pickedDate, hour, minutes), intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, pickedDateToMilis(hour, minutes, pickedDate),
                pendingIntent);
    }


    public static void deleteAlarmFromAPickedDay(LocalDate pickedDate, String alarmHour, String alarmMinute, Context context, String action) {

        if (uniqueRequestCodeForEachAlarm(pickedDate, alarmHour, alarmMinute) == -52) {
            return;
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, (int) uniqueRequestCodeForEachAlarm(pickedDate, alarmHour, alarmMinute), intent,
                0);

        assert alarmManager != null;
        if (uniqueRequestCodeForEachAlarm(pickedDate, alarmHour, alarmMinute) != -1) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private static long pickedDateToMilis(String hour, String minutes, LocalDate pickedDate) {

        long pickedDateToMillis = -1;
        if (hour != null) {
            LocalDateTime ldt = LocalDateTime.of(pickedDate.getYear(), pickedDate.getMonthValue(), pickedDate.getDayOfMonth(), Integer.parseInt(hour), Integer.parseInt(minutes), 0);
            ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
            pickedDateToMillis = zdt.toInstant().toEpochMilli();
        }
        return pickedDateToMillis;

    }

    public static String getAlarmHour(Shift shift) {

        if (shift != null) {
            String alarm = shift.getAlarm();
            if (!alarm.equals("")) {
                String[] split = alarm.split(":");
                return split[0];
            }
        }
        return null;
    }

    public static String getAlarmMinute(Shift shift) {

        if (shift != null) {
            String alarm = shift.getAlarm();
            if (!alarm.equals("")) {
                String[] split = alarm.split(":");
                return split[1];
            }
        }
        return null;
    }

    public static String getAlarmHour(Event event) {

        if (event != null) {
            String alarm = event.getAlarm();
            if (alarm != null && !alarm.equals("")) {
                String[] split = alarm.split(":");
                return split[0];
            }
        }
        return null;
    }

    public static String getAlarmMinute(Event event) {

        if (event != null) {
            String alarm = event.getAlarm();
            if (alarm != null && !alarm.equals("")) {
                String[] split = alarm.split(":");
                return split[1];
            }
        }
        return null;
    }

    public static String getAlarmHour(String eventAlarm) {

        if (eventAlarm != null && !eventAlarm.equals("")) {
            String[] split = eventAlarm.split(":");
            return split[0];

        }
        return null;
    }

    public static String getAlarmMinute(String eventAlarm) {

        if (eventAlarm != null && !eventAlarm.equals("")) {
            String[] split = eventAlarm.split(":");
            return split[1];

        }
        return null;
    }

}
