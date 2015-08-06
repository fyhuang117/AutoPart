package com.autoparts.sellers.fragment;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.autoparts.sellers.R;
import com.autoparts.sellers.activity.InquirySortListActivity;
import com.autoparts.sellers.adapter.GridViewAdapter;
import com.autoparts.sellers.utils.CommonData;
import com.autoparts.sellers.view.LineGrildView;

/**
 * Created by:Liuhuacheng
 * Created time:15-5-22
 */
public class SortHotFragment extends Fragment implements OnItemClickListener {
    int mNum;
    private Context context;
	private LineGrildView gridView;
	private List<String> mLists;
    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    public SortHotFragment newInstance(int num) {
        SortHotFragment f = new SortHotFragment();
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
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    /**
     * The Fragment's UI is just a simple text view showing its
     * instance number.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.inquiry_sort_hot, container, false);
        gridView = (LineGrildView) v.findViewById(R.id.gridView_hot);
        gridView.setOnItemClickListener(this);
        return v;
    }
   
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLists = CommonData.getSort();

       GridViewAdapter adapter = new GridViewAdapter(context, mLists);
       gridView.setAdapter(adapter);

    }

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		 Intent intent = new Intent(context, InquirySortListActivity.class);
	     intent.putExtra("title",mLists.get(position));
	     intent.putExtra("position",position);
	     startActivityForResult(intent, CommonData.REQUEST_CODE_SORT);
		
	}
    

}