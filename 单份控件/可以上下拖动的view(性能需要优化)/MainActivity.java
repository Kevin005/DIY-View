package com.future.myapplication11.activity.Views;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.future.myapplication11.R;
import com.future.myapplication11.activity.Views.CommonDragSortAdapter;

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
//        String[] testTitleStrArray = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"};
//        new ArrayList<String>(Arrays.asList(testTitleStrArray))
        List<String> testTitleStrList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            testTitleStrList.add(i + "");
        }
        BaseAdapter adapter = new com.future.myapplication11.activity.Views.CommonDragSortAdapter<>(this, R.layout.list_item, testTitleStrList, TitleViewHolder.class);
//        BaseAdapter adapter = new DragSortListViewAdapter(this, testTitleStrList);
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

    class DragSortListViewAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater inflater;
        private List<String> datas;

        private int invisblePosition = -1;

        public DragSortListViewAdapter(Context context, List<String> datas) {
            this.context = context;
            this.datas = datas;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = inflater.inflate(R.layout.list_item, parent, false);
            if (position == invisblePosition) {
                itemView.setVisibility(View.INVISIBLE);
                return itemView;
            }
            TextView titleTextView = (TextView) itemView.findViewById(R.id.tv_title);
            titleTextView.setText((CharSequence) getItem(position));
            return itemView;
        }

        public void setInvisblePosition(int invisblePosition) {
            this.invisblePosition = invisblePosition;
        }
    }
}
