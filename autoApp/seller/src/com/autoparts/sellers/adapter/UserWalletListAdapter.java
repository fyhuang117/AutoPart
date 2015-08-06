package com.autoparts.sellers.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.autoparts.sellers.R;
import com.autoparts.sellers.utils.CommonData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * 我的账单详情
 * Created by:Liuhuacheng
 * Created time:15-3-6
 */
public class UserWalletListAdapter extends BaseAdapter {
    public Vector<HashMap<String, Object>> mList;
    private Context context;


    public void setData(Vector<HashMap<String, Object>> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public UserWalletListAdapter(Context context, Vector<HashMap<String, Object>> mList) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.user_wallet_item, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.time = (TextView) view.findViewById(R.id.time);
            holder.money = (TextView) view.findViewById(R.id.money);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        HashMap<String, Object> maps = mList.get(position);

        String type = (String) maps.get("type");
        String price = (String) maps.get("price");
        if (type.equals("0")){
            holder.title.setText("收入");
            holder.money.setText("+"+price);
            holder.money.setTextColor(context.getResources().getColor(R.color.green));

        }else {
            holder.title.setText("提现");
            holder.money.setText("-"+price);
            holder.money.setTextColor(context.getResources().getColor(R.color.grey));
        }
        String time = (String) maps.get("time");

        holder.time.setText(time);

        return view;
    }


    class ViewHolder {
        TextView title, time,money;
    }


}
