package com.autoparts.sellers.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.autoparts.sellers.R;
import com.autoparts.sellers.utils.ImageLoaderBitmapUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class GridAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;
    private RelativeLayout.LayoutParams params;
    public int width = 0;

    private DisplayImageOptions options;

    public void getOptions() {
        options = ImageLoaderBitmapUtil.getInstance(context).getOptionsLoading(R.drawable.icon_photo_default);
    }

    public void setData(List<String> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }
    public GridAdapter(Context context, List<String> list) {
        super();
        this.list = list;
        this.context = context;
        getOptions();

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder;
        if (v == null) {
            holder = new Holder();
            v = LayoutInflater.from(context).inflate(R.layout.gridview_item, null);
            holder.grid_image = (ImageView) v.findViewById(R.id.grid_image);
            holder.grid_del = (ImageView) v.findViewById(R.id.grid_del);
            v.setTag(holder);
        } else {
            holder = (Holder) v.getTag();
        }
        String picUrl = list.get(position);
        if (!TextUtils.isEmpty(picUrl)) {
            ImageLoader.getInstance().displayImage(picUrl, holder.grid_image, options, null);
        }
        return v;

    }


    private class Holder {
        ImageView grid_image,grid_del;
    }
}
