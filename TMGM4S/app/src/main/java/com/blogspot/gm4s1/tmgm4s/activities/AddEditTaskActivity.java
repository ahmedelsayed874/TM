package com.blogspot.gm4s1.tmgm4s.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.blogspot.gm4s1.tmgm4s.R;
import com.blogspot.gm4s1.tmgm4s.data.data_adapter.TaskAdapter;
import com.blogspot.gm4s1.tmgm4s.data.data_holder.Task;
import com.blogspot.gm4s1.tmgm4s.data.data_holder.TaskUtil;
import com.blogspot.gm4s1.tmgm4s.data.db.TasksDB_Operations;
import com.blogspot.gm4s1.tmgm4s.fragments.ColorChooserDialog;
import com.blogspot.gm4s1.tmgm4s.fragments.TasksListFragment;
import com.blogspot.gm4s1.tmgm4s.settings.SettingsManager;
import com.blogspot.gm4s1.tmgm4s.utility.DateTime;

public class AddEditTaskActivity extends AppCompatActivity {
    public final static String START_TIME_INTENT  =  "StartTime";
    public final static String LAST_HOUR_INTENT   =  "LastHour";
    public final static String LAST_MINUTE_INTENT =  "LastMinute";
    public final static String TASK_INTENT        =  "TASK";

    private Task _task;
    private EditTextSection _editTextSection;
    private RecordingSection _recordingSection;
    private ColoringSection _coloringSection;
    private FeaturesSection _featuresSection;
    private SubTasksSection _subTasksSection;


    //-------------------------------------------------------//
    private TasksListFragment tasksListOfCallerActivity; /* this reference value will change every time the user tap (+) button
                                               it will hold the TasksListFragment object of caller class [MainActivity| AddEditTaskActivity]
                                               its value will read in AddEditTaskActivity to help in creating task item */
    private static TasksListFragment tasksList;
    private static boolean isOpenedToAddNew;
    public static void launch(Context context, TasksListFragment.OnAddButtonEventHandler eventHandler, TasksListFragment TasksList, boolean addNew) {
        Intent i = new Intent(context, AddEditTaskActivity.class);
        i.putExtra(START_TIME_INTENT  , eventHandler.get_starTime());
        i.putExtra(LAST_HOUR_INTENT   , eventHandler.get_lastHour());
        i.putExtra(LAST_MINUTE_INTENT , eventHandler.get_lastMinute());
        i.putExtra(TASK_INTENT        , eventHandler.get_TASK());

        tasksList = TasksList;
        isOpenedToAddNew = addNew;

        context.startActivity(i);
    }

    //-------------------------------------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addedittask);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //---------------------------------------------------------------//
        Intent intent = getIntent();
        DateTime startTime = (DateTime) intent.getSerializableExtra(START_TIME_INTENT);
        int lastHour = intent.getIntExtra(LAST_HOUR_INTENT, 23);
        int lastMinute = intent.getIntExtra(LAST_MINUTE_INTENT, 59);
        _task = (Task) intent.getSerializableExtra(TASK_INTENT);

        if (tasksListOfCallerActivity == null) {
            tasksListOfCallerActivity = tasksList; //here, this _activity holds the reference of TasksListFragment object of caller Activity
        }

        //---------------------------------------------------------------//
        int p = 0;
        Task tTask = _task.getParentTask();
        while (tTask != null) {
            p++;
            tTask = tTask.getParentTask();
        }

        //---------------------------------------------------------------//
        _editTextSection = new EditTextSection(this, startTime, lastHour, lastMinute);
        _recordingSection = new RecordingSection(this);
        _coloringSection = new ColoringSection(this);
        _featuresSection = new FeaturesSection(this);
        _subTasksSection = new SubTasksSection(this, false);//p<2);   postpoooooooooooooooooooooned

        if (!isOpenedToAddNew) {

            _editTextSection.setTexts(
                    _task.getInterval().getTime(false, false),
                    _task.getTaskText()
            );

            _recordingSection.setAudioPath(_task.getTaskAudioPath());

            _coloringSection.setColors(
                    _task.getBackgroundColor(),
                    _task.getTextColor()
            );

            _featuresSection.setCB_AllowNotification(_task.isNotifyAllowed());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_edit_task_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
            return true;

        } else if (id == R.id.menu_add_edit_task_save_action){
            saveChanges();
            /*CustomAlertDialog.show(
                    "Alert",
                    "Task created successfully.",
                    getSupportFragmentManager(),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }
            );*/
            finish();
            return true;

        } else if (id == R.id.menu_add_edit_task_cancel_action){
            finish();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }


    //---------------------------------------------------------//
    public void saveChanges() {
        _editTextSection.saveChanges(_task);
        _recordingSection.saveChanges(_task);
        _coloringSection.saveChanges(_task);
        _featuresSection.saveChanges(_task);
        _subTasksSection.saveChanges(_task);

        if (isOpenedToAddNew) {
            tasksListOfCallerActivity.addTaskItem(_task);
            TasksDB_Operations.saveTask(this, _task, true);
        } else {
            if (_task.getNextTask() != null) {
                _task.updateNextTasks(this);
            } else {
                TasksDB_Operations.saveTask(this, _task, false);
            }
        }
    }

    private void displayMessage(String msg) {
        new AlertDialog.Builder(this).
                setMessage(msg).
                setPositiveButton("Ok", null).
                show();
    }



    //-----------------------------------------------------------------//
    class EditTextSection {
        private final DateTime _startTime;
        private final int _lastHour;
        private final int _lastMinute;
        private final DateTime _interval = new DateTime(0, 0);
        private final DateTime _endTime = new DateTime(0, 0);

        private final EditText StartTimeTxt;
        private final EditText IntervalTimeTxt;
        private final EditText EndTimeTxt;
        private final EditText TaskTxt;
        private final AddEditTaskActivity _activity;

        //-------------------------------------------------------------//
        public EditTextSection(AddEditTaskActivity activity, DateTime startTime, int lastHour, int lastMinute) {
            _activity = activity;

            StartTimeTxt    = (EditText) activity.findViewById(R.id.activity_addedittask_edittext_start_time);
            IntervalTimeTxt = (EditText) activity.findViewById(R.id.activity_addedittask_edittext_interval);
            EndTimeTxt      = (EditText) activity.findViewById(R.id.activity_addedittask_edittext_end_time);
            TaskTxt         = (EditText) activity.findViewById(R.id.activity_addedittask_edittext_task);

            _startTime = startTime;
            _lastHour = lastHour;
            _lastMinute = lastMinute;
            StartTimeTxt.setText(_startTime.getTime(true, true));

            IntervalTimeTxt.addTextChangedListener(textWatcher);

            _endTime.setYear(_startTime.getYear());
            _endTime.setMonth(_startTime.getMonth());
            _endTime.setDay(_startTime.getDay());
            changeEndTime(0, 0);
        }

        private void changeEndTime(int h, int m) {
            _endTime.setTime(_startTime.getHour() + h, _startTime.getMinute() + m);

            EndTimeTxt.setText(
                    _endTime.getTime(true, true)
            );
        }

        private TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                //new AlertDialog.Builder(_fragment).setMessage("afterTextChanged\n" + s.toString()).show();
                onIntervalTimeChanged(s);
            }
        };
        private void onIntervalTimeChanged(Editable e) {
            int h = 0;
            int m = 0;
            int idx;
            String[] seg;
            boolean err = false;

            idx = e.toString().indexOf(':');
            if (idx == 0) e.insert(0, "0");

            seg = e.toString().split(":");
            if (seg.length > 0) {
                if (seg[0].length() > 0) {
                    h = Integer.parseInt(seg[0]);
                }
            }
            if (seg.length > 1) {
                idx = 1;
                for (; idx < seg.length; idx++) {
                    if (seg[idx].length() > 0) break;
                }

                m = Integer.parseInt(seg[idx]);

                if (m > 59) {
                    err = true;
                    m = 59;
                }
            }

            int sHour = _startTime.getHour();

            int maxMinutes = 0;
            maxMinutes += (_lastHour - sHour) * 60;
            maxMinutes += (_lastMinute - _startTime.getMinute());

            int totUserMin = 0;
            totUserMin += (_lastHour - (sHour + h)) * 60;
            totUserMin += (_lastMinute - (_startTime.getHour() + m));

            if (totUserMin <= 0) {
                err = true;
                h = maxMinutes / 60;
                m = maxMinutes % 60;
            }


            if (err) {
                _activity.displayMessage(
                        "You entered a wrong value\n" +
                                "Maximum Interval " + (maxMinutes/60) + ":" + (maxMinutes%60) + "\n" +
                                "The value is changed");

                if (idx == -1) changeIntervalTime(e, String.format("%s", h));
                else changeIntervalTime(e, String.format("%s:%s", h, m));
            }
            else if (seg.length > 2) {
                changeIntervalTime(e, String.format("%s:%s", h, m));
            }

            changeEndTime(h, m);

            _interval.setTime(h, m);
        }
        private void changeIntervalTime(Editable e, String newValue) {
            IntervalTimeTxt.removeTextChangedListener(textWatcher);
            e.replace(0, e.length(), newValue);
            IntervalTimeTxt.addTextChangedListener(textWatcher);
        }

        //-------------------------------------------------------------//
        boolean is_interval_set() {
            return (IntervalTimeTxt.getText().length() > 0);
        }
        DateTime get_startTime() {
            return _startTime;
        }
        DateTime get_endTime() {
            return _endTime;
        }

        void setTexts(String interval, String taskTxt) {
            _editTextSection.IntervalTimeTxt.setText(interval);
            _editTextSection.TaskTxt.setText(taskTxt);
        }

        void saveChanges(Task task) {
            task.setStartDateTime(_startTime);
            task.setInterval(_interval);
            task.setEndDateTime(_endTime);
            task.setTaskText(TaskTxt.getText().toString().replaceAll("\n", " "));
        }
    }
    class RecordingSection {
        private final TextView AudioPath;
        private final Button Record;
        private final Button PlayPause;
        private final Button Delete;

        //------------------------------------------------------------//
        public RecordingSection(AddEditTaskActivity activity) {
            AudioPath = (TextView) activity.findViewById(R.id.activity_addedittask_textview_task_audio);
            Record    = (Button)   activity.findViewById(R.id.activity_addedittask_button_record_task_audio);
            PlayPause = (Button)   activity.findViewById(R.id.activity_addedittask_button_paly_pause_task_audio);
            Delete    = (Button)   activity.findViewById(R.id.activity_addedittask_button_delete_task_audio);

            assignButtonsEventListeners();
        }

        private void assignButtonsEventListeners() {
            Record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecordButtonClicked();
                }
            });

            PlayPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPlayPauseButtonClicked();
                }
            });

            Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteButtonClicked();
                }
            });
        }
        private void onRecordButtonClicked(){}
        private void onPlayPauseButtonClicked(){}
        private void onDeleteButtonClicked(){}

        public void setAudioPath(String path) {
            AudioPath.setText(path);
        }

        void saveChanges(Task task) {
            task.setTaskAudioPath(AudioPath.getText().toString());
        }
    }
    class ColoringSection {
        private int bgcolor;
        private int textcolor;

        private final Button TaskBackground;
        private final Button TaskText;
        private final AddEditTaskActivity _activity;

        //---------------------------------------------------//
        public ColoringSection(AddEditTaskActivity activity)  {
            _activity = activity;

            TaskBackground = (Button) activity.findViewById(R.id.activity_addedittask_button_task_color);
            TaskText       = (Button) activity.findViewById(R.id.activity_addedittask_button_text_color);

            bgcolor = SettingsManager.getPreferenceBackgroundColors(_activity);
            textcolor = SettingsManager.getPreferenceTextColors(_activity);
            changeButtonsColors();
            assignButtonsEventListener();
        }


        private void changeButtonsColors() {
            TaskBackground.setBackgroundColor(bgcolor);
            TaskBackground.setTextColor(textcolor);

            TaskText.setBackgroundColor(textcolor);
            TaskText.setTextColor(bgcolor);
        }

        private void assignButtonsEventListener(){
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onColorButtonClicked(v);
                }
            };

            TaskBackground.setOnClickListener(listener);
            TaskText.setOnClickListener(listener);
        }
        private void onColorButtonClicked(final View view) {
            ColorChooserDialog colorChooserDialog = new ColorChooserDialog();
            colorChooserDialog.setOnItemChoosedListener(new ColorChooserDialog.ItemChoosed() {
                @Override
                public void onItemChoosed(int idx) {
                    String[] colors = view.getContext().getResources().getStringArray(R.array.colors_values_preference);

                    if (view.getId() == R.id.activity_addedittask_button_task_color) {

                        bgcolor = Color.parseColor(colors[idx]);

                    } if (view.getId() == R.id.activity_addedittask_button_text_color) {

                        textcolor = Color.parseColor(colors[idx]);
                    }

                    changeButtonsColors();
                }
            });
            colorChooserDialog.show(_activity.getSupportFragmentManager(),"dialog");
        }

        public void setColors(int backgroundColor, int txtColor) {
            bgcolor = backgroundColor;
            textcolor = txtColor;
            TaskBackground.setBackgroundColor(backgroundColor);
            TaskText.setBackgroundColor(txtColor);
        }

        //---------------------------------------------------//
        void saveChanges(Task task) {
            task.setBackgroundColor(bgcolor);
            task.setTextColor(textcolor);
        }
    }
    class FeaturesSection {
        AddEditTaskActivity _activity;
        private final CheckBox CB_AllowNotification;
        private final RadioButton RB_RepetitionEveryDay;
        private final RadioButton RB_RepetitionEveryWeek;
        private final RadioButton RB_RepetitionEveryMonth;
        private final RadioButton RB_RepetitionEveryYear;
        private final RadioButton RB_RepetitionNone;

        public FeaturesSection(AddEditTaskActivity activity) {
            _activity = activity;

            CB_AllowNotification     = (CheckBox)    activity.findViewById(R.id.activity_addedittask_checkbox_allow_notification);

            RB_RepetitionEveryDay    = (RadioButton) activity.findViewById(R.id.activity_addedittask_radiobtn_every_day);
            RB_RepetitionEveryWeek   = (RadioButton) activity.findViewById(R.id.activity_addedittask_radiobtn_every_week);
            RB_RepetitionEveryMonth  = (RadioButton) activity.findViewById(R.id.activity_addedittask_radiobtn_every_month);
            RB_RepetitionEveryYear   = (RadioButton) activity.findViewById(R.id.activity_addedittask_radiobtn_every_year);
            RB_RepetitionNone        = (RadioButton) activity.findViewById(R.id.activity_addedittask_radiobtn_none);

            setCB_AllowNotification(SettingsManager.getPreferenceNotificationAdmittance(_activity));
        }


        //---------------------------------------------//
        public void setCB_AllowNotification(boolean checked) {
            CB_AllowNotification.setChecked(checked);
        }

        void saveChanges(Task task) {
            task.setToneAudioUri(SettingsManager.getPreferenceAlarmTone(_activity));

            task.setNotifyAllowed(CB_AllowNotification.isChecked());

            if (RB_RepetitionEveryDay.isChecked()) {
                task.setRepetition(TaskUtil.Repetitions.EveryDay);

            } else if (RB_RepetitionEveryWeek.isChecked()) {
                task.setRepetition(TaskUtil.Repetitions.EveryWeek);

            } else if (RB_RepetitionEveryMonth.isChecked()) {
                task.setRepetition(TaskUtil.Repetitions.EveryMonth);

            } else if (RB_RepetitionEveryYear.isChecked()) {
                task.setRepetition(TaskUtil.Repetitions.EveryYear);

            } else {
                task.setRepetition(TaskUtil.Repetitions.None);
            }
        }
    }
    class SubTasksSection implements TasksListFragment.OnButtonsListener {
        private final AddEditTaskActivity _activity;

        public SubTasksSection(AddEditTaskActivity activity, boolean showTasksList) {
            _activity = activity;

            if (showTasksList) {
                _activity.getSupportFragmentManager().
                        beginTransaction().
                        add(R.id.activity_addedittask_framelayout, TasksListFragment.getInstance(this, null)).
                        commit();
            } else {
                activity.findViewById(R.id.activity_addedittask_subtasks_textview).setVisibility(View.GONE);
                activity.findViewById(R.id.activity_addedittask_framelayout).setVisibility(View.GONE);
            }
        }

        @Override
        public void onAddButtonClick(TasksListFragment.OnAddButtonEventHandler eventHandler) {
            //Check if user enter value in the Interval text
            if (_editTextSection.is_interval_set() == false) {
                eventHandler.cancel();
                _activity.displayMessage("You should enter the value of interval");
                return;
            }

            /*subtask starts with start_time and ends with end_time
            if list empty; start time equals start_time
            else start_time starts after ending of last_subtask*/

            //set start time
            TaskAdapter adapter = eventHandler.get_TaskAdapter();
            if (adapter == null || adapter.getCount() == 0) {
                eventHandler.set_startTime(_editTextSection.get_startTime());
            } else {
                Task t = adapter.getItem(adapter.getCount() - 1);
                eventHandler.set_startTime(t.getEndDateTime());
            }

            //set last hour & last minute
            eventHandler.set_lastTime(
                    _editTextSection.get_endTime().getHour(),
                    _editTextSection.get_endTime().getMinute());

            //set task
            Task subTask = new Task();
            _task.addSubtask(subTask);
            eventHandler.set_task(subTask);
        }
        @Override
        public void onMoveUpButtonClick() {

        }
        @Override
        public void onMoveDownButtonClick() {

        }

        private void saveChanges(Task task) {

        }
    }
}