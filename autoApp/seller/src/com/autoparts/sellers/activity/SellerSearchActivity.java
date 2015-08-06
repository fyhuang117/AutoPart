package com.autoparts.sellers.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.autoparts.sellers.R;
import com.autoparts.sellers.utils.CameraUtils;
import com.autoparts.sellers.utils.CommonData;
import com.autoparts.sellers.utils.DialogUtil;
import com.autoparts.sellers.utils.Utils;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.util.ArrayList;
import java.util.List;

/**
 * 过滤竞价单
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class SellerSearchActivity extends BaseActivity {
    private Context context;
    private int position = 0;//position ==0 询价配件，==1易损件 == 2询价汽保耗材
    private TextView inquiry_model, inquiry_year, inquiry_sort, brand;

    private LinearLayout model_view;
    private String sortName;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.seller_search);
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra("title");
        setTitle(title);
        setRightView("确定", -1);
        setLeftView("", R.drawable.icon_back);
        init();
    }

    private void init() {
        context = this;
        position = getIntent().getIntExtra("position", 0);
        inquiry_model = (TextView) findViewById(R.id.inquiry_model);
        inquiry_year = (TextView) findViewById(R.id.inquiry_year);
        inquiry_sort = (TextView) findViewById(R.id.inquiry_sort);
        brand = (TextView) findViewById(R.id.brand);
        model_view = (LinearLayout) findViewById(R.id.model_view);

    }

    //车型选择
    public void model(View view) {
        Intent intent = new Intent(context, InquiryModelActivity.class);
        startActivityForResult(intent, CommonData.REQUEST_CODE_MODEL);
    }

    //年款
    public void year(View view) {
        if (TextUtils.isEmpty(id_model)) {
            Utils.showToastShort(context, "请先选择车型");
        } else {
            Intent intent = new Intent(context, InquiryYearActivity.class);
            intent.putExtra(CommonData.INQUIRE_ID, id_model);
            startActivityForResult(intent, CommonData.REQUEST_CODE_YEAR);
        }
    }

    //配件分类
    public void sort(View view) {
        Intent intent = null;
        intent = new Intent(context, InquirySortNewActivity.class);
        startActivityForResult(intent, CommonData.REQUEST_CODE_SORT);
    }

    //配件品牌
    public void category(View view) {
        if (TextUtils.isEmpty(parttypeid)) {
            Utils.showToastShort(context, "请先选择配件分类");
        } else if (parttypeid.equals("-1")) {
            Utils.showToastShort(context, "该配件暂无相关品牌");
        } else {
            Intent intent = new Intent(context, SelectModeActivity.class);
            intent.putExtra("title", "配件品牌");
            intent.putExtra(CommonData.INQUIRE_ID, parttypeid);//"parttypeid":"配件类型标识(int)"
            startActivityForResult(intent, CommonData.REQUEST_INQUIRY_BRAND);
        }
    }


    //询价提交
    public void commit(View view) {
        String all = getString(R.string.search_all);
        inquiry_model.setText(all);
        inquiry_year.setText(all);
        inquiry_sort.setText(all);
        brand.setText(all);

        id_model = "";//选择车型 ID
        parttypeid = "";//配件ID
        year_id = "";//年款 ID
        partbandid = "";//品牌 ID
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.topBar_right_layout:
                Intent intent = new Intent();
                intent.putExtra("model", id_model);
                intent.putExtra("year", year_id);
                intent.putExtra("sort", parttypeid);
                intent.putExtra("brand", partbandid);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    private String name_model = "";//选择车型 名称
    private String id_model = "";//选择车型 ID
    private String parttypeid = "";//配件ID
    private String year_name = "";//年款 名称
    private String year_id = "";//年款 ID
    private String partbandid = "";//品牌 ID

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {//听力测试结束
            switch (requestCode) {
                case CommonData.REQUEST_CODE_MODEL:
                    String name = data.getStringExtra(CommonData.INQUIRE_NAME);
                    String id = data.getStringExtra(CommonData.INQUIRE_ID);
                    name_model = name;
                    id_model = id;
                    inquiry_model.setText(name_model);

                    year_name = "";
                    year_id = "";
                    inquiry_year.setText(year_name);
                    break;
                case CommonData.REQUEST_CODE_YEAR:
                    year_name = data.getStringExtra(CommonData.INQUIRE_NAME);
                    year_id = data.getStringExtra(CommonData.INQUIRE_ID);
                    inquiry_year.setText(year_name);
                    break;
                case CommonData.REQUEST_CODE_SORT:
                    String sort = data.getStringExtra(CommonData.INQUIRE_NAME);
                    parttypeid = data.getStringExtra(CommonData.INQUIRE_ID);
//                    Utils.showToastShort(context,"parttypeid=="+parttypeid);
                    inquiry_sort.setText(sort);
                    break;

                case CommonData.REQUEST_INQUIRY_BRAND:
                    partbandid = data.getStringExtra(CommonData.INQUIRE_ID);
                    String partband_name = data.getStringExtra(CommonData.INQUIRE_NAME);
                    brand.setText(partband_name);//品牌
                    break;

            }
        }
    }


}
