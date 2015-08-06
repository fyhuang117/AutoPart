package com.autoparts.sellers.network;

import org.apache.http.Header;

/**
 * Created by:Liuhuacheng
 * Created time:14-10-30
 */
public class HttpResultHandler implements HttpResultInterface {

    @Override
    public void onResultJson(String json) {

    }
    @Override
    public void onResultSuccess(Header[] headers,ResponseModel response, String message, int statusCode) {

    }

    @Override
    public void onResultFail(String message, int statusCode) {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onDelSuccess() {

    }

}
