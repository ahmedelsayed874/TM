package com.blogspot.gm4s1.tmgm4s.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.blogspot.gm4s1.tmgm4s.R;
import com.blogspot.gm4s1.tmgm4s.data.data_adapter.TaskAdapter;
import com.blogspot.gm4s1.tmgm4s.data.data_holder.Task;
import com.blogspot.gm4s1.tmgm4s.data.db.TasksDB_Operations;
import com.blogspot.gm4s1.tmgm4s.fragments.TasksListFragment;
import com.blogspot.gm4s1.tmgm4s.settings.Settings;
import com.blogspot.gm4s1.tmgm4s.utility.DateTime;

import java.util.ArrayList;

public class TasksListActivity extends AppCompatActivity {
    public static final String DATE_TIME_INTENT = "DATE_TIME_INTENT";

    private static boolean updateNextTasksAllowed = true; //help in block next task when user click notification

    //---------------------------------------------------------//
    public static void launch(AppCompatActivity activity, DateTime dateTime) {
        activity.startActivity(getIntent(activity, dateTime));
        updateNextTasksAllowed = true;
    }
    public static Intent getIntent(Context context, DateTime dateTime) {
        Intent i = new Intent(context, TasksListActivity.class);
        i.putExtra(DATE_TIME_INTENT, dateTime);

        updateNextTasksAllowed = false;

        return i;
    }


    //---------------------------------------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DateTime dateTime = (DateTime) getIntent().getSerializableExtra(DATE_TIME_INTENT);

        //--------------------------------------------//
        new DateTimeSection(this, dateTime);

    }

    @Override
    protected void onStart() {
        super.onStart();

        new TasksListSection(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tasks_list_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
            return true;

        } else if (id == R.id.setting_menu_item_main_menu){
            startActivity(new Intent(this, Settings.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    //---------------------------------------------------------//
    static class DateTimeSection {
        static DateTimeSection Instance;

        private final TextView dateTimeTextView;
        private DateTime dateTime;

        private DateTimeSection(TasksListActivity a, DateTime dt) {
            Instance = this;
            dateTime = dt;

            dateTimeTextView = (TextView) a.findViewById(R.id.activity_tasks_list_textview_date_time);

            changeDateTime(dateTime);
        }

        public void changeDateTime(DateTime dt) {
            dateTime = dt;
            dateTimeTextView.setText(dt.toString());
        }
    }
    static class TasksListSection implements TasksListFragment.OnButtonsListener {

        private TasksListSection(TasksListActivity a) {
            ArrayList<Task> tasks = TasksDB_Operations.fetchTasks(a, DateTimeSection.Instance.dateTime);
            if (tasks != null && tasks.size() > 0) {
                if (updateNextTasksAllowed) {
                    if (tasks.get(0).getStartDateTime().equals(DateTimeSection.Instance.dateTime) == false) {
                        tasks.get(0).update_StartAndEnd_DateTime(DateTimeSection.Instance.dateTime);
                        tasks.get(0).updateNextTasks(a);
                    }
                }
            }
            TasksListFragment tasksListFragment = TasksListFragment.getInstance(this, tasks);

            a.getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_tasks_list_framelayout, tasksListFragment).commit();
        }

        @Override
        public void onAddButtonClick(TasksListFragment.OnAddButtonEventHandler eventHandler) {
            //set start time
            TaskAdapter adapter = eventHandler.get_TaskAdapter();
            if (adapter == null || adapter.getCount() == 0) {
                eventHandler.set_startTime(DateTimeSection.Instance.dateTime);
            } else {
                Task t = adapter.getItem(adapter.getCount() - 1);
                eventHandler.set_startTime(t.getEndDateTime());
            }

            //set last hour & last minute
            eventHandler.set_lastTime(23, 59);

            //set task
            eventHandler.set_task(new Task());
        }
        @Override
        public void onMoveUpButtonClick() {

        }
        @Override
        public void onMoveDownButtonClick() {

        }
    }
}
