package com.blogspot.gm4s1.tmgm4s.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.blogspot.gm4s1.tmgm4s.R;
import com.blogspot.gm4s1.tmgm4s.settings.SettingsManager;
import com.blogspot.gm4s1.tmgm4s.utility.DateTime;

/**
 * Created by GloryMaker on 1/25/2017.
 */

public class NotificationManager {

    public static void notifyUser(Context context, int notificationID, DateTime dt, String msg, Uri alarmSound, Intent resultIntent, Class<?> sourceActivityClass) {
        android.app.NotificationManager mNotificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(sourceActivityClass);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        /*NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        String[] events = new String[6];
        // Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("Event tracker details:");
        // Moves events into the expanded layout
        for (int i=0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }*/

        NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher1);
        mBuilder.setContentTitle(context.getString(R.string.app_name_full));
        mBuilder.setContentText("It's a time of: " + msg);

        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        mBuilder.setOngoing(SettingsManager.getPreferenceNotificationFreeze(context));
        mBuilder.setSound(alarmSound);
        //mBuilder.setStyle(inboxStyle);

        mNotificationManager.notify(notificationID, mBuilder.build());
    }

    public static void dismissNotification(Context context, int notificationID) {
        android.app.NotificationManager mNotificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(notificationID);
    }
}
