package com.dianaszczepankowska.AllInOneCalendar.android.alarm;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;

import com.dianaszczepankowska.AllInOneCalendar.android.R;

import java.io.IOException;

public class AlarmClock extends AlarmService {

    public static final String ACTION_DISMISS = "Silence";
    public static final String ACTION_OPEN_ALARM_CLASS = "Open alarm class";

    @Override
    public void startAlarmService(int ringtoneType, int ringtneType2, int audioManagerStream, int usage, boolean looping) throws IOException {
        super.startAlarmService(ringtoneType, ringtneType2, audioManagerStream, usage, looping);

        setTheNotificationTapAction(this, ACTION_DISMISS, R.drawable.baseline_alarm_black_48, "Wake up", "And have a nice day", R.drawable.baseline_delete_black_48, "Silence");

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null) {
            return START_REDELIVER_INTENT;
        }

        try {
            startAlarmService(RingtoneManager.TYPE_ALARM, RingtoneManager.TYPE_RINGTONE, AudioManager.STREAM_ALARM, AudioAttributes.USAGE_ALARM, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

}


