package com.autoparts.buyers.adapter;

import android.content.Context;
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
public class MainDrawerAdapter extends BaseAdapter {

    public String[] strings;
    private Context context;
    private String count = "0";
    private int[] icons = {R.drawable.menu_icon_mine, R.drawable.menu_icon_account,R.drawable.menu_icon_account, R.drawable.menu_icon_info};


    public void setData(String count) {
        this.count = count;
        notifyDataSetChanged();
    }

    public MainDrawerAdapter(Context context, String[] strings) {
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
            view = LayoutInflater.from(context).inflate(R.layout.main_drawer_list_item, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.num = (TextView) view.findViewById(R.id.num);
            holder.menu_icon = (ImageView) view.findViewById(R.id.menu_icon);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.title.setText(strings[position]);
        if (position == 0) {
            if (count.equals("0")) {
                holder.num.setVisibility(View.GONE);
                holder.num.setText(count);
            }else {
                holder.num.setVisibility(View.VISIBLE);

            }
        } else {
            holder.num.setVisibility(View.GONE);
        }
        holder.menu_icon.setImageResource(icons[position]);
        return view;
    }


    class ViewHolder {
        TextView title, num;
        ImageView menu_icon;
    }

}
