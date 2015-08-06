package com.autoparts.buyers.network;

import android.text.TextUtils;
import com.autoparts.buyers.utils.Constants;
import com.autoparts.buyers.utils.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 数据解析
 * Created by:Liuhuacheng
 * Created time:14-7-9
 */
public class JsonParserUtils {
    private static final String TAG = "JsonParserUtils";

    public static ResponseModel jsonParse(String json) {
        ResponseModel response = new ResponseModel();
        try {
            json = getJson(json);
            JSONObject jsonObject = new JSONObject(json);

            if (true) {//获取成功
                for (int i = 0; i < jsonObject.length(); i++) {
                    //获取key值
                    String key = jsonObject.names().optString(i);
                    JSONArray array = jsonObject.optJSONArray(key);
                    if (array != null) {
                        //TODO 如果数据为JSONArray类型的
                        response.getMap().put(key, array.toString());
                        try {
                            jsonParseArray(response, array);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (jsonObject.optJSONObject(key) != null) {
                        //TODO 如果数据为JSONObject类型的
                        JSONObject jsons = jsonObject.optJSONObject(key);
                        for (int m = 0; m < jsons.length(); m++) {
                            String jsonKey = jsons.names().optString(m);
                            String value = Utils.getTextString(jsons.optString(jsonKey, ""));
                            response.getMap().put(jsonKey, value);
                        }
                    } else {//单一类型key value
                        String value = Utils.getTextString(jsonObject.optString(key, ""));
                        response.getMap().put(key, value);
                    }
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.getStackTrace();
            Utils.showLog("Exception==" + e.getMessage());
            response.getMap().put("rspCode", "-1");
            response.getMap().put("rspDesc", "JsonParseError");
        }

        return response;
    }

//    //解析JsonArray
//    public static void jsonParseArray(ResponseModel response, JSONArray jsonArray) {
//        //解析JsonArray
//        for (int j = 0; j < jsonArray.length(); j++) {
//            JSONObject jsonStr = jsonArray.optJSONObject(j);
//            Map<String, Object> maps = new HashMap<String, Object>();
//            for (int k = 0; k < jsonStr.length(); k++) {
//                String key = jsonStr.names().optString(k);
//                String value = Utils.getTextString(jsonStr.optString(key, ""));
//                maps.put(key, value);
//            }
//            response.getMaps().add((HashMap<String, Object>) maps);
//        }
//    }

    //解析JsonArray
    public static void jsonParseArray(ResponseModel response, JSONArray jsonArray) {
        //解析JsonArray
        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject jsonStr = jsonArray.optJSONObject(j);
            HashMap<String, Object> map = new HashMap<String, Object>();
            for (int k = 0; k < jsonStr.length(); k++) {
                String key = jsonStr.names().optString(k);
                JSONArray array = jsonStr.optJSONArray(key);
                if (array != null) {//数组嵌套数组
                    parseArrayInArray(map, key, array);
                } else {
                    String value = Utils.getTextString(jsonStr.optString(key, ""));
                    map.put(key, value);
                }
            }
            response.getMaps().add(map);
        }
    }

    //解析JsonArray中的JsonArray
    public static void parseArrayInArray(HashMap<String, Object> map, String keyArray, JSONArray jsonArray) {
        Vector<HashMap<String, Object>> mList = new Vector<HashMap<String, Object>>();
        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject jsonStr = null;
            try {
//                String s = (String)jsonArray.get(j);
//                Utils.showLog(j,"sss=="+s);
                jsonStr = jsonArray.optJSONObject(j);
                if (jsonStr != null) {
                    HashMap<String, Object> maps = new HashMap<String, Object>();
                    for (int k = 0; k < jsonStr.length(); k++) {
                        String key = jsonStr.names().optString(k);
                        String value = Utils.getTextString(jsonStr.optString(key, ""));
                        maps.put(key, value);
                    }
                    mList.add(maps);
                    map.put(keyArray, mList);
                } else {
                    map.put(keyArray, jsonArray.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //解析JsonArray
    public static Vector<HashMap<String, Object>> jsonParseArray(String json) {
        Vector<HashMap<String, Object>> mList = new Vector<HashMap<String, Object>>();
        if (!TextUtils.isEmpty(json)) {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(json);
                //解析JsonArray
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonStr = jsonArray.optJSONObject(j);
                    Map<String, Object> maps = new HashMap<String, Object>();
                    for (int k = 0; k < jsonStr.length(); k++) {
                        String key = jsonStr.names().optString(k);
                        String value = Utils.getTextString(jsonStr.optString(key, ""));
                        maps.put(key, value);
                    }
                    mList.add((HashMap<String, Object>) maps);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mList;
    }

    //解析JsonArray
    public static Float[] jsonParseFloatArray(String json) {
        Float[] values = null;
        if (!TextUtils.isEmpty(json)) {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(json);
                values = new Float[jsonArray.length()];
                for (int j = 0; j < jsonArray.length(); j++) {
                    float value = Float.parseFloat((String) jsonArray.get(j));
                    values[j] = value;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return values;
    }

    //解析JsonArray
    public static String[] jsonParseStringArray(String json) {
        String[] values = null;
        json = json.replace("[", "");
        json = json.replace("]", "");
        values = json.split(",");
        return values;
    }

    //解析JsonObject
    public static void jsonParseObject(String json) {
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
    }

    //从json串中获取相应的key值
    public static String jsonParseOther(String json, String key) {
        String url = null;
        try {
            JSONObject object = new JSONObject(json);
            url = object.getString(key);
            Utils.showLog("object===" + object);
            Utils.showLog("url===" + url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getJson(String json) {
        int index = json.indexOf("\"");//寻找第一个 “
        Utils.showLog("index =" + index);
        String newJson = json.substring(index + 1);
        newJson = "{\"" + newJson;
        return newJson;
    }

}
