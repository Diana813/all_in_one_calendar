package com.example.android.flowercalendar.Calendar;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import com.example.android.flowercalendar.R;

import java.io.IOException;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class AlarmClock extends Service {

    private MediaPlayer mediaPlayer;
    private static final String URI_BASE = AlarmClock.class.getName() + ".";
    public static final String ACTION_DISMISS = "Silence";

    public void startAlarmClock() throws IOException {

        setTheNotificationTapAction(this);

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                .setLegacyStreamType(AudioManager.STREAM_ALARM)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build());
        mediaPlayer.setDataSource(this, alarmUri);
        mediaPlayer.setLooping(true);
        mediaPlayer.prepare();
        mediaPlayer.start();

    }

    private void setTheNotificationTapAction(Context context) {

        Intent alarmStopIntent = new Intent(context, AlarmReceiver.class);
        alarmStopIntent.setAction("Silence");
        alarmStopIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        PendingIntent alarmStopPendingIntent =
                PendingIntent.getBroadcast(context, 0, alarmStopIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "id")
                .setSmallIcon(R.drawable.baseline_alarm_black_48)
                .setContentTitle("Wake up")
                .setContentText("And have a nice day")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .addAction(R.drawable.baseline_delete_black_48, "Silence",
                        alarmStopPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        int notificationId = 321;
        notificationManager.notify(notificationId, builder.build());

    }


    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null) {
            return START_REDELIVER_INTENT;
        }

        try {
            startAlarmClock();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        mediaPlayer.release();
    }


}


