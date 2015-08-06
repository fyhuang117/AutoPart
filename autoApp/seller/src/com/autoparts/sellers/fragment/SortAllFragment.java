package com.autoparts.sellers.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.autoparts.sellers.R;
import com.autoparts.sellers.activity.InquirySortListActivity;
import com.autoparts.sellers.adapter.InquirySortlistAdapter;
import com.autoparts.sellers.model.CommonLetterModel;
import com.autoparts.sellers.model.ContactUtils;
import com.autoparts.sellers.utils.CommonData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by:Liuhuacheng
 * Created time:15-5-22
 */
public class SortAllFragment extends ListFragment {
    int mNum;
    private List<CommonLetterModel> mList;
    private ContactUtils contactUtils;
    private Context context;

    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    public SortAllFragment newInstance(int num) {
        SortAllFragment f = new SortAllFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        contactUtils = new ContactUtils();
        mList = new ArrayList<CommonLetterModel>();
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    /**
     * The Fragment's UI is just a simple text view showing its
     * instance number.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.viewpager_item, container, false);
//            textViewMaps.put(mNum, (TextView) v.findViewById(R.id.viewpager_item_text));
        ListView listView = (ListView) v.findViewById(android.R.id.list);
//            listView.setTextFilterEnabled(true);
//            listViewMaps.put(mNum, listView);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mList = CommonData.getSortList();
        mList = contactUtils.getListKey(mList);
        InquirySortlistAdapter listAdapter = new InquirySortlistAdapter(context, mList);
//            mIndexer = contactUtils.getmIndexer();

        setListAdapter(listAdapter);
//            adapterMaps.put(mNum, adapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(context, InquirySortListActivity.class);
        intent.putExtra("title", mList.get(position).getUser_name());
        intent.putExtra("position",  mList.get(position).getUser_position());
        startActivityForResult(intent, 0);
    }
}