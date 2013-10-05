package com.eaglesakura.gvolley.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

/**
 * リクエスト処理の共通部位を記述
 *
 * @param <T>
 */
public abstract class BaseRequest<T> extends Request<T> {

    protected final RequestListener<T> listener;

    /**
     * query string params
     */
    private Map<String, String> queries = new HashMap<String, String>();

    /**
     * ヘッダ一覧
     */
    private Map<String, String> headers = new HashMap<String, String>();

    public BaseRequest(int method, String url, RequestListener<T> listener) {
        super(method, url, listener);

        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    /**
     * ヘッダを付与する
     * @param key
     * @param value
     */
    public void putHeader(String key, String value) {
        headers.put(key, value);
    }

    /**
     * QueryStringを追加する
     * @param key
     * @param value
     */
    public void putQuery(String key, String value) {
        queries.put(key, value);
    }

    @Override
    public String getUrl() {
        try {
            final String url = super.getUrl();
            if (!queries.isEmpty()) {
                StringBuilder sb = new StringBuilder(url);
                sb.append("?");

                Iterator<Entry<String, String>> iterator = queries.entrySet().iterator();
                // URLにクエリを追加する
                while (iterator.hasNext()) {
                    Entry<String, String> entry = iterator.next();
                    sb.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), "utf-8"));
                    if (iterator.hasNext()) {
                        sb.append('&');
                    }
                }
                return sb.toString();
            } else {
                return url;
            }
        } catch (UnsupportedEncodingException e) {
            return super.getUrl();
        }
    }

    @Override
    public int compareTo(Request<T> another) {
        return getUrl().compareTo(another.getUrl());
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

}