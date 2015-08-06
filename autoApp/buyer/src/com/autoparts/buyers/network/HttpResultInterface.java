package com.autoparts.buyers.network;

import org.apache.http.Header;

/**
 * Created by:Liuhuacheng
 * Created time:14-10-30
 */
public interface HttpResultInterface {
    /**
     * //返回json数据，自己解析
     * @param json   返回值
     */
    public void onResultJson(String json);
    /**
     * //返回成功方法
     * @param response   返回值
     * @param message    返回描述信息
     * @param statusCode 返回状态码
     */
    public void onResultSuccess(Header[] headers, ResponseModel response, String message, int statusCode);
    /**
     * //返回失败方法
     * @param message    返回描述信息
     * @param statusCode 返回状态码
     */
    public void onResultFail(String message, int statusCode);
    //请求完成接口
    public void onFinish();

    //删除成功
    public void onDelSuccess();
}
