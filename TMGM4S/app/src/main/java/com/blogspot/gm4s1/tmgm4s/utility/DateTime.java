package com.blogspot.gm4s1.tmgm4s.utility;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Calendar;

import static android.R.attr.y;

/**
 * Created by GloryMaker on 11/30/2016.
 */

public class DateTime implements Serializable {
    private int _year, _month, _day;
    private int _hour, _minute, _second;


    //---------------------------------------------------------------//
    public DateTime() {
        setDateTime(System.currentTimeMillis());
    }

    public DateTime(int year, int month, int day, int hour, int minute) {
        setYear(year);
        setMonth(month);
        setDay(day);

        setTime(hour, minute);
    }
    public DateTime(int hour, int minute){
        setTime(hour, minute);
    }
    public DateTime(int year, int month, int day){
        setYear(year);
        setMonth(month);
        setDay(day);
    }


    //---------------------------------------------------------------//
    public int getYear() {
        return _year;
    }
    public void setYear(int year) {
        _year = year;
    }

    public int getMonth() {
        return _month;
    }
    public void setMonth(int month) {
        _month = month;
    }

    public int getDay() {
        return _day;
    }
    public void setDay(int day) {
        _day = day;
    }

    //---------------------------------------------------------------//
    public int getHour() {
        return _hour;
    }
    public int getMinute() {
        return _minute;
    }
    public void setTime(int hour, int minute) {
        _hour = hour;
        _minute = minute;

        if (minute >= 60) {
            _minute = minute % 60;
            _hour += minute / 60;
        }
    }

    private int get_second() {
        return _second;
    }
    private void set_second(int second) {
        this._second = second;
    }

    public boolean isAM() {
        return _hour < 12;
    }


    //---------------------------------------------------------------//
    Calendar calendar = Calendar.getInstance();
    public void setDateTime(long millis) {
        calendar.setTimeInMillis(millis);

        setYear(calendar.get(Calendar.YEAR));
        setMonth(calendar.get(Calendar.MONTH) + 1);
        setDay(calendar.get(Calendar.DAY_OF_MONTH));

        setTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        set_second(calendar.get(Calendar.SECOND));
    }


    //---------------------------------------------------------------//
    public String getDate(){
        return String.format("%04d-%02d-%02d", getYear(), getMonth(), getDay());
    }
    public String getTime(boolean is12Mode, boolean add_period){
        int h = getHour();
        if (is12Mode) {
            if (h == 0) h = 12;
            if (h > 12) h = h - 12;
        }
        String time = String.format("%02d:%02d", h, getMinute());
        if (add_period) time += (isAM()? "AM" : "PM");

        return time;
    }

    public int getDateAsInteger() {
        return getDateAsInteger(this);
    }
    public int getTimeAsInteger() {
        return getTimeAsInteger(this);
    }

    public static int getDateAsInteger(DateTime dt) {
        String date = String.format("%04d%02d%02d", dt.getYear(), dt.getMonth(), dt.getDay());
        return Integer.parseInt(date);
    }
    public static int getTimeAsInteger(DateTime dt) {
        String time = String.format("%02d%02d", dt.getHour(), dt.getMinute());
        return Integer.parseInt(time);
    }

    public static DateTime fromDateTime(int date, int time) {
        int yr, mo, da;
        int ho, mi;
        try {
            yr = date / 10000;
            date %= 10000;

            mo = date / 100;
            da = date % 100;

            ho = time / 100;
            mi = time % 100;

            return new DateTime(yr, mo, da, ho, mi);
        } catch (Exception e) {
            return null;
        }
    }


    //---------------------------------------------------------------//
    @Override
    public boolean equals(Object o) {
        if (o instanceof DateTime) {
            DateTime dt = (DateTime) o;

            return (
                    this.getYear() == dt.getYear() &&
                    this.getMonth() == dt.getMonth() &&
                    this.getDay() == dt.getDay() &&

                    this.getHour() == dt.getHour() &&
                    this.getMinute() == dt.getMinute() &&

                    this.isAM() == dt.isAM()
            );
        }
        return false;
    }
    @Override
    public String toString() {
        return getDate() + " " + getTime(true, true);
    }
    @Override
    public DateTime clone() {
        DateTime dt = new DateTime(_year, _month, _day, _hour, _minute);
        dt._second = _second;

        return dt;
    }



    //---------------------------------------------------------------//
    /*@Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_year);
        dest.writeInt(_month);
        dest.writeInt(_day);

        dest.writeInt(_hour);
        dest.writeInt(_minute);
    }*/
}
