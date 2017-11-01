package com.future.myapplication11.activity.presenter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * @param <T>
 */
public class CommonDragSortAdapter<T> extends BaseAdapter {
    protected static final String TAG = CommonDragSortAdapter.class.getSimpleName();
    private Context context;
    private int layout;
    private List<T> datas;
    private int dragSrcPosition = -1;
    private Class<? extends CommonViewHolder<T>> viewHolderClazz;

    /**
     * @param context
     * @param layout
     * @param datas
     */
    public CommonDragSortAdapter(Context context, int layout, List<T> datas, Class<? extends CommonViewHolder<T>> clazz) {
        super();
        this.context = context;
        this.layout = layout;
        this.datas = datas;
        this.viewHolderClazz = clazz;
    }

    /**
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        CommonViewHolder<T> holder = null;
//        if (convertView == null) {
//        convertView = LayoutInflater.from(context).inflate(layout, parent, false);
        ((TextView) ((ViewGroup) convertView).getChildAt(0)).setText((CharSequence) datas.get(position));

//            try {
//                holder = viewHolderClazz.getDeclaredConstructor(View.class).newInstance(convertView);
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//                Log.e(TAG, e.toString());
//            } catch (SecurityException e) {
//                e.printStackTrace();
//                Log.e(TAG, e.toString());
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.e(TAG, e.toString());
//            }
//
//            convertView.setTag(holder);
//        } else {
//            convertView.setVisibility(View.VISIBLE);
//            holder = (CommonViewHolder<T>) convertView.getTag();
//        }
//
//        holder.setItem(datas.get(position));
//        if (position == dragSrcPosition) {
//            convertView.setVisibility(View.INVISIBLE);
//        }
        return convertView;
    }

    /**
     * @return
     */
    @Override
    public int getCount() {
        return datas.size();
    }

    /**
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    /**
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return datas.size();
    }

    public void moveItem(int oldPosition, int newtPosition) {
        if (oldPosition == newtPosition) {
            notifyDataSetChanged();
            return;
        }
        T movingItem = datas.get(oldPosition);
        datas.remove(oldPosition);
        datas.add(newtPosition, movingItem);
    }

    /**
     * @param <T>
     */
    public static abstract class CommonViewHolder<T> {
        /**
         * @param item
         */
        public abstract void setItem(T item);
    }

    public void setDragSrcPosition(int dragSrcPosition) {
        this.dragSrcPosition = dragSrcPosition;
    }

}