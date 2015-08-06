package com.autoparts.buyers.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.autoparts.buyers.R;
import com.autoparts.buyers.network.JsonParserUtils;
import com.autoparts.buyers.utils.Utils;

import java.util.HashMap;
import java.util.Vector;

/**
 * 车型选择-详细车型
 * Created by:Liuhuacheng
 * Created time:15-3-6
 */
public class ExpandListViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private int group_open_position = -1;
    private Vector<HashMap<String, Object>> mList;

    public void setData(Vector<HashMap<String, Object>> mList) {

        this.mList = mList;
    }

    public ExpandListViewAdapter(Context context,Vector<HashMap<String, Object>> mList) {
        this.context = context;
        this.mList = mList;
    }

    public int getGroupCount() {
        if (mList == null){
            return 0;
        }
        return mList.size();
    }

    public int getChildrenCount(int i) {
        Vector<HashMap<String, Object>> maps = (Vector<HashMap<String, Object>>) mList.get(i).get("line");
        if (maps == null){
            return 0;
        }else {
            return maps.size();
        }
    }

    public Object getGroup(int i) {
        return mList.get(i);
    }


    public Object getChild(int i, int i1) {
        Vector<HashMap<String, Object>> maps = (Vector<HashMap<String, Object>>) mList.get(i).get("line");
        return maps;
    }
    public long getGroupId(int i) {
        return i;
    }

    public long getChildId(int i, int i1) {
        return i1;
    }


    public boolean hasStableIds() {
        return false;
    }


    public View getGroupView(int i, boolean isExpanded, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.expandablegroupitem, null);
        TextView group_name = (TextView) v.findViewById(R.id.group_name);

        String content = (String) mList.get(i).get("name");
        group_name.setText(content);
        return v;
    }


    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.expandablechilditem, null);
        TextView appName = (TextView) v.findViewById(R.id.child_content);
        Vector<HashMap<String, Object>> maps = (Vector<HashMap<String, Object>>) mList.get(i).get("line");

        String content = (String) maps.get(i1).get("nam");
//        String content = childs[i][i1];
        appName.setText(content);
        return v;
    }


    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


}