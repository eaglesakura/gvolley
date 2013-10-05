package com.eaglesakura.gvolley.request;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.eaglesakura.gvolley.json.JSON;
import com.eaglesakura.lib.android.game.util.LogUtil;

/**
 * JSON Modelをリクエストするクラス
 *
 * @param <T>
 */
public class SimpleModelRequest<T> extends BaseRequest<T> {
    /**
     * 
     */
    protected final Class<? extends T> clazz;

    public SimpleModelRequest(int method, String url, Class<? extends T> clazz, RequestListener<T> listener) {
        super(method, url, listener);
        this.clazz = clazz;
    }

    /**
     * 指定されたレスポンスJSONへ変換する
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        // Modelへ変換する
        try {
            InputStream is = new ByteArrayInputStream(response.data);
            T model = JSON.decode(is, clazz);
            if (model != null) {
                return Response.success(model, getCacheEntry());
            }
        } catch (Exception e) {
            LogUtil.log(e);
        }
        LogUtil.log("Model is null... :: resp[" + new String(response.data) + "]");
        return Response.error(new VolleyError("Model is not json"));
    }

}
