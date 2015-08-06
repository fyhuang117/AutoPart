package com.autoparts.sellers.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.autoparts.sellers.R;
import com.autoparts.sellers.adapter.SelectItemAdapter;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 列表单项选择
 * Created by:haoming
 * Created time:15-5-1
 */
public class SelectItemActivity extends BaseActivity {

    private Context context;
    private Intent pIntent;
    public final static int ProvincesTitle = 10001;
    public final static int CityTitle = 10002;
    public final static int BankTitle = 10003;
    public final static int DistrictTitle = 10004;
    private int xmlResourceId = 0;
    private ArrayList<String> dataList;//ID-1,名称
    private ListView select_list;
    private int title_id = 0;
    private String selectStr = "";
    private HashMap<String, String> cityMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.common_list_view);
        super.onCreate(savedInstanceState);
        pIntent = getIntent();
        context = this;
        initView();
    }

    private void initView() {
        title_id = pIntent.getIntExtra("title_id", ProvincesTitle);
        switch (title_id) {
            case ProvincesTitle:
                setTitle(context.getString(R.string.select_province_title));
                xmlResourceId = R.xml.provinces;
                break;
            case CityTitle:
                setTitle(context.getString(R.string.select_city_title));
                xmlResourceId = R.xml.cities;
                cityMap = new HashMap<String, String>();
                break;
            case BankTitle:
                setTitle(context.getString(R.string.select_bank_title));
                xmlResourceId = R.xml.banks;
                break;
            case DistrictTitle:
                setTitle(context.getString(R.string.select_district_title));
                xmlResourceId = R.xml.districts;
                break;
            default:
                break;
        }
        select_list = (ListView) findViewById(R.id.mListView);
        getSelectItems();
        SelectItemAdapter adapter = new SelectItemAdapter(context, dataList);
        select_list.setAdapter(adapter);
        select_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (title_id == ProvincesTitle) {
                    //当点击省份时，打开城市选择
                    Intent intent = new Intent(context, SelectItemActivity.class);
                    intent.putExtra("title_id", SelectItemActivity.CityTitle);
                    intent.putExtra("PID", String.valueOf(position + 1));
                    intent.putExtra("is_select_district", pIntent.getBooleanExtra("is_select_district", false));
                    startActivityForResult(intent, SelectItemActivity.CityTitle);
                    selectStr = dataList.get(position);
                } else if (title_id == CityTitle && pIntent.getBooleanExtra("is_select_district", false) == true) {
                    //当点击城市并且需要选择地区
                    Intent intent = new Intent(context, SelectItemActivity.class);
                    intent.putExtra("title_id", SelectItemActivity.DistrictTitle);
                    intent.putExtra("CID", cityMap.get(dataList.get(position)));
                    startActivityForResult(intent, SelectItemActivity.DistrictTitle);
                    selectStr = dataList.get(position);
                } else {
                    //1.点击银行; 2.点击城市，不选择地区; 3.点击地区
                    Intent data = new Intent();
                    data.putExtra("returnStr", dataList.get(position));
                    setResult(title_id, data);
                    finish();
                }
            }
        });
    }

    private void getSelectItems() {
        XmlResourceParser parser = context.getResources().getXml(xmlResourceId);
        try {
            int event = parser.getEventType();
            while (event != XmlResourceParser.END_DOCUMENT) {
                switch (event) {
                    case XmlResourceParser.START_DOCUMENT:
                        dataList = new ArrayList<String>();
                        break;
                    case XmlResourceParser.START_TAG:
                        if (parser.getName().equals("Province") || parser.getName().equals("Bank")) {
                            //读取省份或者银行
                            dataList.add(parser.getAttributeValue(1));
                        }
                        if (parser.getName().equals("City") && parser.getAttributeValue(2).equals(pIntent.getStringExtra("PID"))) {
                            //读取城市数据
                            dataList.add(parser.getAttributeValue(1));
                            cityMap.put(parser.getAttributeValue(1), parser.getAttributeValue(0));
                        }
                        if (parser.getName().equals("District") && parser.getAttributeValue(2).equals(pIntent.getStringExtra("CID"))) {
                            dataList.add(parser.getAttributeValue(1));
                        }
                        break;
                    case XmlResourceParser.END_TAG:
                        break;
                    default:
                        break;
                }
                event = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CityTitle || resultCode == DistrictTitle) {
            selectStr += data.getStringExtra("returnStr");
            Intent intent = new Intent();
            intent.putExtra("returnStr", selectStr);
            setResult(title_id, intent);
            finish();
        }
    }
}
