package com.autoparts.sellers.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.autoparts.sellers.R;
import com.autoparts.sellers.adapter.CommonListAdapter;
import com.autoparts.sellers.utils.DialogUtil;
import com.autoparts.sellers.utils.Utils;

/**
 * 询价-配件
 * Created by:Liuhuacheng
 * Created time:15-3-5
 */
public class CommonListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private Context context;
    private CommonListAdapter adapter;
    private ListView mListView;
    String title = "";
    String[] strings = null;
    String[] strings1 = {"质保一年", "一年内免费维修", "两年内免费更换", "质保两年","无"};
    String[] strings2 = {"否", "是"};
    String[] strings3 = {"货到付款", "在线支付"};

    String[] strings4 = {"快递", "自提"};

    String[] strings5 = {"副厂", "原厂"};
    private int position;
    private Dialog expressDialog;
    private TextView express_ok, express_cancel;
    private EditText express_name, express_money;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.common_list_view);
        super.onCreate(savedInstanceState);
        title = getIntent().getStringExtra("title");
        setTitle(title);
        init();

    }

    private void init() {
        context = this;
        position = getIntent().getIntExtra("position", 0);
        strings3 = getResources().getStringArray(R.array.pay);
        if (position == 0) {
            strings = strings1;
        } else if (position == 1) {
            strings = strings2;
        } else if (position == 2) {
            strings = strings3;
        } else if (position == 3) {
            strings = strings4;
        } else if (position == 4) {
            strings = strings5;
        }
        mListView = (ListView) findViewById(R.id.mListView);
        adapter = new CommonListAdapter(context, strings);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        initExpressDialog();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (position == 3 && strings[i].equals("快递")) {
            expressDialog.show();

        } else {
            Intent intent = new Intent();
            intent.putExtra("name", strings[i]);
            intent.putExtra("position", position);
            intent.putExtra("id", i + "");
            intent.putExtra("money", "0");
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    public void initExpressDialog() {
        expressDialog = DialogUtil.createDialog(this, R.layout.express_selected, R.style.CustomDialog);
        express_name = (EditText) expressDialog.findViewById(R.id.express_name);
        express_money = (EditText) expressDialog.findViewById(R.id.express_money);

        express_ok = (TextView) expressDialog.findViewById(R.id.express_ok);
        express_cancel = (TextView) expressDialog.findViewById(R.id.express_cancel);
        express_ok.setOnClickListener(this);
        express_cancel.setOnClickListener(this);
        expressDialog.setCancelable(false);
        DialogUtil.setDialogParams(this, expressDialog, R.dimen.dialog_width_margin);
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.express_ok:
                String name = express_name.getText().toString();
                String money = express_money.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Utils.showToastShort(context, "请先输入快递名称");
                } else {
                    if (TextUtils.isEmpty(money)) {
                        money = "0";
                    }
                    dimissDiaog();
                    Intent intent = new Intent();
                    intent.putExtra("name", name);
                    intent.putExtra("money", money);
                    intent.putExtra("position", position);
                    setResult(RESULT_OK, intent);
                    finish();
                }

                break;
            case R.id.express_cancel:
                dimissDiaog();
                break;
        }
    }


    public void dimissDiaog() {
        if (expressDialog != null && expressDialog.isShowing()) {
            expressDialog.dismiss();
        }
    }

}
