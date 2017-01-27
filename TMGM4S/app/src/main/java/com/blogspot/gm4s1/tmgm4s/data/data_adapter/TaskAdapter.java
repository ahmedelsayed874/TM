package com.blogspot.gm4s1.tmgm4s.data.data_adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.gm4s1.tmgm4s.R;
import com.blogspot.gm4s1.tmgm4s.data.data_holder.Task;
import com.blogspot.gm4s1.tmgm4s.services.TasksScheduler;
import com.blogspot.gm4s1.tmgm4s.utility.DateTime;
import com.blogspot.gm4s1.tmgm4s.utility.TaskMenu;

import java.util.ArrayList;

import static android.R.attr.id;
import static android.R.attr.translateY;

/**
 * Created by GloryMaker on 12/24/2016.
 */

public class TaskAdapter extends BaseAdapter {
    private Context _context;
    private ArrayList<Task> _tasks;
    private OnItemClickListener onItemClickListener;
    Task pointedTask = null;


    public TaskAdapter(Context context, ArrayList<Task> tasks, OnItemClickListener listener) {
        this._context = context;

        if (tasks != null) this._tasks = tasks;
        else this._tasks = new ArrayList<>();

        for (int i = 1; i < this._tasks.size(); i++) {
            linkTasks(
                    this._tasks.get(i-1),
                    this._tasks.get(i)
            );
        }

        onItemClickListener = listener;
    }

    private void linkTasks(Task pTask, Task nTask) {
        pTask.setNextTask(nTask);
        nTask.setPreviousTask(pTask);
    }

    public void addTask(Task task) {
        _tasks.add(task);

        if (_tasks.size() > 1) {
            linkTasks(
                    _tasks.get(_tasks.size() - 2),
                    task
            );
        }

        doCompare(task);
    }
    private boolean doCompare(Task task) {
        if (TasksScheduler._startDateTime == null) return false;

        if (task.getStartDateTime().equals(TasksScheduler._startDateTime)) {
            task.setCurrentTask(true);
            if (pointedTask != null) {
                pointedTask.setCurrentTask(false);
            }

            pointedTask = task;

            return true;
        }

        return false;
    }

    public void removeTask(int index) {
        _tasks.remove(index);
    }

    @Override
    public int getCount() {
        return _tasks.size();
    }
    @Override
    public Task getItem(int position) {
        return _tasks.get(position);
    }
    @Override
    public long getItemId(int position) {
        return _tasks.get(position).hashCode();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(_context).inflate(R.layout.task_item_layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Task t = _tasks.get(position);
        holder.assignValues(t, holder);
//        holder.findSubTasks(t.getSubtasks());

        return convertView;
    }


    //-----------------------------------------------------------------------//
    class ViewHolder {
        HorizontalScrollView hor_scroll_of_task;
        TextView txtview_task;
        TextView txtview_endtime;
        TextView txtview_interval;
        //TextView txtview_pointer;
        ImageView imgview_pointer;
        Bitmap pointerImg;

//        LinearLayout linearLayout_container;
//        LinearLayout linearLayout_subtasks;

        ViewHolder(View v) {
            hor_scroll_of_task = (HorizontalScrollView) v.findViewById(R.id.task_item_horizontal_scrollview_of_task_textview);
            txtview_task =     (TextView) v.findViewById(R.id.task_item_textview_task);
            txtview_endtime =  (TextView) v.findViewById(R.id.task_item_textview_endtime);
            txtview_interval = (TextView) v.findViewById(R.id.task_item_textview_duration);
            //txtview_pointer =  (TextView) v.findViewById(R.id.task_item_textview_pointer);
            imgview_pointer =  (ImageView) v.findViewById(R.id.task_item_imageview_pointer);
            pointerImg = BitmapFactory.decodeResource(_context.getResources(), R.drawable.pointer);
//            linearLayout_container = (LinearLayout) v.findViewById(R.id.task_parent_linearlayout_container);
//            linearLayout_subtasks =  (LinearLayout) v.findViewById(R.id.task_parent_linearlayout_subtasks);

            assignClickEvent();

        }

        void assignClickEvent() {
            hor_scroll_of_task.setOnClickListener(onItemClicked);
            txtview_task.setOnClickListener(onItemClicked);
            txtview_endtime.setOnClickListener(onItemClicked);
            txtview_interval.setOnClickListener(onItemClicked);
            //txtview_pointer.setOnClickListener(onItemClicked);
            imgview_pointer.setOnClickListener(onItemClicked);
        }
        View.OnClickListener onItemClicked = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = (Task) v.getTag();
                if (onItemClickListener != null) onItemClickListener.onItemClicked(task);
            }
        };

        void assignValues(Task t, ViewHolder holder) {
            holder.hor_scroll_of_task.setTag(t);
            holder.txtview_task.setTag(t);
            holder.txtview_endtime.setTag(t);
            holder.txtview_interval.setTag(t);
            //holder.txtview_pointer.setTag(t);
            holder.imgview_pointer.setTag(t);

            holder.txtview_task.setText(t.getTaskText());
            holder.txtview_endtime.setText(t.getEndDateTime().getTime(true, true));
            holder.txtview_interval.setText(t.getInterval().getTime(false, false));


            holder.hor_scroll_of_task.setBackgroundColor(t.getBackgroundColor());
            holder.txtview_task.setBackgroundColor(t.getBackgroundColor());
            holder.txtview_endtime.setBackgroundColor(t.getBackgroundColor());
            holder.txtview_interval.setBackgroundColor(t.getBackgroundColor());
            //holder.txtview_pointer.setBackgroundColor(t.getBackgroundColor());
            holder.imgview_pointer.setBackgroundColor(t.getBackgroundColor());
            if (t.isCurrentTask()) {
                holder.imgview_pointer.setImageBitmap(pointerImg);
            } else {
                holder.imgview_pointer.setImageBitmap(null);
            }

            holder.txtview_task.setTextColor(t.getTextColor());
            holder.txtview_endtime.setTextColor(t.getTextColor());
            holder.txtview_interval.setTextColor(t.getTextColor());
        }
        void findSubTasks(ArrayList<Task> subTasks, ViewHolder holder) {
            /*if (subTasks == null) return;

            View subView;
            ViewHolder subHolder;

            for (Task st : subTasks) {
                subView = LayoutInflater.from(_context).inflate(R.layout.task_item_layout, holder.linearLayout_subtasks, false);
                subHolder = new ViewHolder(subView);

                assignValues(st, subHolder);

                findSubTasks(st.getSubtasks(), subHolder);
            }

            if (subTasks.size() > 0) holder.linearLayout_container.setVisibility(View.VISIBLE);
            else holder.linearLayout_container.setVisibility(View.GONE);*/
        }
    }


    //-----------------------------------------------------------------------//
    public interface OnItemClickListener {
        void onItemClicked(Task task);
    }
}
