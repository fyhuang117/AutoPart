package com.autoparts.buyers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.autoparts.buyers.R;

/**
 * 询价商家列表
 * Created by:Liuhuacheng
 * Created time:15-3-6
 */
public class MyInquiryListAdapter extends BaseAdapter {

    public String[] strings;
    private Context context;
    private int layoutView = -1;

    public void setData(String[] strings) {
    }


    public MyInquiryListAdapter(Context context, String[] strings) {
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
            view = LayoutInflater.from(context).inflate(R.layout.myinquiry_list_item, parent, false);
            holder = new ViewHolder();
            holder.inquiry_title = (TextView) view.findViewById(R.id.inquiry_title);
            holder.inquiry_num = (TextView) view.findViewById(R.id.inquiry_num);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.inquiry_title.setText(strings[position]);

        return view;
    }


    class ViewHolder {
        TextView inquiry_title, inquiry_num;
    }

}
