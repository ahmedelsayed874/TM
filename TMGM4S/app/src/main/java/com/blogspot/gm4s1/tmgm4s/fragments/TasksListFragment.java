package com.blogspot.gm4s1.tmgm4s.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.blogspot.gm4s1.tmgm4s.R;
import com.blogspot.gm4s1.tmgm4s.activities.AddEditTaskActivity;
import com.blogspot.gm4s1.tmgm4s.data.data_adapter.TaskAdapter;
import com.blogspot.gm4s1.tmgm4s.data.data_holder.Task;
import com.blogspot.gm4s1.tmgm4s.data.db.TasksDB_Operations;
import com.blogspot.gm4s1.tmgm4s.services.TasksScheduler;
import com.blogspot.gm4s1.tmgm4s.utility.DateTime;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TasksListFragment extends Fragment {

    private static OnButtonsListener buttonsListener;
    private static ArrayList<Task> tasks2DisplayOnCreate;
    private TasksListSection tasksListSection;
    private ListView listView;


    //-----------------------------------------------------------//
    public static TasksListFragment getInstance(OnButtonsListener btnListener, ArrayList<Task> tasks) {
        buttonsListener = btnListener;
        tasks2DisplayOnCreate = tasks;

        return new TasksListFragment();
    }


    //-----------------------------------------------------------//
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tasks_list, container, false);

        tasksListSection = new TasksListSection(this, view);
        new ButtonsSection(this, view);

        this.registerForContextMenu(listView);

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.context_menu_tasks_items, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int id = item.getItemId();

        if (id == R.id.context_menu_action_delete) {
            long id1 = menuInfo.id;
            int position = menuInfo.position;

            removeTaskItem(position);
            return true;
        }

        return super.onContextItemSelected(item);
    }


    //-----------------------------------------------------------//
    public void addTaskItem(Task task) {
        tasksListSection.addTaskItem(task);
    }
    public void removeTaskItem(int index) {
        tasksListSection.removeTaskItem(index);
    }


    //-----------------------------------------------------------//
    private TaskAdapter getTaskAdapter() {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) return null;

        return (TaskAdapter) adapter;
    }


    //-----------------------------------------------------------//
    public interface OnButtonsListener {
        void onAddButtonClick(OnAddButtonEventHandler eventHandler);
        void onMoveUpButtonClick();
        void onMoveDownButtonClick();
    }
    public abstract class OnAddButtonEventHandler {
        private DateTime _starTime;
        private int _lastHour;
        private int _lastMinute;
        private Task _TASK = null;
        private boolean _cancel = false;

        public abstract TaskAdapter get_TaskAdapter();

        public void set_startTime(DateTime starTime) {
            _starTime = starTime;
        }
        public void set_lastTime(int lastHour, int lastMinute) {
            _lastHour = lastHour;
            _lastMinute = lastMinute;
        }
        public void set_task(Task task) {
            _TASK = task;
        }

        public int get_lastHour() {
            return _lastHour;
        }
        public int get_lastMinute() {
            return _lastMinute;
        }
        public DateTime get_starTime() {
            return _starTime;
        }
        public Task get_TASK() {
            return _TASK;
        }

        public void cancel() {
            _cancel = true;
        }
        public void permit() {
            _cancel = false;
        }
        public boolean is_cancel() {
            return _cancel;
        }
    }


    //-----------------------------------------------------------//
    class TasksListSection {
        private TasksListFragment _fragment;

        TasksListSection(TasksListFragment frag, View view){
            _fragment = frag;

            listView = (ListView) view.findViewById(R.id.listview_main_activity);

            displaySentTasks();
        }

        private void displaySentTasks() {
            if (tasks2DisplayOnCreate != null) {
                for (Task t : tasks2DisplayOnCreate) {
                    this.addTaskItem(t);
                }

                tasks2DisplayOnCreate = null;
            }
        }
        private void addTaskItem(Task task) {
            TaskAdapter adapter = getTaskAdapter();
            if (adapter == null) {
                adapter = new TaskAdapter(listView.getContext(), new ArrayList<Task>(), new TaskAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClicked(Task task) {
                        onTaskItemClicked(task);
                    }
                });
            }

            if (task != null) adapter.addTask(task);
            listView.setAdapter(adapter);
        }
        private void removeTaskItem(int index) {
            TaskAdapter adapter = getTaskAdapter();
            if (adapter != null) {
                Task task = adapter.getItem(index);
                TasksDB_Operations.deleteTask(getContext(), task.getID());

                adapter.removeTask(index);

                Task pTask = task.getPreviousTask();
                Task nTask = task.getNextTask();

                if (pTask != null) pTask.setNextTask(nTask);
                if (nTask != null) nTask.setPreviousTask(pTask);

                if (pTask != null) pTask.updateNextTasks(getContext());
                else {
                    if (nTask != null) {
                        nTask.update_StartAndEnd_DateTime(task.getStartDateTime());
                        nTask.updateNextTasks(getContext());
                    }
                }

                adapter.notifyDataSetChanged();
            }
        }

        private void onTaskItemClicked(Task task) {
            OnAddButtonEventHandler eventHandler = new OnAddButtonEventHandler() {
                @Override
                public TaskAdapter get_TaskAdapter() {
                    return getTaskAdapter();
                }
            };
            eventHandler.set_task(task);
            eventHandler.set_lastTime(23, 59);
            eventHandler.set_startTime(task.getStartDateTime());

            //start adding _activity
            AddEditTaskActivity.launch(_fragment.getActivity(), eventHandler, _fragment, false); //fragment reference of caller class to AddEditTaskActivity to help in saving operation and creating tasks items
        }
    }

    class ButtonsSection {
        private TasksListFragment _fragment;
        private OnAddButtonEventHandler _eventHandler;
        private OnButtonsListener _buttonsListener;


        ButtonsSection(TasksListFragment frag, View view){
            _fragment = frag;

            Button addButton = (Button) view.findViewById(R.id.addnew_button_main_activity);
            Button moveUpButton = (Button) view.findViewById(R.id.up_button_main_activity);
            Button moveDownButton = (Button) view.findViewById(R.id.down_button_main_activity);

            _buttonsListener = buttonsListener;

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAddNewTaskItemButtonClicked(v);
                }
            });
            moveUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMoveTaskItemUpButtonClicked(v);
                }
            });
            moveDownButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMoveTaskItemDownButtonClicked(v);
                }
            });

            _eventHandler = new OnAddButtonEventHandler() {
                @Override
                public TaskAdapter get_TaskAdapter() {
                    ListAdapter adapter = listView.getAdapter();
                    if (adapter == null) return null;

                    return (TaskAdapter) adapter;
                }
            };
        }

        private void onAddNewTaskItemButtonClicked(View view) {
            //get start time & task
            _eventHandler.set_task(null);
            _eventHandler.permit();
            _buttonsListener.onAddButtonClick(_eventHandler);
            if (_eventHandler.is_cancel()) return;

            //start adding _activity
            AddEditTaskActivity.launch(_fragment.getActivity(), _eventHandler, _fragment, true); //fragment reference of caller class to AddEditTaskActivity to help in saving operation and creating tasks items
        }
        private void onMoveTaskItemUpButtonClicked(View view) {
            if (_buttonsListener != null) _buttonsListener.onMoveUpButtonClick();
        }
        private void onMoveTaskItemDownButtonClicked(View view) {
            if (_buttonsListener != null) _buttonsListener.onMoveDownButtonClick();
        }
    }
}