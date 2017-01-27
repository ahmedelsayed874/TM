package com.blogspot.gm4s1.tmgm4s.services;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.blogspot.gm4s1.tmgm4s.activities.TasksListActivity;
import com.blogspot.gm4s1.tmgm4s.data.db.TasksDB_Operations;
import com.blogspot.gm4s1.tmgm4s.data.db.TasksDB_tables;
import com.blogspot.gm4s1.tmgm4s.utility.DateTime;

import static com.blogspot.gm4s1.tmgm4s.data.db.TasksDB_Operations.getCurrentTask;

public class TasksScheduler extends IntentService {
    public static OnTaskOccurredListener _taskOccurredListener;
    public static DateTime _startDateTime;

    public final int notificationID = 1;

    private static DateTime cDT = null;
    private static DateTime endDT = null;
    private static int previousTime = -1;

    public TasksScheduler() {
        super(TasksScheduler.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        int time;
        int idx1;

        cDT = new DateTime();
        Cursor cursor = getCurrentTask(this, cDT);

        if (cursor.moveToFirst()) {
            idx1 = cursor.getColumnIndex(TasksDB_tables.TASKS.START_TIME_I);
            time = cursor.getInt(idx1);

            if (previousTime != time) {
                previousTime = time;

                idx1 = cursor.getColumnIndex(TasksDB_tables.TASKS.ALLOW_NOTIFICATION_I);
                if (cursor.getInt(idx1) == 1) {
                    idx1 = cursor.getColumnIndex(TasksDB_tables.TASKS.TASK_TEXT_S);
                    int idx2 = cursor.getColumnIndex(TasksDB_tables.TASKS.TONE_AUDIO_URI_S);

                    com.blogspot.gm4s1.tmgm4s.notification.NotificationManager
                        .notifyUser(
                                this,
                                notificationID,
                                cDT,
                                cursor.getString(idx1),
                                Uri.parse(cursor.getString(idx2)),
                                TasksListActivity.getIntent(this, cDT),
                                TasksListActivity.class
                        );

                    idx1 = cursor.getColumnIndex(TasksDB_tables.TASKS.END_DATE_I);
                    idx2 = cursor.getColumnIndex(TasksDB_tables.TASKS.END_TIME_I);

                    endDT = DateTime.fromDateTime(cursor.getInt(idx1), cursor.getInt(idx2));

                    _startDateTime = cDT;

                    if (_taskOccurredListener != null) {
                        _taskOccurredListener.onTaskOccurred();
                    }
                }
            }
        }

        if (!cursor.isClosed()) cursor.close();

        if (TasksDB_Operations.getTasksCountAfterTime(this, cDT) == 0) {
            if (endDT != null) {
                if (cDT.getDateAsInteger() + cDT.getTimeAsInteger() >
                    endDT.getDateAsInteger() + endDT.getTimeAsInteger()) {

                    com.blogspot.gm4s1.tmgm4s.notification.NotificationManager
                            .dismissNotification(this, notificationID);
                }
            } else {
                com.blogspot.gm4s1.tmgm4s.notification.NotificationManager
                        .dismissNotification(this, notificationID);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //SchedulerTrigger.startBackgroundService(this);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    public interface OnTaskOccurredListener {
        void onTaskOccurred();
    }
}
