package com.example.myapplication.ui.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.R;
import com.example.myapplication.ui.MainActivity;

import java.util.Random;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String wateringName = bundle.getString("wateringName");
        String note = bundle.getString("note");
        int id = bundle.getInt("id");

        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyWater")
            .setSmallIcon(R.drawable.water)
            .setContentTitle(wateringName)
            .setContentText(note)
            .setColor(0xff123456)
            .setColorized(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        SystemClock.sleep(2000);
        notificationManagerCompat.notify(m, builder.build());
        /*long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("text")
                .setContentText("text")
                .setWhen(when)
                .setContentIntent(pendingIntent);
        notificationManager.notify(11, mNotifyBuilder.build());*/

    }

}