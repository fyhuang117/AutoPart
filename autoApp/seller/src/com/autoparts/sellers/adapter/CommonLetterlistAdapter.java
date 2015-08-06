package com.autoparts.sellers.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.autoparts.sellers.R;
import com.autoparts.sellers.model.CommonLetterModel;
import com.autoparts.sellers.utils.ImageLoaderBitmapUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

/**
 * 快速索引
 * Created by:Liuhuacheng
 * Created time:15-3-6
 */
public class CommonLetterlistAdapter extends BaseAdapter {

    public List<CommonLetterModel> lists;
    private Context context;
    private DisplayImageOptions options;
    private LayoutInflater mInflater;

    public void getOptions() {

        options = ImageLoaderBitmapUtil.getInstance(context).getOptionsLoading(R.drawable.model_default_icon);
    }

    public void setData(List<CommonLetterModel> lists) {
        this.lists = lists;
    }

    public CommonLetterlistAdapter(Context context, List<CommonLetterModel> lists) {
        this.lists = lists;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        getOptions();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.common_letter_list_item, parent, false);
            holder = new ViewHolder();
            holder.contact_image = (ImageView) view.findViewById(R.id.contact_image);
            holder.contact_name = (TextView) view.findViewById(R.id.contact_name);
            holder.contact_alpha = (TextView) view.findViewById(R.id.contact_alpha);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        CommonLetterModel contactModel = lists.get(position);
        String currentStr = contactModel.getUser_key();//当前字母
        String previewStr = (position - 1) >= 0 ? lists.get(position - 1).getUser_key() : "";
        if (!previewStr.equals(currentStr)) {
            holder.contact_alpha.setVisibility(View.VISIBLE);
            holder.contact_alpha.setText(currentStr);
        } else {
            holder.contact_alpha.setVisibility(View.GONE);

        }
        holder.contact_name.setText(contactModel.getUser_name());
        String picUrl = contactModel.getUser_image();
        if (TextUtils.isEmpty(picUrl)) {
            holder.contact_image.setVisibility(View.GONE);
        } else {
            holder.contact_image.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(picUrl, holder.contact_image, options, new AnimateFirstDisplayListener());
        }
        return view;
    }


    class ViewHolder {
        ImageView contact_image;
        TextView contact_name, contact_alpha;
    }

    private class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

        }
    }

}
