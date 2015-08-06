package com.autoparts.sellers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.autoparts.sellers.R;

import java.util.ArrayList;

public class SelectItemAdapter extends BaseAdapter{
    
    private Context context;
    private ArrayList<String> dataList;
    
    public SelectItemAdapter(Context context, ArrayList<String> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
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
        TextView textView = null;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.select_listview_item, parent, false);
            textView = (TextView)view.findViewById(R.id.select_item_content);
            view.setTag(textView);
        }else{
            textView = (TextView)view.getTag();
        }
        textView.setText(dataList.get(position));
        return view;
    }
    
}
