package com.blogspot.gm4s1.tmgm4s.data.data_holder;

import android.content.Context;
import android.graphics.Color;

import com.blogspot.gm4s1.tmgm4s.data.db.TasksDB_Operations;
import com.blogspot.gm4s1.tmgm4s.utility.DateTime;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by GloryMaker on 11/30/2016.
 */

public class Task implements Serializable {

    private int _id = -1;
    private DateTime startDateTime = new DateTime();
    private DateTime endDateTime = new DateTime();
    private DateTime interval = new DateTime();

    private String taskText = "";
    private String taskAudioPath = "";

    private int backgroundColor = Color.WHITE;
    private int textColor = Color.RED;

    private boolean isNotifyAllowed = false;
    private String toneAudioUri = ""; //postpooooooooooooooooooooooooooooooooned

    private TaskUtil.Repetitions repetition = TaskUtil.Repetitions.None;

    private Task parentTask = null;
    private ArrayList<Task> subtasks;

    private Task nextTask = null;
    private Task previousTask = null;



    //-----------------------------------------------------------------------//
    public Task() {
        sn++;
        dn = sn;
    }


    //-----------------------------------------------------------------------//
    public int getID() {
        return _id;
    }
    public void setID(int id) {
        this._id = id;
    }

    //-----------------------------------------------------------------------//
    public DateTime getStartDateTime() {
        return startDateTime;
    }
    public void setStartDateTime(DateTime startTime) {
        this.startDateTime = startTime;
    }

    public DateTime getEndDateTime() {
        return endDateTime;
    }
    public void setEndDateTime(DateTime endTime) {
        this.endDateTime = endTime;
    }

    public DateTime getInterval() {
        return interval;
    }
    public void setInterval(DateTime interval) {
        this.interval = interval;
    }


    //-----------------------------------------------------------------------//
    public String getTaskText() {
        return taskText;
    }
    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public String getTaskAudioPath() {
        return taskAudioPath;
    }
    public void setTaskAudioPath(String taskAudioPath) {
        this.taskAudioPath = taskAudioPath;
    }


    //-----------------------------------------------------------------------//
    public int getBackgroundColor() {
        return backgroundColor;
    }
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }


    //-----------------------------------------------------------------------//
    public boolean isNotifyAllowed() {
        return isNotifyAllowed;
    }
    public void setNotifyAllowed(boolean notifyAllowed) {
        isNotifyAllowed = notifyAllowed;
    }

    public String getToneAudioUri() {
        return toneAudioUri;
    }
    public void setToneAudioUri(String toneAudioUri) {
        this.toneAudioUri = toneAudioUri;
    }


    //-----------------------------------------------------------------------//
    public TaskUtil.Repetitions getRepetition() {
        return repetition;
    }
    public void setRepetition(TaskUtil.Repetitions repetition) {
        this.repetition = repetition;
    }


    //-----------------------------------------------------------------------//
    public Task getParentTask() {
        return parentTask;
    }

    public ArrayList<Task> getSubtasks() {
        return subtasks;
    }
    public void addSubtask(Task task){
        if (subtasks == null) subtasks = new ArrayList<>();
        subtasks.add(task);
        task.parentTask = this;
    }
    public void removeSubtask(Task task){
        subtasks.remove(task);
    }


    //-----------------------------------------------------------------------//
    public Task getNextTask() {
        return nextTask;
    }
    public void setNextTask(Task nextTask) {
        this.nextTask = nextTask;
    }

    public Task getPreviousTask() {
        return previousTask;
    }
    public void setPreviousTask(Task previousTask) {
        this.previousTask = previousTask;
    }

    public void updateNextTasks(Context context) {
        Task pTask = this;
        Task nTask = this.getNextTask();

        while (nTask != null) {
            nTask.update_StartAndEnd_DateTime(pTask.getEndDateTime());

            TasksDB_Operations.saveTask(context, pTask, false);
            TasksDB_Operations.saveTask(context, nTask, false);

            pTask = nTask;
            nTask = nTask.getNextTask();
        }
    }

    public void update_StartAndEnd_DateTime(DateTime startDT){
        this.getStartDateTime().
                setTime(
                        startDT.getHour(),
                        startDT.getMinute()
                );

        this.getEndDateTime().
                setTime(
                        this.getStartDateTime().getHour()   + this.getInterval().getHour(),
                        this.getStartDateTime().getMinute() + this.getInterval().getMinute()
                );

        if (this.getEndDateTime().getTimeAsInteger() > 2359) {
            this.getEndDateTime().setTime(23, 59);
        }
    }


    private boolean currentTask = false;
    public boolean isCurrentTask() {
        return currentTask;
    }
    public void setCurrentTask(boolean currentTask) {
        this.currentTask = currentTask;
    }

    //-----------------------------------------------------------------------//
    @Override
    public String toString() {
        return "Task: #" + dn;//"ST: " + startTime.toString() + " &ET: " + endTime.toString();
    }
    static int sn = 0;
    int dn = 0;
}
