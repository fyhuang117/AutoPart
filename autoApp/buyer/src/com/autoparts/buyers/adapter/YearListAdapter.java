package com.autoparts.buyers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.autoparts.buyers.R;

import java.util.HashMap;
import java.util.Vector;

/**
 * 车型选择-详细车型
 * Created by:Liuhuacheng
 * Created time:15-3-6
 */
public class YearListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private int group_open_position = -1;
    private Vector<HashMap<String, Object>> mList;

    public void setData(Vector<HashMap<String, Object>> mList) {

        this.mList = mList;
    }

    public YearListAdapter(Context context, Vector<HashMap<String, Object>> mList) {
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
        Vector<HashMap<String, Object>> maps = (Vector<HashMap<String, Object>>) mList.get(i).get("car");
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
        Vector<HashMap<String, Object>> maps = (Vector<HashMap<String, Object>>) mList.get(i).get("car");
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

        String content = (String) mList.get(i).get("year");
        group_name.setText(content);
        return v;
    }


    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.expandablechilditem, null);
        TextView appName = (TextView) v.findViewById(R.id.child_content);
        Vector<HashMap<String, Object>> maps = (Vector<HashMap<String, Object>>) mList.get(i).get("car");

        String content = (String) maps.get(i1).get("nam");
//        String content = childs[i][i1];
        appName.setText(content);
        return v;
    }


    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


}
