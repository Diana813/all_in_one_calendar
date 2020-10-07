package com.dianaszczepankowska.AllInOneCalendar.android.alarm;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;

import com.dianaszczepankowska.AllInOneCalendar.android.R;

import java.io.IOException;

public class Notification extends AlarmService {

    public static final String ACTION_STOP = "OK";
    public static final String ACTION_OPEN_NOTIFICATION_CLASS = "Open notification class";
    public static final String ACTION_SET_NOTIFICATIONS = "Set notification";

    private String notificationInfo;


    @Override
    public void startAlarmService(int ringtoneType, int ringtneType2, int audioManagerStream, int usage, boolean looping) throws IOException {
        super.startAlarmService(ringtoneType, ringtneType2, audioManagerStream, usage, looping);

        setTheNotificationTapAction(this, ACTION_STOP, R.mipmap.ic_launcher, notificationInfo, "All in One Calendar", R.drawable.baseline_delete_black_48, "OK");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null) {
            return START_REDELIVER_INTENT;
        }
        try {
            notificationInfo = intent.getStringExtra("title");
            startAlarmService(RingtoneManager.TYPE_NOTIFICATION, RingtoneManager.TYPE_RINGTONE, AudioManager.STREAM_NOTIFICATION, AudioAttributes.USAGE_NOTIFICATION, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }
}



