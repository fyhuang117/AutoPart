package com.autoparts.sellers.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.autoparts.sellers.R;
import com.autoparts.sellers.adapter.FilterListAdapter;
import com.autoparts.sellers.adapter.PopupListAdapter;

/**
 * Created by:Liuhuacheng
 * Created time:14-12-4
 */
public class PopupWindowUtil {
    private PopupWindow popupWindow;
    private String[] strings;
    private BaseAdapter adapter;
    public int position = 0;
    int x = 5;

    public void show(final Context context, View showView, final String[] strings, final CallBackName callBackName) {
        this.strings = strings;
        View view = LayoutInflater.from(context).inflate(R.layout.popup_item, null);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        adapter = new PopupListAdapter(context, strings);

        if (position == 1) {
            listView.setBackgroundResource(R.drawable.lib_phone_charge_kuang_bj);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            listView.setLayoutParams(params);
            adapter = new FilterListAdapter(context, strings);

        }
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Utils.showToastShort(context,"click="+lists.get(position));
                callBackName.name(position);
                popupWindow.dismiss();
            }
        });

        popupWindow = new PopupWindow(context);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        // popupWindow.setFocusable(true); // 设置PopupWindow可获得焦点
        popupWindow.setTouchable(true); // 设置PopupWindow可触摸
        popupWindow.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸

        popupWindow.setContentView(view);

        int width = Utils.getScreenWidth(context) / 2;
        int height = (int) context.getResources().getDimension(R.dimen.popup_view_height);
        if (position == 1){
            height = Utils.getScreenHeight(context);
            width = (int) (Utils.getScreenWidth(context)*0.6f);
            x = 0;
        }

        popupWindow.setWidth(width);

        popupWindow.setHeight(height);

        popupWindow.setAnimationStyle(R.style.PopupStyle); // 设置
        // popupWindow
        popupWindow.setFocusable(true);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);                                                                // 动画样式
//		}
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {

            }
        });
//        popupWindow.showAtLocation(showView,Gravity.BOTTOM, 0, 0);

        popupWindow.showAsDropDown(showView, x, 0);
        popupWindow.update();
    }

    public interface CallBackName {
        public void name(int position);
    }
}
