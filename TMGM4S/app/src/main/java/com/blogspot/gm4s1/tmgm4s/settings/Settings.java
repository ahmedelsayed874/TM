package com.blogspot.gm4s1.tmgm4s.settings;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;

import com.blogspot.gm4s1.tmgm4s.R;

import static com.blogspot.gm4s1.tmgm4s.R.string.preference_key_colorlist_task_background_color;


/**
 * Created by GloryMaker on 12/1/2016.
 */

public class Settings extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
    public static final String RINGTONE_PREF = Settings.class.getName() + ".ringtone_pref";
    public static final String RINGTONE_KEY = "ringtonekey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_layout);

        Preference notification = findPreference(getString(R.string.preference_key_notification_admittance));
        Preference notification_freezing = findPreference(getString(R.string.preference_key_notification_freezing));
        Preference alarmtone = findPreference(getString(R.string.preference_key_alarmtone));
        Preference defaultBackgroundcolor = findPreference(getString(preference_key_colorlist_task_background_color));
        Preference defaultTextcolor = findPreference(getString(R.string.preference_key_colorlist_task_text_color));

        notification.setOnPreferenceChangeListener(this);
        notification_freezing.setOnPreferenceChangeListener(this);
        alarmtone.setOnPreferenceChangeListener(this);
        defaultBackgroundcolor.setOnPreferenceChangeListener(this);
        defaultTextcolor.setOnPreferenceChangeListener(this);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        onPreferenceChange(
                notification,
                sharedPref.getBoolean(
                        notification.getKey(),
                        getString(R.string.preference_notification_admittance_default_value).equals("true"))
        );
        onPreferenceChange(
                notification_freezing,
                sharedPref.getBoolean(
                        notification_freezing.getKey(),
                        getString(R.string.preference_notification_freezing_default_value).equals("true"))
        );
        onPreferenceChange(
                alarmtone,
                sharedPref.getString(
                        alarmtone.getKey(),
                        getString(R.string.preference_alarmtone_default_value))
        );
        onPreferenceChange(
                defaultBackgroundcolor,
                sharedPref.getString(
                        defaultBackgroundcolor.getKey(),
                        getString(R.string.preference_colorlist_default_task_background_color))
        );
        onPreferenceChange(
                defaultTextcolor,
                sharedPref.getString(
                        defaultTextcolor.getKey(),
                        getString(R.string.preference_colorlist_default_task_text_color))
        );
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (preference instanceof SwitchPreference){
            SwitchPreference sp = (SwitchPreference) preference;
            boolean value = (boolean) newValue;

            if (sp.getKey() == getString(R.string.preference_key_notification_admittance)) {
                if (value) {
                    sp.setSummary("You'll notify next Task");
                } else {
                    sp.setSummary("Notify will not reach you");
                }
            } else if (sp.getKey() == getString(R.string.preference_key_notification_freezing)) {
                if (value) {
                    sp.setSummary("The Notification is not allowed to dismiss");
                } else {
                    sp.setSummary("Notification will dismiss");
                }

            }

        } else if (preference instanceof RingtonePreference) {
            RingtonePreference rp = (RingtonePreference) preference;

            Uri toneUri = Uri.parse(newValue.toString());
            Ringtone tone = RingtoneManager.getRingtone(getApplicationContext(), toneUri);

            if (tone != null) {
                rp.setSummary(tone.getTitle(getApplicationContext()));
            } else {
                rp.setSummary("Select Tone");
            }

            SharedPreferences pre = getSharedPreferences(RINGTONE_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pre.edit();
            editor.putString(RINGTONE_KEY, newValue.toString());
            editor.commit();

        } else if (preference instanceof ListPreference) {
            ListPreference lp = (ListPreference) preference;

            int idx = lp.findIndexOfValue(newValue.toString());

            if (idx > 0) {
                lp.setSummary(lp.getEntries()[idx]);
            }

        }
        return true;
    }
}
