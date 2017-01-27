package com.blogspot.gm4s1.tmgm4s.data.data_holder;

/**
 * Created by GloryMaker on 11/3/2016.
 */

public class TaskUtil {
    public enum Repetitions {
        EveryDay(1),
        EveryWeek(2),
        EveryMonth(3),
        EveryYear(4),
        None(0);

        int value;
        Repetitions(int v) {
            value = v;
        }

        public int getValue() {
            return value;
        }

        public Repetitions getRepetition(int val) {
            switch (val) {
                case 0: return Repetitions.None;
                case 1: return Repetitions.EveryDay;
                case 2: return Repetitions.EveryWeek;
                case 3: return Repetitions.EveryMonth;
                case 4: return Repetitions.EveryYear;
                default: return Repetitions.None;
            }
        }
    }
}
