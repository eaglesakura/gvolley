package com.eaglesakura.gvolley.request;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.eaglesakura.gvolley.auth.AuthProvider;
import com.eaglesakura.gvolley.auth.GoogleOAuth2Helper.AuthToken;
import com.eaglesakura.lib.android.game.util.LogUtil;

public abstract class AuthorizedProgressRequestListener<T> extends ProgressRequestListener<T> {

    final AuthProvider provider;

    SimpleModelRequest<AuthToken> refreshRequest = null;

    public AuthorizedProgressRequestListener(Context context, RequestQueue queue, AuthProvider provider) {
        super(context, queue);
        this.provider = provider;
    }

    @Override
    protected final void onError(VolleyError error) {
        if (error.networkResponse.statusCode == 401) {
            // トークン交換を開始する
            startTokenRefresh();
        } else {
            onError2(error);
        }
    }

    @Override
    protected final void onFinalize(boolean success) {
        // リフレッシュ中じゃなければ通常通りの処理を行う
        if (refreshRequest == null) {
            super.onFinalize(success);
        }
    }

    protected abstract void onError2(VolleyError error);

    protected void onFinalize2(boolean success) {
    }

    /**
     * 裏でトークンの再発行を行う
     */
    void startTokenRefresh() {
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
                AuthorizedProgressRequestListener.this.onError(error);
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
}
