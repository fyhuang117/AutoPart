package com.autoparts.buyers.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.autoparts.buyers.R;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.view.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * 询价中
 * Created by:Liuhuacheng
 * Created time:15-3-6
 */
public class MyInquiryAdapter extends BaseAdapter {

    public Vector<HashMap<String, Object>> mList;
    private Context context;
    private Handler handler;
    private int num = 0;
    private int state = 0;//0代表进行中的全部(待付款，待发货，待收货)，1待评价，2已完成

    public void setNum(int num) {
        this.num = num;
    }

    public void setData(Vector<HashMap<String, Object>> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public MyInquiryAdapter(Context context, Vector<HashMap<String, Object>> mList, Handler handler) {
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
            view = LayoutInflater.from(context).inflate(R.layout.myinquiry_item, parent, false);
            holder = new ViewHolder();
            holder.inquiry_title = (TextView) view.findViewById(R.id.inquiry_title);
            holder.inquiry_name = (TextView) view.findViewById(R.id.inquiry_name);
            holder.inquiry_num = (TextView) view.findViewById(R.id.inquiry_num);
            holder.inquiry_state = (TextView) view.findViewById(R.id.inquiry_state);
            holder.line = view.findViewById(R.id.line);
            holder.inquiry_min_money = (TextView) view.findViewById(R.id.inquiry_min_money);
            holder.inquiry_seller_num = (TextView) view.findViewById(R.id.inquiry_seller_num);
            holder.inquiry_no = (TextView) view.findViewById(R.id.inquiry_no);
            holder.inquiry_data_view = (LinearLayout) view.findViewById(R.id.inquiry_data_view);

            holder.inquiry_btn_view = (LinearLayout) view.findViewById(R.id.inquiry_btn_view);
            holder.order_del = (Button) view.findViewById(R.id.order_del);
            holder.order_contact = (Button) view.findViewById(R.id.order_contact);
            holder.order_pay = (Button) view.findViewById(R.id.order_pay);
            holder.order_evaluate = (Button) view.findViewById(R.id.order_evaluate);
            holder.order_receipt = (Button) view.findViewById(R.id.order_receipt);

            holder.mGridView = (GridView) view.findViewById(R.id.mGridView);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        HashMap<String, Object> maps = mList.get(position);
        holder.inquiry_title.setText((String) maps.get("par"));
        holder.inquiry_name.setText((String) maps.get("car"));
        int sta = Integer.parseInt((String) maps.get("sta"));
        String state = CommonData.getOrderState(sta);
        holder.inquiry_state.setText(state);
        String num = (String) maps.get("num");
        holder.inquiry_no.setText("订单号:" + num);
        setViewData(holder, maps);

        holder.inquiry_btn_view.setVisibility(View.VISIBLE);
        holder.order_pay.setVisibility(View.GONE);
        holder.order_del.setVisibility(View.GONE);
        holder.order_contact.setVisibility(View.GONE);
        holder.order_evaluate.setVisibility(View.GONE);
        holder.order_receipt.setVisibility(View.GONE);

        if (sta == 0){//待付款
            holder.order_pay.setVisibility(View.VISIBLE);
        }else if (sta == 1){//待发货
            holder.order_contact.setVisibility(View.VISIBLE);
        }else if (sta == 4){//待收货
            holder.order_receipt.setVisibility(View.VISIBLE);
            holder.order_contact.setVisibility(View.VISIBLE);
        }else if (sta == 5){//待评价
            holder.order_evaluate.setVisibility(View.VISIBLE);
        }

//        holder.inquiry_data_view.setVisibility(View.GONE);
//        holder.inquiry_btn_view.setVisibility(View.VISIBLE);
//        holder.inquiry_seller_num.setVisibility(View.GONE);
//        if (num == 1) {//待评价
//            String state = CommonData.getOrderState(4);
//            holder.inquiry_state.setText(state);
//            holder.order_del.setVisibility(View.GONE);
//            holder.order_pay.setVisibility(View.GONE);
//            holder.inquiry_no.setVisibility(View.VISIBLE);
//            holder.inquiry_min_money.setText("成交价格：￥" + "9800.0");
//        } else {//已完成
//            holder.inquiry_min_money.setText("成交价格：￥" + "9800.0");
//            holder.inquiry_no.setVisibility(View.VISIBLE);
//            holder.order_evaluate.setVisibility(View.GONE);
//            holder.order_pay.setVisibility(View.GONE);
//            String state = CommonData.getOrderState(5);
//            holder.inquiry_state.setText(state);
//        }


        onClick(holder, position);

        return view;
    }


    class ViewHolder {
        TextView inquiry_title, inquiry_name, inquiry_num;
        TextView inquiry_state, inquiry_no;
        TextView inquiry_min_money, inquiry_seller_num;
        View line;
        LinearLayout inquiry_btn_view, inquiry_data_view;
        Button order_del, order_contact, order_pay, order_evaluate,order_receipt;
        private GridView mGridView;
    }

    public void onClick(ViewHolder holder, final int positio) {

        holder.order_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.arg1 = positio;
                message.what = 1;
                handler.sendMessage(message);
            }
        });
        holder.order_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.arg1 = positio;
                message.what = 2;
                handler.sendMessage(message);
            }
        });
        holder.order_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.arg1 = positio;
                message.what = 3;
                handler.sendMessage(message);
            }
        });
        holder.order_evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.arg1 = positio;
                message.what = 4;
                handler.sendMessage(message);
            }
        });
        holder.order_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.arg1 = positio;
                message.what = 5;
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

    public void setItemClick(GridView mGridView,final List<String> mGridPhotos){
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommonData.getInstance(context).showImage(context,mGridPhotos,position);
            }
        });
    }
}
