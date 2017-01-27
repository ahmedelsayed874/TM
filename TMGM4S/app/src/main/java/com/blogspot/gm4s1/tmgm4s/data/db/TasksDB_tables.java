package com.blogspot.gm4s1.tmgm4s.data.db;

import android.provider.BaseColumns;

/**
 * Created by GloryMaker on 12/18/2016.
 */

public class TasksDB_tables {

    public static class TASKS implements BaseColumns {
        public static String TABLE_NAME = TASKS.class.getSimpleName();

        public static String START_DATE_I = "start_date";
        public static String START_TIME_I = "start_time";

        public static String END_DATE_I = "end_date";
        public static String END_TIME_I = "end_time";

        public static String INTERVAL_I = "interval";

        public static String TASK_TEXT_S = "task_text";
        public static String TASK_AUDIO_PATH_S = "task_audio_path";

        public static String BACKGROUND_COLOR_I = "background_color";
        public static String TEXT_COLOR_I = "text_color";

        public static String ALLOW_NOTIFICATION_I = "allow_notification";
        public static String TONE_AUDIO_URI_S = "tone_audio_uri";

        public static String REPETITION_TYPE_I = "repetition_type";

        public static String PARENT_ID_I = "parent_id";
    }
}
