package com.blogspot.gm4s1.tmgm4s.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.blogspot.gm4s1.tmgm4s.R;
import com.blogspot.gm4s1.tmgm4s.utility.DateTime;

public class SetDateFragment extends Fragment {
    private DatePicker datePicker;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_set_date, container, false);
        datePicker = (DatePicker) view.findViewById(R.id.fragment_setdate_datePicker);
        return view;
    }

    public void updateDateTime(DateTime dt) {
        dt.setYear(datePicker.getYear());
        dt.setMonth(datePicker.getMonth() + 1);
        dt.setDay(datePicker.getDayOfMonth());
    }
}
