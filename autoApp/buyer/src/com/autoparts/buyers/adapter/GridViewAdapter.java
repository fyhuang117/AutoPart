package com.autoparts.buyers.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.autoparts.buyers.R;
import com.autoparts.buyers.utils.CommonData;
import com.autoparts.buyers.utils.ImageLoaderBitmapUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.HashMap;
import java.util.Vector;

/**
 * 图片加载适配器
 * Created by:Liuhuacheng
 * Created time:14-9-6
 */
public class GridViewAdapter extends BaseAdapter {

    private Vector<HashMap<String, Object>> mList;
    private Context context;
    private LayoutInflater mInflater;
    private Handler handler;
    private int width = 100;
    private ImageSize targetSize;
    private LinearLayout.LayoutParams params;
    private boolean isMeasureWidth = false;
    private int sortPosition = 0;

    public void setPosition(int position) {
        this.sortPosition = position;
    }

    private DisplayImageOptions options;

    public void getOptions() {
        options = ImageLoaderBitmapUtil.getInstance(context).getOptionsLoading(R.drawable.icon_photo_default);
    }

    public void setData(Vector<HashMap<String, Object>> mList) {
        this.mList = mList;
    }

    public GridViewAdapter(Context context, Vector<HashMap<String, Object>> mList) {
        this.context = context;
        this.mList = mList;
        mInflater = LayoutInflater.from(context);
        getOptions();
        width = 100;
        targetSize = new ImageSize(width, width);

    }


    @Override
    public int getCount() {
        if (mList != null && mList.size() > 0) {
            return mList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.inquiry_sort_grid_item, parent, false);
            holder = new ViewHolder();

            holder.ItemImage = (ImageView) convertView.findViewById(R.id.ItemImage);
            holder.ItemText = (TextView) convertView.findViewById(R.id.ItemText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (sortPosition == 0) {
            if (position < CommonData.wear_icons.length) {
                holder.ItemImage.setImageResource(CommonData.wear_icons[position]);
            }
        } else {
            if (position < CommonData.consumable_icons.length) {
                holder.ItemImage.setImageResource(CommonData.consumable_icons[position]);
            }
        }
        String nam = (String) mList.get(position).get("nam");
        holder.ItemText.setText(nam);

        return convertView;
    }

    class ViewHolder {
        ImageView ItemImage;
        TextView ItemText;
    }

}
