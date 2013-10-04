package com.eaglesakura.gvolley.request;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

/**
 * JSONモデルのリクエストを受け付ける
 *
 * @param <T>
 */
public abstract class RequestListener<T> implements Listener<T>, ErrorListener {

    public RequestListener() {
    }

    @Override
    public final void onResponse(T response) {
        onSuccess(response);
        onFinalize(true);
    }

    @Override
    public final void onErrorResponse(VolleyError error) {
        onError(error);
        onFinalize(false);
    }

    /**
     * 成功時のオブジェクトを受信する
     * @param response
     */
    protected abstract void onSuccess(T response);

    /**
     * エラーを受信する
     * @param error
     */
    protected abstract void onError(VolleyError error);

    /**
     * 終了処理
     * 最後にかならず呼び出される。
     * 
     * @param success 正常にレスポンスを受け取れていたらtrueが指定される
     */
    protected abstract void onFinalize(boolean success);
}
