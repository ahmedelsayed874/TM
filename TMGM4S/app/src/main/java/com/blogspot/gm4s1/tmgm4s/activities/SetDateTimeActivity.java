package com.blogspot.gm4s1.tmgm4s.activities;

import android.content.Intent;
import android.content.IntentSender;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.blogspot.gm4s1.tmgm4s.R;
import com.blogspot.gm4s1.tmgm4s.fragments.SetDateFragment;
import com.blogspot.gm4s1.tmgm4s.fragments.SetTimeFragment;
import com.blogspot.gm4s1.tmgm4s.utility.DateTime;

public class SetDateTimeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private SetDateFragment dateFragment = new SetDateFragment();
    private SetTimeFragment timeFragment = new SetTimeFragment();


    //---------------------------------------------------------//
    public static void launch(AppCompatActivity activity) {
        Intent i = new Intent(activity, SetDateTimeActivity.class);

        activity.startActivity(i);
    }


    //--------------------------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setdatetime);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private Fragment[] fragments = new Fragment[]{ dateFragment, timeFragment };

            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        };

        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(pagerAdapter);
    }


    //--------------------------------------------//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.set_date_time_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
            return true;

        } else if (id == R.id.menu_set_date_time_done_action){
            updateDateTime();
            finish();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }


    private void updateDateTime(){
        DateTime dateTime = new DateTime();

        dateFragment.updateDateTime(dateTime);
        timeFragment.updateDateTime(dateTime);

        TasksListActivity.launch(this, dateTime);
        finish();
    }
}