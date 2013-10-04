package com.eaglesakura.gvolley.request;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.eaglesakura.gvolley.json.JSON;
import com.eaglesakura.lib.android.game.util.LogUtil;

public class SimpleModelRequest<T> extends Request<T> {
    final RequestListener<T> listener;
    final Class<? extends T> clazz;

    public SimpleModelRequest(int method, String url, Class<? extends T> clazz, RequestListener<T> listener) {
        super(method, url, listener);

        this.listener = listener;
        this.clazz = clazz;
    }

    @Override
    public int compareTo(Request<T> another) {
        return getUrl().compareTo(another.getUrl());
    }

    /**
     * 指定されたレスポンスJSONへ変換する
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        // Modelへ変換する
        T model = JSON.decodeOrNull(new String(response.data), clazz);
        if (model != null) {
            return Response.success(model, getCacheEntry());
        } else {
            LogUtil.log("Model is null... :: resp[" + new String(response.data) + "]");
            return Response.error(new VolleyError("Model is not json"));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

}
