package com.blogspot.gm4s1.tmgm4s.services.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.blogspot.gm4s1.tmgm4s.services.TasksScheduler;

/**
 * Created by GloryMaker on 1/25/2017.
 */

public class TriggerReceiver extends BroadcastReceiver {
    public TriggerReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, TasksScheduler.class);
        context.startService(serviceIntent);
    }
}
