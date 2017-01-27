package com.blogspot.gm4s1.tmgm4s.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blogspot.gm4s1.sqlitecommands.Constraints;
import com.blogspot.gm4s1.sqlitecommands.DataTypes;
import com.blogspot.gm4s1.sqlitecommands.SqlCommands;

/**
 * Created by GloryMaker on 12/18/2016.
 */

public class TasksDB extends SQLiteOpenHelper {
    public static final String DB_NAME = "tasks_db";
    public static final int DB_VERSION = 2;

    public TasksDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SqlCommands sqlCommands = new SqlCommands();

        SqlCommands.CreateTable tasksTable = sqlCommands.new CreateTable(TasksDB_tables.TASKS.TABLE_NAME);
        tasksTable.addColumn(TasksDB_tables.TASKS._ID                 , DataTypes.INTEGER, new Constraints[]{ Constraints.PRIMARY_Key, Constraints.AUTOINCREMENT });

        tasksTable.addColumn(TasksDB_tables.TASKS.START_DATE_I        , DataTypes.INTEGER, null);
        tasksTable.addColumn(TasksDB_tables.TASKS.START_TIME_I        , DataTypes.INTEGER, null);

        tasksTable.addColumn(TasksDB_tables.TASKS.END_DATE_I          , DataTypes.INTEGER, null);
        tasksTable.addColumn(TasksDB_tables.TASKS.END_TIME_I          , DataTypes.INTEGER, null);

        tasksTable.addColumn(TasksDB_tables.TASKS.INTERVAL_I          , DataTypes.INTEGER, null);

        tasksTable.addColumn(TasksDB_tables.TASKS.TASK_TEXT_S         , DataTypes.TEXT   , null);
        tasksTable.addColumn(TasksDB_tables.TASKS.TASK_AUDIO_PATH_S   , DataTypes.TEXT   , null);

        tasksTable.addColumn(TasksDB_tables.TASKS.BACKGROUND_COLOR_I  , DataTypes.INTEGER, null);
        tasksTable.addColumn(TasksDB_tables.TASKS.TEXT_COLOR_I        , DataTypes.INTEGER, null);

        tasksTable.addColumn(TasksDB_tables.TASKS.ALLOW_NOTIFICATION_I, DataTypes.INTEGER, null);
        tasksTable.addColumn(TasksDB_tables.TASKS.TONE_AUDIO_URI_S    , DataTypes.TEXT   , null);

        tasksTable.addColumn(TasksDB_tables.TASKS.REPETITION_TYPE_I   , DataTypes.INTEGER, null);
        tasksTable.addColumn(TasksDB_tables.TASKS.PARENT_ID_I         , DataTypes.INTEGER, null);

        db.execSQL(tasksTable.getCode());
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SqlCommands sqlCommands = new SqlCommands();

        SqlCommands.DropTable tasksTable = sqlCommands.new DropTable(TasksDB_tables.TASKS.TABLE_NAME);
        db.execSQL(tasksTable.getCode());

        onCreate(db);
    }


    //---------------------------------------------------------------------//
    public static Cursor query(Context context, String[] columns,
                               String whereClause, String orderBy) {
        TasksDB task_db = new TasksDB(context);
        SQLiteDatabase db = task_db.getReadableDatabase();

        /*select columnsNames from tableName where columnName1=value1 AND columnName1=value1*/
        Cursor query = db.query(
                TasksDB_tables.TASKS.TABLE_NAME,
                columns,
                whereClause,
                null, null, null,
                orderBy);

        return query;
    }
    public static void insert(Context context, ContentValues values) {
        TasksDB task_db = new TasksDB(context);
        SQLiteDatabase db = task_db.getWritableDatabase();

        /*insert into TableName(column1, ..., columnN) values(value1, ..., valuesN)*/
        db.insert(TasksDB_tables.TASKS.TABLE_NAME, null, values);
    }
    public static void update(Context context, ContentValues values, String targetID) {
        TasksDB task_db = new TasksDB(context);
        SQLiteDatabase db = task_db.getWritableDatabase();

        db.update(
                TasksDB_tables.TASKS.TABLE_NAME,
                values,
                TasksDB_tables.TASKS._ID + "=?",
                new String[] { targetID }
        );
    }
    public static void delete(Context context, int taskID) {
        TasksDB task_db = new TasksDB(context);
        SQLiteDatabase db = task_db.getWritableDatabase();

        db.delete(
                TasksDB_tables.TASKS.TABLE_NAME,
                TasksDB_tables.TASKS._ID + "=?",
                new String[] { taskID + "" }
        );
    }
}