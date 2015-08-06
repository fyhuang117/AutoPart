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
 * 优惠券
 * Created by:Liuhuacheng
 * Created time:15-3-6
 */
public class MenuCouponAdapter extends BaseAdapter {

    private Vector<HashMap<String, Object>> mList;
    private Context context;
    private int layoutView = -1;

    public void setData(Vector<HashMap<String, Object>> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void setView(int layoutView){
       this.layoutView = layoutView;
    }

    public MenuCouponAdapter(Context context, Vector<HashMap<String, Object>> mList) {
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
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.fragment_coupon_item, parent, false);
            holder = new ViewHolder();
            holder.coupon_desc = (TextView) view.findViewById(R.id.coupon_desc);
            holder.coupon_title = (TextView) view.findViewById(R.id.coupon_title);
            holder.coupon_content = (TextView) view.findViewById(R.id.coupon_content);
            holder.coupon_time = (TextView) view.findViewById(R.id.coupon_time);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        HashMap<String, Object> map = mList.get(position);

        holder.coupon_desc.setText("￥"+map.get("price"));
        holder.coupon_title.setText("优惠券");
        holder.coupon_content.setText("最低消费"+map.get("need_min")+"元");
        holder.coupon_time.setText("有效期至："+map.get("end_date"));


//        "price":"额度(int)",
//                "need_min":"最低消费(int)",
//                "begin_date":"开始时间",
//                "end_date":"结束时间",
//                "date_status":"是否有效(int 0-失效 1-有效)",
//                "use_status":"是否已使用(int 0-未使用 1-已使用)"

        return view;
    }


    class ViewHolder {
        TextView coupon_desc,coupon_title,coupon_content,coupon_time;
    }

}
