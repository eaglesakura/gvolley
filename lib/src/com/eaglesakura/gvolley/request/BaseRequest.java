package com.eaglesakura.gvolley.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.eaglesakura.gvolley.ContentType;
import com.eaglesakura.gvolley.request.listener.RequestListener;

/**
 * リクエスト処理の共通部位を記述
 * receiveで受け取ってconvertへコンバートして流すことも可能
 * @param <T>
 */
public abstract class BaseRequest<Convert, Receive> extends Request<Receive> {

    protected final RequestListener<Convert> listener;

    /**
     * query string params
     */
    private Map<String, String> queries = new HashMap<String, String>();

    /**
     * ヘッダ一覧
     */
    private Map<String, String> headers = new HashMap<String, String>();

    /**
     * フォームデータを送信する場合
     */
    private Map<String, String> formParams = new HashMap<String, String>();

    /**
     * コンテンツタイプ
     */
    private ContentType contentType;

    public BaseRequest(int method, String url, RequestListener<Convert> listener) {
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

    /**
     * フォーム送信用のパラメータを組み立てる
     * @param key
     * @param value
     */
    public void putForm(String key, String value) {
        formParams.put(key, value);
        contentType = ContentType.WebFormUTF8;
    }

    @Override
    public String getBodyContentType() {
        if (contentType != null) {
            return contentType.getContentType();
        } else {
            return super.getBodyContentType();
        }
    }

    /**
     * フォームデータが存在していたらそれを返し、存在しなければsuperに従う。
     */
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (formParams.isEmpty()) {
            return super.getParams();
        } else {
            return formParams;
        }
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
    public int compareTo(Request<Receive> another) {
        return getUrl().compareTo(another.getUrl());
    }

    /**
     * レスポンスを所定オブジェクトにコンバートする
     * @param response
     * @return
     */
    @SuppressWarnings("unchecked")
    protected Convert convert(Receive response) {
        return (Convert) response;
    }

    @Override
    protected void deliverResponse(Receive response) {
        listener.onResponse(convert(response));
    }

}