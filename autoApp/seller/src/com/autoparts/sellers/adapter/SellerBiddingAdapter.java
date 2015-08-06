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
import com.autoparts.sellers.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * 竞价中
 * Created by:Liuhuacheng
 * Created time:15-3-6
 */
public class SellerBiddingAdapter extends BaseAdapter {
    private Context context;
    private Vector<HashMap<String, Object>> mList;
    private Handler handler;
    private int num = 0;
    private int state = 0;//0代表进行中的全部，1代表竞价中，2代表待付款，3代表待发货

    public void setNum(int num) {
        this.num = num;
    }

    public void setData(Vector<HashMap<String, Object>> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public SellerBiddingAdapter(Context context,Vector<HashMap<String, Object>> mList, Handler handler) {
        this.mList = mList;
        this.context = context;
        this.handler = handler;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.seller_bidding_item, parent, false);
            holder = new ViewHolder();
            holder.inquiry_title = (TextView) view.findViewById(R.id.inquiry_title);
            holder.inquiry_name = (TextView) view.findViewById(R.id.inquiry_name);
            holder.mGridView = (GridView) view.findViewById(R.id.mGridView);
            holder.inquiry_cancel = (TextView) view.findViewById(R.id.inquiry_cancel);
            holder.inquiry_time = (TextView) view.findViewById(R.id.inquiry_time);
            holder.line = view.findViewById(R.id.line);
            holder.inquiry_min_money = (TextView) view.findViewById(R.id.inquiry_min_money);
            holder.inquiry_btn_view = (LinearLayout) view.findViewById(R.id.inquiry_btn_view);
            holder.inquiry_data_view = (LinearLayout) view.findViewById(R.id.inquiry_data_view);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        HashMap<String, Object> maps = mList.get(position);
        holder.inquiry_data_view.setVisibility(View.VISIBLE);
        holder.inquiry_btn_view.setVisibility(View.GONE);

        String par = (String) maps.get("par");
        String car = (String) maps.get("car");
        if (TextUtils.isEmpty(par)) {
            par = context.getString(R.string.inquiry_parts_null);
        }
        if (TextUtils.isEmpty(car)) {
            car = context.getString(R.string.inquiry_car_null);
        }

        holder.inquiry_title.setText(par);
        holder.inquiry_name.setText(car);

//        String num = (String) maps.get("num");
        String pri = (String) maps.get("pri");
        holder.inquiry_min_money.setText("报价:"+pri);
        setViewData(holder, maps);

        String begin_time = (String) maps.get("begin_time");
        String end_time = (String) maps.get("end_time");

        Utils.showLog(position,"begin_time="+begin_time);
        Utils.showLog(position,"end_time="+end_time);
        if (!TextUtils.isEmpty(begin_time)) {
            String time = CommonData.getInstance(context).getTime(begin_time, end_time);
            Utils.showLog(position,"time="+time);
            holder.inquiry_time.setText(time);
        }


        onClick(holder, position);

        return view;
    }


    class ViewHolder {
        TextView inquiry_title,inquiry_name;
        TextView inquiry_time, inquiry_cancel;
        TextView inquiry_min_money;
        View line;
        LinearLayout inquiry_btn_view, inquiry_data_view;
        private GridView mGridView;
    }

    public void onClick(ViewHolder holder, final int positio) {
        holder.inquiry_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.arg1 = positio;
                message.what = 0;
                handler.sendMessage(message);
            }
        });

    }

    public void setViewData(ViewHolder holder, HashMap<String, Object> maps) {
        List<String> mGridPhotos = new ArrayList<String>();
        GridAdapter mGridadapter = new GridAdapter(context, mGridPhotos);

        String pic1 = (String) maps.get("pic1");
        String pic2 = (String) maps.get("pic2");
        String pic3 = (String) maps.get("pic3");

        if (!TextUtils.isEmpty(pic1)) {
            mGridPhotos.add(pic1);
        }
        if (!TextUtils.isEmpty(pic2)) {
            mGridPhotos.add(pic2);
        }
        if (!TextUtils.isEmpty(pic3)) {
            mGridPhotos.add(pic3);
        }
        mGridadapter.setData(mGridPhotos);
        holder.mGridView.setAdapter(mGridadapter);
        setItemClick(holder.mGridView,mGridPhotos);

    }


    public void setItemClick(GridView mGridView, final List<String> mGridPhotos){
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommonData.getInstance(context).showImage(context,mGridPhotos,position);
            }
        });
    }


}
