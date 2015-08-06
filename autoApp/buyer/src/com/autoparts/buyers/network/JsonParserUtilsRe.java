package com.autoparts.buyers.network;

import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.utils.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据解析
 * Created by:Liuhuacheng
 * Created time:14-7-9
 */
public class JsonParserUtilsRe {

    private static final String TAG = "JsonParserUtils";
    public static ResponseModel jsonParse(String json) {
        ResponseModel response = new ResponseModel();
        try {
            JSONObject jsonObject = new JSONObject(json);
            int rspCode = jsonObject.optInt("rspCode", -1);
            int total = jsonObject.optInt("total", -1);
            String rspDesc = jsonObject.optString("rspDesc");
            response.getMap().put("rspCode", String.valueOf(rspCode));
            response.getMap().put("rspDesc", rspDesc);
            response.getMap().put("total", String.valueOf(total));

            if (rspCode == Constants.status) {//获取成功
                for (int i = 0; i < jsonObject.length(); i++) {
                    //获取key值
                    String name = jsonObject.names().optString(i);
                    JSONArray array = jsonObject.optJSONArray(name);
                    //如果Json数据为JSONArray类型的
                    if (array != null) {
                        response.getMap().put("jsonArrayStr", jsonObject.optString(name, ""));
                        /*******************************************************/
                        for (int j = 0; j < array.length(); j++) {
                            JSONObject jsonStr = array.optJSONObject(j);
                            Map<String, Object> arrayMap = new HashMap<String, Object>();
                            for (int k = 0; k < jsonStr.length(); k++) {
                                String jsonName = jsonStr.names().optString(k);
                                if (jsonStr.optJSONObject(jsonName) != null) {
                                    //如果数组里面是对象
                                    Map<String, String> jsonMap = new HashMap<String, String>();
                                    JSONObject jsons = jsonStr.optJSONObject(jsonName);

                                    for (int m = 0; m < jsons.length(); m++) {
                                        String jsonMapName = jsons.names().optString(m);
                                        jsonMap.put(jsonMapName, jsons.optString(jsonMapName, ""));
                                    }
                                    arrayMap.put(jsonName, jsonMap);
                                } else {
                                    String value = jsonStr.optString(jsonName, "");
                                    arrayMap.put(jsonName, value);
                                }

                            }
                            response.getMaps().add((HashMap<String, Object>) arrayMap);
                        }
                    } else if (jsonObject.optJSONObject(name) != null) {
                        //如果Json数据为JSONObject类型的
                        JSONObject jsons = jsonObject.optJSONObject(name);
                        for (int m = 0; m < jsons.length(); m++) {
                            String jsonName = jsons.names().optString(m);
                            response.getMap().put(jsonName, jsons.optString(jsonName, ""));
                        }
                    } else {//单一类型
                        response.getMap().put(name, jsonObject.optString(name, ""));
                    }
                }
            } else {

            }

        } catch (Exception e) {
            // TODO: handle exception
            Utils.showLog(TAG, "Exception");
            e.getStackTrace();
        }

        return response;
    }

    //解析JsonArray
    public static ResponseModel jsonParseArray(String json) {
        ResponseModel response = new ResponseModel();
        try {
            JSONArray array = new JSONArray(json);
            //如果Json数据为JSONArray类型的
            if (array != null) {
                /*******************************************************/
                for (int j = 0; j < array.length(); j++) {
                    JSONObject jsonStr = array.optJSONObject(j);
                    Map<String, Object> arrayMap = new HashMap<String, Object>();
                    for (int k = 0; k < jsonStr.length(); k++) {
                        String jsonName = jsonStr.names().optString(k);
                        String value = jsonStr.optString(jsonName, "");
                        arrayMap.put(jsonName, value);
                    }
                    response.getMaps().add((HashMap<String, Object>) arrayMap);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            Utils.showLog(TAG, "Exception");
            e.getStackTrace();
        }
        return response;
    }

    //解析JsonObject
    public static ResponseModel jsonParseObject(String json) {
        ResponseModel response = new ResponseModel();
        try {
            JSONArray array = new JSONArray(json);
            //如果Json数据为JSONArray类型的
            if (array != null) {
                /*******************************************************/
                for (int j = 0; j < array.length(); j++) {
                    JSONObject jsonStr = array.optJSONObject(j);
                    Map<String, Object> arrayMap = new HashMap<String, Object>();
                    for (int k = 0; k < jsonStr.length(); k++) {
                        String jsonName = jsonStr.names().optString(k);
                        String value = jsonStr.optString(jsonName, "");
                        arrayMap.put(jsonName, value);
                    }
                    response.getMaps().add((HashMap<String, Object>) arrayMap);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            Utils.showLog(TAG, "Exception");
            e.getStackTrace();
        }
        return response;
    }

    //从json串中获取相应的key值
    public static String jsonParseOther(String json, String key) {
        String url = null;
        try {
            json = json.substring(1, json.length() - 1);
            JSONObject object = new JSONObject(json);
            url = object.getString(key);
            Utils.showLog("object===" + object);
            Utils.showLog("url===" + url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return url;
    }

}
