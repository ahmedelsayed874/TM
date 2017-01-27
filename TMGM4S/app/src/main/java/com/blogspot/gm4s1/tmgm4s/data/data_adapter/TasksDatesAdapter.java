package com.blogspot.gm4s1.tmgm4s.data.data_adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blogspot.gm4s1.tmgm4s.R;
import com.blogspot.gm4s1.tmgm4s.data.data_holder.Task;
import com.blogspot.gm4s1.tmgm4s.utility.DateTime;

import java.util.ArrayList;

/**
 * Created by GloryMaker on 1/14/2017.
 */

public class TasksDatesAdapter extends BaseAdapter {
    private Context _context;
    private ArrayList<DateTime> _dates;


    public TasksDatesAdapter(Context context, ArrayList<DateTime> dates) {
        this._context = context;

        if (dates != null) this._dates = dates;
        else this._dates = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return _dates.size();
    }
    @Override
    public DateTime getItem(int position) {
        return _dates.get(position);
    }
    @Override
    public long getItemId(int position) {
        return _dates.get(position).hashCode();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(_context).inflate(android.R.layout.simple_list_item_1, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DateTime dt = _dates.get(position);
        holder.text1.setText(dt.getDate());//.toString());

        return convertView;
    }

    class ViewHolder {
        TextView text1;

        ViewHolder(View v){
            text1 = (TextView) v.findViewById(android.R.id.text1);

            text1.setGravity(Gravity.CENTER);
        }
    }
}
