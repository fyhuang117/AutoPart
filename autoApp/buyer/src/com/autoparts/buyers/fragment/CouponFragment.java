package com.autoparts.buyers.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.autoparts.buyers.R;
import com.autoparts.buyers.adapter.MenuCouponAdapter;
import com.autoparts.buyers.network.HttpClientUtils;
import com.autoparts.buyers.network.HttpResultHandler;
import com.autoparts.buyers.network.ResponseModel;
import com.autoparts.buyers.utils.Constants;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 优惠券Fragment
 * Created by:Liuhuacheng
 * Created time:15-4-27
 */
public class CouponFragment extends ListFragment {
    int mNum;
    private Vector<HashMap<String, Object>> mList;
    private MenuCouponAdapter adapter;
    private ListView listView;
    private LinearLayout data_null_view;

    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    public CouponFragment newInstance(int num) {
        CouponFragment f = new CouponFragment();
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
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        mList = new Vector<HashMap<String, Object>>();
    }

    /**
     * The Fragment's UI is just a simple text view showing its
     * instance number.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.viewpager_item, container, false);
        listView = (ListView) v.findViewById(android.R.id.list);
        data_null_view = (LinearLayout) v.findViewById(R.id.data_null_view);
        listView.setDivider(null);
        int divider = (int) getResources().getDimension(R.dimen.common_margin);
        listView.setDividerHeight(divider);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new MenuCouponAdapter(getActivity(), mList);
        setListAdapter(adapter);
        getData();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//        Intent intent = new Intent(getActivity(), InquirySortListActivity.class);
//        startActivity(intent);
    }

    //获取优惠券
    public void getData() {
        String url = Constants.USER_COUPON;
        Map<String, Object> params = new HashMap<String, Object>();
        HttpClientUtils.post(getActivity(), url, params, new HttpResultHandler() {
            @Override
            public void onResultFail(String message, int statusCode) {
                super.onResultFail(message, statusCode);
            }

            @Override
            public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode) {
                super.onResultSuccess(headers, response, message, statusCode);
                setData(response);
            }

            @Override
            public void onResultJson(String json) {
                super.onResultJson(json);
            }
        });

    }

    public void setData(ResponseModel responseModel) {
        mList = responseModel.getMaps();
        if (mList != null && mList.size() > 0) {
            adapter.setData(mList);
            setDataNull(false);
        } else {
            setDataNull(true);
        }
    }

    public void setDataNull(boolean b) {
        if (b) {
            if (data_null_view != null) {
                data_null_view.setVisibility(View.VISIBLE);
            }
            listView.setVisibility(View.GONE);

        } else {
            listView.setVisibility(View.VISIBLE);
            if (data_null_view != null) {
                data_null_view.setVisibility(View.GONE);
            }
        }
    }
}