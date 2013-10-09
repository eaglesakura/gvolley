package com.eaglesakura.gvolley.request.listener;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.android.volley.RequestQueue;
import com.eaglesakura.gvolley.R;
import com.eaglesakura.gvolley.request.BaseRequest;
import com.eaglesakura.lib.android.game.thread.UIHandler;
import com.eaglesakura.lib.android.game.util.LogUtil;

/**
 * リクエスト通信中にダイアログ表示を行う汎用リスナー
 *
 * @param <T>
 */
public abstract class ProgressRequestController<T> extends RequestListener<T> implements
        DialogInterface.OnCancelListener {

    /**
     * 表示用のダイアログ
     */
    ProgressDialog dialog;

    /**
     * 進捗中のリクエスト
     */
    BaseRequest<T> request;

    final RequestQueue queue;

    /**
     * 
     */
    public ProgressRequestController(Context context, RequestQueue queue) {
        dialog = new ProgressDialog(context);
        dialog.setMessage(context.getString(R.string.network_connecting));
        dialog.setOnCancelListener(this);

        // 外部タッチは無効にする
        dialog.setCanceledOnTouchOutside(false);
        this.queue = queue;
    }

    /**
     * 処理中のダイアログを取得する
     * @return
     */
    public ProgressDialog getDialog() {
        return dialog;
    }

    /**
     * リクエストを設定し、キューへ追加する
     * @param req
     */
    public ProgressRequestController<T> addRequestQueue(BaseRequest<T> req) {
        this.request = req;
        queue.add(req);
        queue.start();
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
    public ProgressRequestController<T> show() {
        UIHandler.postUI(new Runnable() {
            @Override
            public void run() {
                try {
                    dialog.show();
                } catch (Exception e) {
                    LogUtil.log(e);
                }
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
        request.cancel();
    }
}
