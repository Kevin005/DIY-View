package com.kevin.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    private GridView record_gridView;//定义gridView
    private DateAdapter dateAdapter;//定义adapter
    private ImageView record_left;//左箭头
    private ImageView record_right;//右箭头
    private TextView record_title;//标题
    private int year;
    private int month;
    private String title;
    private int[][] days = new int[6][7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        //初始化日期数据
        initData();
        //初始化组件
        initView();
        //组件点击监听事件
        initEvent();
    }

    private void initData() {
        year = DateUtils.getYear();
        month = DateUtils.getMonth();
    }

    private void initEvent() {
        record_left.setOnClickListener(this);
        record_right.setOnClickListener(this);
    }

    private void initView() {
        /**
         * 以下是初始化GridView
         */
        record_gridView = (GridView) findViewById(R.id.record_gridView);
        days = DateUtils.getDayOfMonthFormat(2016, 8);
        dateAdapter = new DateAdapter(this, days, year, month);//传入当前月的年
        record_gridView.setAdapter(dateAdapter);
        record_gridView.setVerticalSpacing(60);
        record_gridView.setEnabled(true);
        /**
         * 以下是初始化其他组件
         */
        record_left = (ImageView) findViewById(R.id.record_left);
        record_right = (ImageView) findViewById(R.id.record_right);
        record_title = (TextView) findViewById(R.id.record_title);
    }


    /**
     * 下一个月
     *
     * @return
     */
    private int[][] nextMonth() {
        if (month == 12) {
            month = 1;
            year++;
        } else {
            month++;
        }
        days = DateUtils.getDayOfMonthFormat(year, month);
        return days;
    }

    /**
     * 上一个月
     *
     * @return
     */
    private int[][] prevMonth() {
        if (month == 1) {
            month = 12;
            year--;
        } else {
            month--;
        }
        days = DateUtils.getDayOfMonthFormat(year, month);
        return days;
    }

    /**
     * 设置标题
     */
    private void setTile() {
        title = year + "年" + month + "月";
        record_title.setText(title);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record_left:
                days = prevMonth();
                dateAdapter = new DateAdapter(this, days, year, month);
                record_gridView.setAdapter(dateAdapter);
                dateAdapter.notifyDataSetChanged();
                setTile();
                break;
            case R.id.record_right:
                days = nextMonth();
                dateAdapter = new DateAdapter(this, days, year, month);
                record_gridView.setAdapter(dateAdapter);
                dateAdapter.notifyDataSetChanged();
                setTile();
                break;
        }
    }
}
