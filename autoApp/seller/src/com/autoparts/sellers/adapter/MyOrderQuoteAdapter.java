package com.autoparts.sellers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.autoparts.sellers.R;

/**
 * 我的订单--商家报价列表
 * Created by:Liuhuacheng
 * Created time:15-3-6
 */
public class MyOrderQuoteAdapter extends BaseAdapter {

    public String[] strings;
    private Context context;
    private int layoutView = -1;

    public void setData(String[] strings) {
    }


    public MyOrderQuoteAdapter(Context context, String[] strings) {
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
            view = LayoutInflater.from(context).inflate(R.layout.my_order_quote_item, parent, false);
            holder = new ViewHolder();
            holder.quote_title = (TextView) view.findViewById(R.id.quote_title);
            view.setTag(holder);
        } else {
        	
            holder = (ViewHolder) view.getTag();
        }
        
        holder.quote_title.setText(strings[position]);

        return view;
    }


    class ViewHolder {
        TextView quote_title;
    }

}
