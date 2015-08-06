package com.autoparts.buyers.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.autoparts.buyers.R;
import com.autoparts.buyers.network.JsonParserUtils;
import com.autoparts.buyers.utils.Utils;

import java.util.HashMap;
import java.util.Vector;

/**
 * 询价中
 * Created by:Liuhuacheng
 * Created time:15-3-6
 */
public class MainOrderBeingAdapter extends BaseAdapter {

    private Context context;
    private Vector<HashMap<String, Object>> mList;

    public void setData(Vector<HashMap<String, Object>> mList) {
        this.mList = mList;
    }


    public MainOrderBeingAdapter(Context context, Vector<HashMap<String, Object>> mList) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mList.size();
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
            view = LayoutInflater.from(context).inflate(R.layout.main_order_being_item, parent, false);
            holder = new ViewHolder();
            holder.being_title = (TextView) view.findViewById(R.id.being_title);
            holder.being_state = (TextView) view.findViewById(R.id.being_state);
            holder.being_content = (TextView) view.findViewById(R.id.being_content);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        HashMap<String, Object> map = mList.get(position);
        String par = (String) map.get("par");
        String car = (String) map.get("car");
        String ban = (String) map.get("ban");
        Utils.showLog("ban===="+ban);

        if (TextUtils.isEmpty(par)){
            par = context.getString(R.string.inquiry_parts_null);
        }  if (TextUtils.isEmpty(car)){
            car = context.getString(R.string.inquiry_car_null);
        }
        holder.being_title.setText(par);
//        String value = bans[0];
        holder.being_content.setText(car);

        return view;
    }


    class ViewHolder {
        TextView being_title, being_state,being_content;
    }

}
