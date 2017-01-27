package com.blogspot.gm4s1.tmgm4s.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.blogspot.gm4s1.tmgm4s.R;
import com.blogspot.gm4s1.tmgm4s.data.db.TasksDB_Operations;
import com.blogspot.gm4s1.tmgm4s.services.SchedulerTrigger;
import com.blogspot.gm4s1.tmgm4s.utility.DateTime;


public class MainActivity extends AppCompatActivity {

    private TextView txtview_today;
    private ListView lstview_tasks_dates;
    private TextView txtview_add_new_tasks;
    private TextView txtview_no_tasks_yet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtview_today = (TextView) findViewById(R.id.activity_main_textview_today);
        lstview_tasks_dates = (ListView) findViewById(R.id.activity_main_listview_tasks_dates);
        txtview_add_new_tasks = (TextView) findViewById(R.id.activity_main_textview_add_new_tasks);
        txtview_no_tasks_yet = (TextView) findViewById(R.id.activity_main_textview_no_tasks_yet);

        assignEventHandlers();

        SchedulerTrigger.startBackgroundService(this);
        //this.registerReceiver(new ResponsiveReceiver(), new IntentFilter("android.intent.action.TIME_TICK"));
    }
    void assignEventHandlers() {
        txtview_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTodayTextViewClicked(v);
            }
        });
        lstview_tasks_dates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onTasksListItemClicked(parent, position);
            }
        });
        txtview_add_new_tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddNewTasksTextViewClicked(v);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        displayNumberOfTodayTasks();
        displayRegisteredDates();
    }
    private void displayNumberOfTodayTasks()  {
        int nTasks;
        DateTime today = new DateTime();

        nTasks = TasksDB_Operations.getTasksCount(this, today.getDateAsInteger() + "");

        txtview_today.setText("TODAY [" + nTasks + "]");
    }
    private void displayRegisteredDates() {

        BaseAdapter adapter = TasksDB_Operations.getTasksDatesAdapter(this);
        lstview_tasks_dates.setAdapter(adapter);

        if (adapter != null) {
            if (adapter.getCount() > 0) {
                txtview_no_tasks_yet.setVisibility(View.GONE);
            }
        }
    }


    private void onTodayTextViewClicked(View view) {
        DateTime dt = new DateTime();

        int t = TasksDB_Operations.getFirstTaskTime(this, dt.getDateAsInteger());

        if (t != -1) {
            TasksListActivity.launch(this, dt.fromDateTime(dt.getDateAsInteger(), t));
        } else {
            TasksListActivity.launch(this, dt);
        }
    }
    private void onTasksListItemClicked(AdapterView<?> parent, int position) {
        DateTime dt = (DateTime) parent.getItemAtPosition(position);

        TasksListActivity.launch(this, dt);
    }
    private void onAddNewTasksTextViewClicked(View view) {
        SetDateTimeActivity.launch(this);
    }


}
