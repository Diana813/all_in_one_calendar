package com.dianaszczepankowska.AllInOneCalendar.android.alarm;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import java.io.IOException;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class AlarmService extends Service {

    MediaPlayer mediaPlayer;

    public void startAlarmService(int ringtoneType, int ringtneType2, int audioManagerStream, int usage, boolean looping) throws IOException {

        Uri alarmUri = RingtoneManager.getDefaultUri(ringtoneType);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(ringtneType2);
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                .setLegacyStreamType(audioManagerStream)
                .setUsage(usage)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build());
        mediaPlayer.setDataSource(this, alarmUri);
        mediaPlayer.setLooping(looping);
        mediaPlayer.prepare();
        mediaPlayer.start();

    }

    public void setTheNotificationTapAction(Context context, String action, int imageIcon, String title, String subtitle, int imageDismiss, String dismissTitle) {

        Intent alarmStopIntent = new Intent(context, AlarmReceiver.class);
        alarmStopIntent.setAction(action);
        alarmStopIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        PendingIntent alarmStopPendingIntent =
                PendingIntent.getBroadcast(context, 0, alarmStopIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "id")
                .setSmallIcon(imageIcon)
                .setContentTitle(title)
                .setContentText(subtitle)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .addAction(imageDismiss, dismissTitle,
                        alarmStopPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        int notificationId = 321;
        notificationManager.notify(notificationId, builder.build());

    }


    public void onCreate() {
        super.onCreate();
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

