package com.autoparts.sellers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.autoparts.sellers.R;

import java.util.HashMap;
import java.util.Vector;

/**
 * 我的消息
 * Created by:Liuhuacheng
 * Created time:15-3-6
 */
public class MyMessageAdapter extends BaseAdapter {

    private Vector<HashMap<String, Object>> mList;
    private Context context;

    public void setData(Vector<HashMap<String, Object>> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public MyMessageAdapter(Context context, Vector<HashMap<String, Object>> mList) {
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
            view = LayoutInflater.from(context).inflate(R.layout.user_message_item, parent, false);
            holder = new ViewHolder();
            holder.message_title = (TextView) view.findViewById(R.id.message_title);
            holder.message_content = (TextView) view.findViewById(R.id.message_content);
            holder.message_time = (TextView) view.findViewById(R.id.message_time);
            holder.message_state = (TextView) view.findViewById(R.id.message_state);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        HashMap<String, Object> map = mList.get(position);
        holder.message_title.setText((String) map.get("title"));
        holder.message_content.setText((String) map.get("content"));
        holder.message_time.setText((String) map.get("time"));

        String state = (String) map.get("read_status");
        if (state.equals("0")){
            holder.message_state.setText("未读");
            holder.message_state.setTextColor(context.getResources().getColor(R.color.bg_color));

        }else {
            holder.message_state.setText("已读");
            holder.message_state.setTextColor(context.getResources().getColor(R.color.grey_text));
        }


//        "message_id":"消息标识(int)",
//                "title":"标题",
//                "content":"内容",
//                "time ":"发送时间",
//                "read_status":"是否已读(int 0-未读 1-已读)"


        return view;
    }


    class ViewHolder {
        TextView message_title, message_content;
        TextView message_time, message_state;
    }

}
