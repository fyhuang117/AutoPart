package com.autoparts.buyers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.autoparts.buyers.R;

import java.util.HashMap;
import java.util.Vector;

/**
 * 通用List
 * Created by:Liuhuacheng
 * Created time:15-3-6
 */
public class InquireSortAdapter extends BaseAdapter {

    private Context context;
    private Vector<HashMap<String, Object>> mList;

    public void setData(Vector<HashMap<String, Object>> mList) {

        this.mList = mList;
    }

    public InquireSortAdapter(Context context, Vector<HashMap<String, Object>> mList) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.common_list_item, parent, false);
            holder = new ViewHolder();
            holder.common_item_title = (TextView) view.findViewById(R.id.common_item_title);
            holder.common_item_icon = (ImageView) view.findViewById(R.id.common_item_icon);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.common_item_icon.setVisibility(View.GONE);
        holder.common_item_title.setText((String) mList.get(position).get("nam"));
        return view;
    }


    class ViewHolder {
        TextView common_item_title;
        ImageView common_item_icon;
    }

}
