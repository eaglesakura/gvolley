package com.eaglesakura.gvolley.request.listener;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.android.volley.RequestQueue;
import com.eaglesakura.gvolley.R;
import com.eaglesakura.gvolley.auth.OAuthProvider;
import com.eaglesakura.gvolley.request.BaseRequest;
import com.eaglesakura.lib.android.game.thread.UIHandler;

/**
 * リクエスト通信中にダイアログ表示を行う汎用リスナー
 *
 * @param <T>
 */
public abstract class AuthorizedProgressRequestController<T> extends AuthorizedRequestController<T> implements
        DialogInterface.OnCancelListener {

    /**
     * 表示用のダイアログ
     */
    ProgressDialog dialog;

    /**
     * 
     */
    public AuthorizedProgressRequestController(Context context, RequestQueue queue, OAuthProvider provider) {
        super(queue, provider);
        dialog = new ProgressDialog(context);
        dialog.setMessage(context.getString(R.string.network_connecting));
        dialog.setOnCancelListener(this);

        // 外部タッチは無効にする
        dialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 処理中のダイアログを取得する
     * @return
     */
    public ProgressDialog getDialog() {
        return dialog;
    }

    /**
     * 設定されているリクエストを得る
     * @return
     */
    public BaseRequest<T> getRequest() {
        return request;
    }

    /**
     * ダイアログを表示する
     * @return
     */
    public AuthorizedProgressRequestController<T> show() {
        UIHandler.postUI(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
        return this;
    }

    /**
     * 通信が終了したらダイアログを閉じる
     */
    @Override
    protected void onComplete(boolean success) {
        super.onComplete(success);
        dialog.dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        request.cancel();
    }
}
