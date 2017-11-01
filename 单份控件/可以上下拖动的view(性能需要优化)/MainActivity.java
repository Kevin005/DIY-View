package com.future.myapplication11.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.future.myapplication11.R;
import com.future.myapplication11.activity.presenter.CommonDragSortAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        ListView testListView = (ListView) findViewById(R.id.lv_test);
        List<String> testTitleStrList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            testTitleStrList.add(i + "");
        }
        BaseAdapter adapter = new CommonDragSortAdapter<>(this, R.layout.list_item, testTitleStrList, TitleViewHolder.class);
        testListView.setAdapter(adapter);
    }

    static class TitleViewHolder extends CommonDragSortAdapter.CommonViewHolder<String> {
        private TextView titleTextView;

        public TitleViewHolder(View convertView) {
            titleTextView = (TextView) convertView.findViewById(R.id.tv_title);
        }

        @Override
        public void setItem(String item) {
            titleTextView.setText(item);
        }
    }
}
