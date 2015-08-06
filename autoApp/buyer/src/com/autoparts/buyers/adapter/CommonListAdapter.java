package com.autoparts.buyers.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.autoparts.buyers.R;

/**
 * 通用List
 * Created by:Liuhuacheng
 * Created time:15-3-6
 */
public class CommonListAdapter extends BaseAdapter {

    public  String[] strings;
    private Context context;
    private int layoutView = -1;

    public void setData(String[] strings) {
    }

    public void setView(int layoutView){
       this.layoutView = layoutView;
    }

    public CommonListAdapter(Context context, String[] strings) {
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
            if (layoutView != -1){
                view = LayoutInflater.from(context).inflate(layoutView, parent, false);
            }else {
                view = LayoutInflater.from(context).inflate(R.layout.common_list_item, parent, false);
            }
            holder = new ViewHolder();
            holder.common_item_title = (TextView) view.findViewById(R.id.common_item_title);
            holder.common_item_icon = (ImageView) view.findViewById(R.id.common_item_icon);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.common_item_title.setText(strings[position]);


        return view;
    }


    class ViewHolder {
        TextView common_item_title;
        ImageView common_item_icon;
    }

}
