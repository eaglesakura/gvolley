package com.eaglesakura.gvolley.request;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 指定したヘッダを取得するリクエスト
 */
public class HeaderRequest extends BaseRequest<String> {

    final String headerKey;

    public HeaderRequest(int method, String url, String headerKey, RequestListener<String> listener) {
        super(method, url, listener);
        this.headerKey = headerKey;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        final String value = response.headers.get(headerKey);
        if (value != null) {
            return Response.success(value, getCacheEntry());
        } else {
            return Response.error(new VolleyError("header not found :: " + response.headers.toString()));
        }
    }

}
