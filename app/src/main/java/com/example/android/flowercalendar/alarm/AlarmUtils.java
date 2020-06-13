package com.example.android.flowercalendar.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class AlarmUtils {

    private long uniqueRequestCodeForEachAlarm(LocalDate pickedDate, String alarmHour, String alarmMinute) {

        int day = pickedDate.getDayOfMonth();
        int month = pickedDate.getMonthValue();
        int year = pickedDate.getYear();

        String findId;
        if (alarmHour != null) {
            findId = day + "" + month + "" + year + "" + alarmHour + "" + alarmMinute;
        } else {
            findId = "-52";
        }

        return Long.parseLong(findId);
    }

    public void setAlarmToPickedDay(String hour, String minutes, LocalDate pickedDate, Context context, String action) {

        if (uniqueRequestCodeForEachAlarm(pickedDate, hour, minutes) == -52) {
            return;
        }

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, (int) uniqueRequestCodeForEachAlarm(pickedDate, hour, minutes), intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, pickedDateToMilis(hour, minutes, pickedDate),
                pendingIntent);

    }

    void setCyclicalNotification(String hour, String minutes, LocalDate pickedDate, Context context, String action, long intervalMilis, String eventName) {

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


    public void deleteAlarmFromAPickedDay(LocalDate pickedDate, String alarmHour, String alarmMinute, Context context, String action) {

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

    private long pickedDateToMilis(String hour, String minutes, LocalDate pickedDate) {

        long pickedDateToMillis = -1;
        if (hour != null) {
            LocalDateTime ldt = LocalDateTime.of(pickedDate.getYear(), pickedDate.getMonthValue(), pickedDate.getDayOfMonth(), Integer.parseInt(hour), Integer.parseInt(minutes), 0);
            ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
            pickedDateToMillis = zdt.toInstant().toEpochMilli();
        }
        return pickedDateToMillis;

    }

}
