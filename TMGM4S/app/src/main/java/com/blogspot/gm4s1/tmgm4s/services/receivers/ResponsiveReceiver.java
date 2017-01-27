package com.blogspot.gm4s1.tmgm4s.services.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.blogspot.gm4s1.tmgm4s.services.SchedulerTrigger;

/**
 * Created by GloryMaker on 1/25/2017.
 */

public class ResponsiveReceiver extends BroadcastReceiver {
    public ResponsiveReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SchedulerTrigger.startBackgroundService(context);
    }
}
