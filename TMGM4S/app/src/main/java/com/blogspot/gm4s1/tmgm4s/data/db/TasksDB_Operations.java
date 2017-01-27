package com.blogspot.gm4s1.tmgm4s.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.BaseAdapter;

import com.blogspot.gm4s1.tmgm4s.data.data_adapter.TasksDatesAdapter;
import com.blogspot.gm4s1.tmgm4s.data.data_holder.Task;
import com.blogspot.gm4s1.tmgm4s.data.data_holder.TaskUtil;
import com.blogspot.gm4s1.tmgm4s.utility.DateTime;

import java.util.ArrayList;

/**
 * Created by GloryMaker on 1/16/2017.
 */

public class TasksDB_Operations {

    //
    public static int getTasksCount(Context context, String dateValue) {
        Cursor query = TasksDB.query(
                context,
                new String[]{"count(*)"},
                TasksDB_tables.TASKS.START_DATE_I + "=" + dateValue,
                null
        );

        int c = 0;
        if (query.moveToFirst()) c = query.getInt(0);
        query.close();

        return c;
    }

    public static int getTasksCountAfterTime(Context context, DateTime cdt) {
        String where = "";
        where += /*TasksDB_tables.TASKS.START_DATE_I + "= " +*/ cdt.getDateAsInteger();
        where += " AND ";
        where += TasksDB_tables.TASKS.START_TIME_I + ">= " + cdt.getTimeAsInteger();

        return getTasksCount(context, where);
    }

    //
    public static Cursor getCurrentTask(Context context, DateTime cdt) {
        String where = "";
        where += TasksDB_tables.TASKS.START_DATE_I + "= " + cdt.getDateAsInteger();
        where += " AND ";
        where += TasksDB_tables.TASKS.START_TIME_I + "= " + cdt.getTimeAsInteger(); //BETWEEN n1 AND n2

        Cursor cursor = TasksDB.query(
                context,
                new String[] { "*" },
                where,
                null
        );

        return cursor;
    }

    //
    public static int getFirstTaskTime(Context context, int date) {
        Cursor query = TasksDB.query(
                context,
                new String[] { TasksDB_tables.TASKS.START_TIME_I },
                TasksDB_tables.TASKS.START_DATE_I + "=" + date,
                TasksDB_tables.TASKS.START_TIME_I
        );

        int t = -1;
        if (query.moveToFirst()) {
            t = getFieldValue(query, TasksDB_tables.TASKS.START_TIME_I, 0);
        }

        query.close();

        return t;
    }

    //
    public static BaseAdapter getTasksDatesAdapter(Context context) {
        Cursor query = TasksDB.query(
                context,
                new String[]{ "DISTINCT " +TasksDB_tables.TASKS.START_DATE_I },
                null,
                TasksDB_tables.TASKS.START_DATE_I
        );

        DateTime now = new DateTime();
        ArrayList<DateTime> dates = null;
        if (query.moveToFirst()) {
            dates = new ArrayList<>();
            int date;
            int time;
            do {
                date = getFieldValue(query, TasksDB_tables.TASKS.START_DATE_I, 0);

                if (now.getDateAsInteger() == date) continue;

                time = getFirstTaskTime(context, date);
                dates.add(DateTime.fromDateTime(date, time));
            } while (query.moveToNext());
        }

        TasksDatesAdapter adapter = null;

        if (dates != null) {
            adapter = new TasksDatesAdapter(context, dates);
        }

        query.close();

        return adapter;
    }

    //
    public static ArrayList<Task> fetchTasks(Context context, DateTime dt) {
        Cursor query = TasksDB.query(
                context,
                new String[] { "*" },
                TasksDB_tables.TASKS.START_DATE_I + "=" + dt.getDateAsInteger(),
                TasksDB_tables.TASKS.START_DATE_I
        );

        ArrayList<Task> tasks = null;
        if (query.moveToFirst()) {
            tasks = new ArrayList<>();

            do {
                tasks.add(convertCursorToTask(query, new Task()));
            } while (query.moveToNext());
        }

        query.close();

        return tasks;
    }

    //
    public static void saveTask(Context context, Task task, boolean saveNew) {
        ContentValues values = new ContentValues();

        values.put(TasksDB_tables.TASKS.START_DATE_I        , task.getStartDateTime().getDateAsInteger());
        values.put(TasksDB_tables.TASKS.START_TIME_I        , task.getStartDateTime().getTimeAsInteger());

        values.put(TasksDB_tables.TASKS.END_DATE_I          , task.getEndDateTime().getDateAsInteger());
        values.put(TasksDB_tables.TASKS.END_TIME_I          , task.getEndDateTime().getTimeAsInteger());
        values.put(TasksDB_tables.TASKS.INTERVAL_I          , task.getInterval().getTimeAsInteger());

        values.put(TasksDB_tables.TASKS.TASK_TEXT_S         , task.getTaskText());
        values.put(TasksDB_tables.TASKS.TASK_AUDIO_PATH_S   , task.getTaskAudioPath());

        values.put(TasksDB_tables.TASKS.BACKGROUND_COLOR_I  , task.getBackgroundColor());
        values.put(TasksDB_tables.TASKS.TEXT_COLOR_I        , task.getTextColor());

        values.put(TasksDB_tables.TASKS.ALLOW_NOTIFICATION_I, task.isNotifyAllowed()? 1:0);
        values.put(TasksDB_tables.TASKS.REPETITION_TYPE_I   , task.getRepetition().getValue());
        values.put(TasksDB_tables.TASKS.TONE_AUDIO_URI_S    , task.getToneAudioUri());

        //values.put(TasksDB_tables.TASKS.PARENT_ID_I         , task.getParentTask()==null? -1:task.getParentTask().getID());

        if (saveNew) TasksDB.insert(context, values);
        else TasksDB.update(context, values, task.getID()+"");
    }

    //
    public static void deleteTask(Context context, int taskID) {
        TasksDB.delete(context, taskID);
    }


    //----------------------------------------------------------------------//
    private static int getFieldValue(Cursor query, String colName, final int defaultVal) {
        int idx = query.getColumnIndex(colName);

        if (!query.isNull(idx)) {
            return query.getInt(idx);
        }

        return defaultVal;
    }
    private static String getFieldValue(Cursor query, String colName, final String defaultVal) {
        int idx = query.getColumnIndex(colName);

        if (!query.isNull(idx)) {
            return query.getString(idx);
        }

        return defaultVal;
    }
    private static Task convertCursorToTask(Cursor query, Task task) {
        int id;
        int sd, st;
        int ed, et;
        int in;
        String tt, tap;
        int bgc, txtc;
        int not, rep;
        String tone;
        //int parentId;

        task = new Task();

        id = getFieldValue(query  , TasksDB_tables.TASKS._ID                 , task.getID());
        sd = getFieldValue(query  , TasksDB_tables.TASKS.START_DATE_I        , 0          );
        st = getFieldValue(query  , TasksDB_tables.TASKS.START_TIME_I        , 0          );

        ed = getFieldValue(query  , TasksDB_tables.TASKS.END_DATE_I          , 0          );
        et = getFieldValue(query  , TasksDB_tables.TASKS.END_TIME_I          , 0          );
        in = getFieldValue(query  , TasksDB_tables.TASKS.INTERVAL_I          , 0          );

        tt = getFieldValue(query  , TasksDB_tables.TASKS.TASK_TEXT_S         , task.getTaskText());
        tap = getFieldValue(query , TasksDB_tables.TASKS.TASK_AUDIO_PATH_S   , task.getTaskAudioPath());

        bgc = getFieldValue(query , TasksDB_tables.TASKS.BACKGROUND_COLOR_I  , task.getBackgroundColor());
        txtc = getFieldValue(query, TasksDB_tables.TASKS.TEXT_COLOR_I        , task.getTextColor());

        not = getFieldValue(query , TasksDB_tables.TASKS.ALLOW_NOTIFICATION_I, task.isNotifyAllowed()? 1:0 );
        rep = getFieldValue(query , TasksDB_tables.TASKS.REPETITION_TYPE_I   , task.getRepetition().getValue());
        tone = getFieldValue(query, TasksDB_tables.TASKS.TONE_AUDIO_URI_S    , task.getToneAudioUri());

        //parentId = getFieldValue(query  , TasksDB_tables.TASKS.PARENT_ID_I   , -1);

        //
        task.setID(id);

        task.setStartDateTime(DateTime.fromDateTime(sd, st));
        task.setEndDateTime(DateTime.fromDateTime(ed, et));
        task.setInterval(DateTime.fromDateTime(0, in));

        task.setTaskText(tt);
        task.setTaskAudioPath(tap);

        task.setBackgroundColor(bgc);
        task.setTextColor(txtc);

        task.setNotifyAllowed(not == 1);
        task.setRepetition(TaskUtil.Repetitions.None.getRepetition(rep));
        task.setToneAudioUri(tone);

        return task;
    }
}
