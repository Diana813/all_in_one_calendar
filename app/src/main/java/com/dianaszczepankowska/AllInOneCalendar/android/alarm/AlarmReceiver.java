package com.dianaszczepankowska.AllInOneCalendar.android.alarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarEvents;
import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarEventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Event;
import com.dianaszczepankowska.AllInOneCalendar.android.database.EventsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.database.Shift;
import com.dianaszczepankowska.AllInOneCalendar.android.database.ShiftsDao;
import com.dianaszczepankowska.AllInOneCalendar.android.events.cyclicalEvents.UpcomingCyclicalEvent;
import com.dianaszczepankowska.AllInOneCalendar.android.utils.AppUtils;

import java.time.LocalDate;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Intent.ACTION_BOOT_COMPLETED;
import static android.content.Intent.ACTION_MY_PACKAGE_REPLACED;
import static com.dianaszczepankowska.AllInOneCalendar.android.alarm.AlarmClock.ACTION_DISMISS;
import static com.dianaszczepankowska.AllInOneCalendar.android.alarm.AlarmClock.ACTION_OPEN_ALARM_CLASS;
import static com.dianaszczepankowska.AllInOneCalendar.android.alarm.Notification.ACTION_OPEN_NOTIFICATION_CLASS;
import static com.dianaszczepankowska.AllInOneCalendar.android.alarm.Notification.ACTION_SET_NOTIFICATION;
import static com.dianaszczepankowska.AllInOneCalendar.android.alarm.Notification.ACTION_STOP;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (ACTION_DISMISS.equals(action)) {
            dismissRingtone(context);
        } else if (ACTION_OPEN_ALARM_CLASS.equals(action)) {
            openAlarmClass(context);
        } else if (ACTION_STOP.equals(action)) {
            dismissNotification(context);
        } else if (ACTION_OPEN_NOTIFICATION_CLASS.equals(action)) {
            String notificationInfo = intent.getStringExtra("title");
            openNotificationClass(context, notificationInfo);
        } else if (ACTION_SET_NOTIFICATION.equals(action)) {
            String notificationInfo = intent.getStringExtra("title");
            setUpcomingEventNotyfication(context, notificationInfo);
        } else if (ACTION_BOOT_COMPLETED.equals(action)) {
            setAllTheNotifications(context);
            setAllTheAlarms(context);
        } else if (ACTION_MY_PACKAGE_REPLACED.equals((action))) {
            setAllTheNotifications(context);
            setAllTheAlarms(context);
        }

    }


    private void openAlarmClass(Context context) {

        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), AlarmClock.class.getName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(intent);
    }


    private void openNotificationClass(Context context, String notificationInfo) {

        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), Notification.class.getName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("title", notificationInfo);
        context.startService(intent);
    }


    public void dismissRingtone(Context context) {

        Intent i = new Intent(context, AlarmClock.class);
        context.stopService(i);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.cancel(321);

        cancelCurrentPendingIntent(context);
    }


    public void dismissNotification(Context context) {

        Intent i = new Intent(context, Notification.class);
        context.stopService(i);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.cancel(321);

        cancelCurrentPendingIntent(context);
    }


    public void cancelCurrentPendingIntent(Context context) {

        AlarmManager aManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        assert aManager != null;
        aManager.cancel(pendingIntent);
    }

    private void setUpcomingEventNotyfication(Context context, String notificationInfo) {

        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();
        List<Event> events = eventsDao.findAllByEventKindAndName(notificationInfo, 0);

        for (Event event : events) {
            String[] parts = event.getFrequency().split("-");
            String pickedDaysOfWeek = parts[3];
            String[] alarmParts = event.getAlarm().split(":");
            String alarmHour = alarmParts[0];
            String alarmMinutes = alarmParts[1];
            UpcomingCyclicalEvent.displayNextEvent(event.getPickedDay(), event.getFrequency(), pickedDaysOfWeek, event.getTerm(), context);
            AlarmUtils.setUpcomingEventNotification(alarmHour, alarmMinutes, AppUtils.refactorStringIntoDate(event.getPickedDay()), context, notificationInfo);
        }

    }

    private void setAllTheNotifications(Context context) {
        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();

        List<Event> cyclicalEvents = eventsDao.findByKind(0);

        for (Event event : cyclicalEvents) {
            CyclicalEventsNotifications.setNotification(event, context);
        }
    }

    private void setAllTheAlarms(Context context) {

        CalendarEventsDao calendarEventsDao = CalendarDatabase.getDatabase(context).calendarEventsDao();
        List<CalendarEvents> calendarEvents = calendarEventsDao.findAllEvents();

        for (CalendarEvents calendarEvent : calendarEvents) {
            if (calendarEvent.getShiftNumber() != null && !calendarEvent.getShiftNumber().equals("") && (AppUtils.refactorStringIntoDate(calendarEvent.getPickedDate()).isAfter(LocalDate.now()) || AppUtils.refactorStringIntoDate(calendarEvent.getPickedDate()).isEqual(LocalDate.now()))) {
                ShiftsDao shiftsDao = CalendarDatabase.getDatabase(context).shiftsDao();
                Shift shift = shiftsDao.findByShiftName(calendarEvent.getShiftNumber());
                AlarmUtils.getAlarmTime(shift);
                AlarmUtils.setAlarmToPickedDay(AlarmUtils.alarmHour, AlarmUtils.alarmMinutes, AppUtils.refactorStringIntoDate(calendarEvent.getPickedDate()), context, ACTION_OPEN_ALARM_CLASS);
            }
        }
    }

}