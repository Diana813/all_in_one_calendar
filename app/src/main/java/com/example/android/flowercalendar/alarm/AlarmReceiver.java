package com.example.android.flowercalendar.alarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.android.flowercalendar.alarm.AlarmClock.ACTION_DISMISS;
import static com.example.android.flowercalendar.alarm.AlarmClock.ACTION_OPEN_ALARM_CLASS;
import static com.example.android.flowercalendar.alarm.Notification.ACTION_OPEN_NOTIFICATION_CLASS;
import static com.example.android.flowercalendar.alarm.Notification.ACTION_STOP;

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

}