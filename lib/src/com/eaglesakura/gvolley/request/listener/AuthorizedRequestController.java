package com.eaglesakura.gvolley.request.listener;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.eaglesakura.gvolley.auth.GoogleOAuth2Helper.AuthToken;
import com.eaglesakura.gvolley.auth.OAuthProvider;
import com.eaglesakura.gvolley.request.BaseRequest;
import com.eaglesakura.gvolley.request.SimpleModelRequest;
import com.eaglesakura.lib.android.game.util.LogUtil;

/**
 * OAuth2認証が必要なリクエストを管理する
 *
 * @param <T>
 */
public abstract class AuthorizedRequestController<T> extends RequestListener<T> {

    /**
     * トークンリフレッシュ用のリクエスト
     */
    SimpleModelRequest<AuthToken> refreshRequest = null;

    /**
     * リクエストの処理対象
     */
    final RequestQueue queue;

    /**
     * OAuthトークン管理クラス
     */
    final OAuthProvider provider;

    /**
     * リクエスト本体
     * 認証エラーが発生した場合、このインスタンスを使いまわして再度通信を試みる。
     */
    protected BaseRequest<T> request;

    public AuthorizedRequestController(RequestQueue queue, OAuthProvider provider) {
        this.queue = queue;
        this.provider = provider;
    }

    /**
     * 通信を開始する
     * @param req
     */
    public void addRequestQueue(BaseRequest<T> req) {
        this.request = req;
        provider.authorize(req, null);
        this.queue.add(req);
        this.queue.start();
    }

    @Override
    protected final void onError(VolleyError error) {
        if (error != null && error.networkResponse != null && error.networkResponse.statusCode == 401) {
            onAuthorizationError(error);
        } else {
            onVolleyError(error);
        }
    }

    /**
     * 通信エラー
     * @param error
     */
    protected abstract void onVolleyError(VolleyError error);

    /**
     * 認証エラー
     * @param error
     */
    protected void onAuthorizationError(VolleyError error) {
        LogUtil.log("token refresh start");
        refreshRequest = provider.requestTokenRefresh(new RequestListener<AuthToken>() {
            @Override
            protected void onSuccess(AuthToken response) {
                LogUtil.log("refresh complete");

                provider.onAuthCompleted(response);
                // 再度認証する
                provider.authorize(request, null);
                // リクエストへ再度登録する
                addRequestQueue(request);
            }

            @Override
            protected void onError(VolleyError error) {
                onVolleyError(error);
            }

            @Override
            protected void onFinalize(boolean success) {
                // リフレッシュリクエストをクリアーする
                refreshRequest = null;
            }
        });
        queue.add(refreshRequest);
        queue.start();
    }

    @Override
    protected final void onFinalize(boolean success) {
        // リフレッシュ中じゃなければ通常通りの処理を行う
        if (refreshRequest == null) {
            onComplete(success);
        }
    }

    /**
     * 終了処理
     * @param success
     */
    protected void onComplete(boolean success) {
    }

}
