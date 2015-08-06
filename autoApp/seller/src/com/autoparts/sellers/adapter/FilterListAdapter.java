package com.autoparts.sellers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.autoparts.sellers.R;

/**
 * 通用List
 * Created by:Liuhuacheng
 * Created time:15-3-6
 */
public class FilterListAdapter extends BaseAdapter {

    public String[] strings;
    private Context context;

    public FilterListAdapter(Context context, String[] strings) {
        this.strings = strings;
        this.context = context;
    }

    @Override
    public int getCount() {
        return strings.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
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
            view = LayoutInflater.from(context).inflate(R.layout.filter_list_item, parent, false);
            holder = new ViewHolder();
            holder.common_item_title = (TextView) view.findViewById(R.id.common_item_title);
            holder.common_item_num = (TextView) view.findViewById(R.id.common_item_num);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.common_item_title.setText(strings[position]);

        if (position == 1){
            holder.common_item_num.setVisibility(View.VISIBLE);
        }else {
            holder.common_item_num.setVisibility(View.GONE);
        }
        return view;
    }


    class ViewHolder {
        TextView common_item_title,common_item_num;
    }

}
