package com.eaglesakura.gvolley.request;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.eaglesakura.gvolley.R;
import com.eaglesakura.lib.android.game.thread.UIHandler;

/**
 * リクエスト通信中にダイアログ表示を行う汎用リスナー
 *
 * @param <T>
 */
public abstract class ProgressRequestListener<T> extends RequestListener<T> implements DialogInterface.OnCancelListener {

    /**
     * 表示用のダイアログ
     */
    ProgressDialog dialog;

    /**
     * 進捗中のリクエスト
     */
    BaseRequest<T> request;

    /**
     * 
     */
    public ProgressRequestListener(Context context) {
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
     * リクエストを設定する
     * @param req
     */
    public ProgressRequestListener<T> setRequest(BaseRequest<T> req) {
        this.request = req;
        return this;
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
    public ProgressRequestListener<T> show() {
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
    protected void onFinalize(boolean success) {
        dialog.dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
    }
}
