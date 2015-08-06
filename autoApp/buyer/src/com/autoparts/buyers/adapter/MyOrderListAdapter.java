package com.autoparts.buyers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.autoparts.buyers.R;

/**
 * 我的订单列表
 * Created by:Liuhuacheng
 * Created time:15-3-6
 */
public class MyOrderListAdapter extends BaseAdapter {

    public String[] strings;
    private Context context;

    public void setData(String[] strings) {
    }


    public MyOrderListAdapter(Context context, String[] strings) {
        this.strings = strings;
        this.context = context;
    }

    @Override
    public int getCount() {
        return strings.length;
//        return 2;
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
            view = LayoutInflater.from(context).inflate(R.layout.my_order_item, parent, false);
            holder = new ViewHolder();
            holder.order_title = (TextView) view.findViewById(R.id.order_title);
            holder.order_name = (TextView) view.findViewById(R.id.order_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.order_title.setText(strings[position]);
//        holder.order_name.setText(strings[position]);

        return view;
    }


    class ViewHolder {
        TextView order_title, order_name;
    }

}
