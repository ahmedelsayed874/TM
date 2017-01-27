package com.blogspot.gm4s1.tmgm4s.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.blogspot.gm4s1.tmgm4s.R;
import com.blogspot.gm4s1.tmgm4s.utility.DateTime;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetTimeFragment extends Fragment {

    /*private TimePicker timePicker;
    private int mHour, mMinute;*/

    private Spinner hourSpinner;
    private Spinner minuteSpinner;
    private Spinner dayPeriodSpinner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_set_time, container, false);

        /*timePicker = (TimePicker) view.findViewById(R.id.fragment_setTime_timePicker);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;
            }
        });*/

        hourSpinner      = (Spinner)    view.findViewById(R.id.fragment_setTime_spinner_hour);
        minuteSpinner    = (Spinner)    view.findViewById(R.id.fragment_setTime_spinner_minute);
        dayPeriodSpinner = (Spinner)    view.findViewById(R.id.fragment_setTime_spinner_dayperiod);

        DateTime dateTime = new DateTime();
        fillHourSpinner(dateTime.getHour());
        fillMinuteSpinner(dateTime.getMinute());
        fillDayPeriodSpinner(dateTime.isAM());

        return view;
    }

    private void fillHourSpinner(int h){
        String[] hours = new String[12];
        for (int i = 0; i < hours.length; i++) hours[i] = i+1 + "";
        ArrayAdapter<String> hoursAdapter = new ArrayAdapter<String>(
                this.getContext(),
                android.R.layout.simple_list_item_1,
                hours);
        hourSpinner.setAdapter(hoursAdapter);
        if (h > 12) h -= 12;
        if (h == 0) h = 12;
        hourSpinner.setSelection(h-1);
    }
    private void fillMinuteSpinner(int m){
        String[] minutes = new String[60];
        for (int i = 0; i < minutes.length; i++) minutes[i] = i + "";
        ArrayAdapter<String> minutesAdapter = new ArrayAdapter<String>(
                this.getContext(),
                android.R.layout.simple_list_item_1,
                minutes);
        minuteSpinner.setAdapter(minutesAdapter);
        minuteSpinner.setSelection(m);
    }
    private void fillDayPeriodSpinner(boolean amPeriod){
        String[] dayPer = { "AM", "PM" };
        ArrayAdapter<String> dayPerAdapter = new ArrayAdapter<String>(
                this.getContext(),
                android.R.layout.simple_list_item_1,
                dayPer);
        dayPeriodSpinner.setAdapter(dayPerAdapter);
        dayPeriodSpinner.setSelection((amPeriod? 0 : 1));
    }


    public void updateDateTime(DateTime dt) {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dt.setTime(timePicker.getHour(), timePicker.getMinute());
        } else {
            dt.setTime(mHour, mMinute);
        }*/

        int h = Integer.parseInt(hourSpinner.getSelectedItem().toString());
        if (dayPeriodSpinner.getSelectedItem().toString().equalsIgnoreCase("PM")) {
            if (h < 12) h += 12;
        } else {
            if (h == 12) h = 0;
        }
        dt.setTime(h, Integer.parseInt(minuteSpinner.getSelectedItem().toString()));
    }
}
