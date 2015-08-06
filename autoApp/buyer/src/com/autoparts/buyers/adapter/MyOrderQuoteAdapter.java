package com.autoparts.buyers.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.autoparts.buyers.R;
import com.autoparts.buyers.network.JsonParserUtils;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.ImageLoaderBitmapUtil;
import com.autoparts.buyers.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.HashMap;
import java.util.Vector;

/**
 * 我的订单--商家报价列表
 * Created by:Liuhuacheng
 * Created time:15-3-6
 */
public class MyOrderQuoteAdapter extends BaseAdapter {

    private Vector<HashMap<String, Object>> mList;
    private Context context;
    private String[] avis;
    private CommonData commonData;
    private DisplayImageOptions options;

    public void getOptions() {
        options = ImageLoaderBitmapUtil.getInstance(context).getOptionsLoading(R.drawable.icon_photo_default);
    }

    public void setData(Vector<HashMap<String, Object>> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }


    public MyOrderQuoteAdapter(Context context, Vector<HashMap<String, Object>> mList) {
        this.mList = mList;
        this.context = context;
        avis = context.getResources().getStringArray(R.array.avis);
        commonData = CommonData.getInstance(context);
        getOptions();
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
            view = LayoutInflater.from(context).inflate(R.layout.my_order_quote_item, parent, false);
            holder = new ViewHolder();
            holder.quote_title = (TextView) view.findViewById(R.id.quote_title);
            holder.quote_price = (TextView) view.findViewById(R.id.quote_price);
            holder.quote_content1 = (TextView) view.findViewById(R.id.quote_content1);
            holder.quote_content2 = (TextView) view.findViewById(R.id.quote_content2);
            holder.quote_content3 = (TextView) view.findViewById(R.id.quote_content3);
            holder.quote_image = (ImageView) view.findViewById(R.id.quote_image);
            holder.rank_image = (ImageView) view.findViewById(R.id.rank_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        HashMap<String, Object> map = mList.get(position);
        holder.quote_title.setText((String) map.get("nam"));
        String pri = (String) map.get("pri");
        holder.quote_price.setText("￥" + pri);
        int avi = CommonData.getInstance(context).getIntData((String) map.get("avi"));
        holder.quote_content1.setText(avis[avi]);
//        "avi":"是否现货(int)",
        String sell_level = (String) map.get("sell_level");//信用等级
        int level = commonData.getIntData(sell_level);
        if (level>15){
            level = 15;
        }
        holder.rank_image.setImageResource(CommonData.images[level-1]);
        String business_area = (String) map.get("business_area");//商圈
        if (TextUtils.isEmpty(business_area)){
            business_area = "无";
        }
        holder.quote_content2.setText(business_area);

        String sell_score = (String) map.get("sell_score");
        String[] scores = JsonParserUtils.jsonParseStringArray(sell_score);
        float total = 0;
        if (scores != null && scores.length>0){
            for (int i=0;i<scores.length;i++){
                total += Float.parseFloat(scores[i]);
            }
        }
        String fenshu = "0";
        if (total != 0){
            fenshu = Utils.getFloatFormat(total/3);
        }
        holder.quote_content3.setText("信用"+fenshu +"分");


        String sell_pic = (String) map.get("sell_pic");
        ImageLoaderBitmapUtil.getInstance(context).showListImage(sell_pic,holder.quote_image,options);

//        define('ORDER_STATUS_NOPAY',0);				//未支付
//        define('ORDER_STATUS_NOSEND',1);			//未发货
//        define('ORDER_STATUS_COMPLETE',2);			//完成
//        define('ORDER_STATUS_CANCEL',3);			//取消
//        define('ORDER_STATUS_SEND',4);				//已发货
//        define('ORDER_STATUS_SEND',5);				//待评价
//        "sell_pic":"",
//                "business_area":"",
//                "sell_level":1,
//                "sell_score":[
//        0,
//                0,
//                0
//        ],

        return view;
    }


    class ViewHolder {
        TextView quote_title, quote_price;
        TextView quote_content1, quote_content2,quote_content3;
        ImageView quote_image,rank_image;
    }

}
