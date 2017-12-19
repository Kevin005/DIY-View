package com.kevin.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by kevin on 17-12-10.
 */

public class DateAdapter extends BaseAdapter {
    private int[] days = new int[42];
    private Context context;
    private int year;
    private int month;

    public DateAdapter(Context context, int[][] days, int year, int month) {
        this.context = context;
        int dayNum = 0;
        //将二维数组转化为一维数组，方便使用
        for (int i = 0; i < days.length; i++) {
            for (int j = 0; j < days[i].length; j++) {
                this.days[dayNum] = days[i][j];
                dayNum++;
            }
        }
        this.year = year;
        this.month = month;
    }

    @Override
    public int getCount() {
        return days.length;
    }

    @Override
    public Object getItem(int i) {
        return days[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.date_item, null);
            viewHolder = new ViewHolder();
            viewHolder.date_item = (TextView) view.findViewById(R.id.date_item);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
		//将上个月的和下个月的设置为灰色
        if (i < 7 && days[i] > 20) {
            viewHolder.date_item.setTextColor(Color.rgb(204, 204, 204));
        } else if (i > 20 && days[i] < 15) {
            viewHolder.date_item.setTextColor(Color.rgb(204, 204, 204));
        }
        viewHolder.date_item.setText(days[i] + "");
        viewHolder.date_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aa = viewHolder.date_item.getText().toString();
                Toast.makeText(context, aa, Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    /**
     * 优化Adapter
     */
    class ViewHolder {
        TextView date_item;
    }
}
