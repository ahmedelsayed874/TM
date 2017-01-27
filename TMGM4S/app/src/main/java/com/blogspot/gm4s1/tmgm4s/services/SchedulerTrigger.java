package com.blogspot.gm4s1.tmgm4s.services;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.blogspot.gm4s1.tmgm4s.services.receivers.ResponsiveReceiver;
import com.blogspot.gm4s1.tmgm4s.services.receivers.TriggerReceiver;

public class SchedulerTrigger {

    private static PendingIntent getPendingIntent(Context context) {
        Intent triggerIntent = new Intent(context, TriggerReceiver.class);
        return PendingIntent.getBroadcast(context, 0, triggerIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public static void startBackgroundService(Context context) {
        PendingIntent pendingIntent = getPendingIntent(context);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 30000, pendingIntent);

        // Enable {@code ResponsiveReceiver} to automatically restart the alarm when the
        // device is rebooted.
        ComponentName receiver = new ComponentName(context, ResponsiveReceiver.class);

        PackageManager packageManager = context.getPackageManager();
        packageManager.setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
    public static void stopBackgroundService(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent(context));

        // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the
        // alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, ResponsiveReceiver.class);

        PackageManager packageManager = context.getPackageManager();
        packageManager.setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    private boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
