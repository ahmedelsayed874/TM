package com.blogspot.gm4s1.tmgm4s.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.preference.PreferenceManager;

import com.blogspot.gm4s1.tmgm4s.R;

/**
 * Created by GloryMaker on 1/25/2017.
 */

public class SettingsManager {

    public static boolean getPreferenceNotificationAdmittance(Context context) {
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(context);
        boolean notificationStatus =
                pref.getBoolean(
                        context.getString(R.string.preference_key_notification_admittance),
                        context.getString(R.string.preference_notification_admittance_default_value).equals("true"));



        return notificationStatus;
    }

    public static boolean getPreferenceNotificationFreeze(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(
                context.getString(R.string.preference_key_notification_freezing),
                context.getString(R.string.preference_notification_freezing_default_value).equals("true")
        );
    }

    public static String getPreferenceAlarmTone(Context context) {
        String notificationTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString();
        SharedPreferences preferences = context.getSharedPreferences(Settings.RINGTONE_PREF, Context.MODE_PRIVATE);
        notificationTone = preferences.getString(Settings.RINGTONE_KEY, notificationTone);

        return notificationTone;
    }

    public static int getPreferenceBackgroundColors(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        String sbgcolor =
                pref.getString(
                        context.getString(R.string.preference_key_colorlist_task_background_color),
                        context.getString(R.string.preference_colorlist_default_task_background_color));


        return Color.parseColor(sbgcolor);
    }
    public static int getPreferenceTextColors(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        String stextcolor =
                pref.getString(
                        context.getString(R.string.preference_key_colorlist_task_text_color),
                        context.getString(R.string.preference_colorlist_default_task_text_color));

        return Color.parseColor(stextcolor);
    }
}
