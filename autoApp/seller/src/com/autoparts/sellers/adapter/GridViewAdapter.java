package com.autoparts.sellers.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.autoparts.sellers.R;
import com.autoparts.sellers.utils.CommonData;
import com.autoparts.sellers.utils.ImageLoaderBitmapUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.List;

/**
 * 图片加载适配器
 * Created by:Liuhuacheng
 * Created time:14-9-6
 */
public class GridViewAdapter extends BaseAdapter {

    public List<String> lists;
    private Context context;
    private DisplayImageOptions options;
    private LayoutInflater mInflater;
    private Handler handler;
    private int width = 100;
    private ImageSize targetSize;
    private LinearLayout.LayoutParams params;
    private boolean isMeasureWidth = false;
    
    public void getOptions() {
        options = ImageLoaderBitmapUtil.getInstance(context).getPicOptions();
    }

    public void setData(List<String> lists) {
        this.lists = lists;
    }

    public GridViewAdapter(Context context, List<String> lists) {
        this.context = context;
        this.lists = lists;
        mInflater = LayoutInflater.from(context);
        getOptions();
        width = 100;
        targetSize = new ImageSize(width, width);

    }


    @Override
    public int getCount() {
        if (lists != null && lists.size() > 0) {
            return lists.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
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
        //设置前三个图片
        if (position<CommonData.imgList.length) {
        	holder.ItemImage.setImageResource(CommonData.imgList[position]);
		}
        
        
        holder.ItemText.setText(lists.get(position));
        return convertView;
    }

    class ViewHolder {
        ImageView ItemImage;
        TextView ItemText;
    }

}
