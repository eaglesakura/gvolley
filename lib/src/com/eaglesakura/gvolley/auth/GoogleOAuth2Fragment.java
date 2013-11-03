package com.eaglesakura.gvolley.auth;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewDatabase;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.eaglesakura.gvolley.auth.GoogleOAuth2Helper.AuthToken;
import com.eaglesakura.gvolley.request.listener.ProgressRequestController;
import com.eaglesakura.lib.android.game.thread.UIHandler;
import com.eaglesakura.lib.android.game.util.FileUtil;
import com.eaglesakura.lib.android.game.util.LogUtil;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;
import com.googlecode.androidannotations.annotations.UiThread;

/**
 * GData認証を行うためのFragment
 */
@EFragment
public class GoogleOAuth2Fragment extends Fragment {
    /**
     * 認証用のWebView
     */
    WebView webView = null;

    /**
     * client-id保持
     */
    static final String ARG_CLIENT_ID = "ARG_CLIENT_ID";

    /**
     * client-secret保持
     */
    static final String ARG_CLIENT_SECRET = "ARG_CLIENT_SECRET";

    /**
     * リダイレクト用URI
     */
    static final String ARG_REDIRECT_URI = "ARG_REDIRECT_URI";

    /**
     * 接続スコープ一覧
     */
    static final String ARG_SCOPE_URLs = "ARG_SCOPE_URLs";

    /**
     * デフォルトのリダイレクト用URI
     */
    static final String ARG_DEFAULT_REDIRECT_URI = "http://localhost";

    @FragmentArg(ARG_CLIENT_ID)
    String clientId;

    @FragmentArg(ARG_CLIENT_SECRET)
    String clientSecret;

    @FragmentArg(ARG_REDIRECT_URI)
    String redirectUri;

    @FragmentArg(ARG_SCOPE_URLs)
    String[] scopes;

    OAuth2Listener listener;

    RequestQueue requests;

    @Override
    @SuppressWarnings("all")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        webView = new WebView(getActivity());
        return webView;
    }

    /**
     * WebViewの状態を初期化する
     */
    @SuppressLint("SetJavaScriptEnabled")
    @AfterViews
    void onAfterViews() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.clearCache(true);
        webView.clearFormData();
        webView.clearSslPreferences();
        webView.clearHistory();
        webView.clearMatches();
        webView.clearView();
        webView.setWebViewClient(new WebViewClient() {
            boolean checked = false;

            synchronized boolean checkURL(final String checkUrl) {
                if (checked) {
                    return false;
                }

                if (checkUrl.startsWith(redirectUri)) {
                    // 正常にリダイレクトが動いた場合 
                    int index = checkUrl.indexOf('=');
                    final String authCode = checkUrl.substring(index + 1);
                    LogUtil.log("debug::" + authCode);
                    //
                    LogUtil.log("auth code / complete");
                    UIHandler.postUI(new Runnable() {
                        @Override
                        public void run() {
                            // WebViewを不可視にする
                            webView.setVisibility(View.INVISIBLE);
                            onReceiveAuthCode(authCode);
                        }
                    });
                    checked = true;
                    return true;
                }
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String startUrl, Bitmap favicon) {
                super.onPageStarted(view, startUrl, favicon);

                checkURL(startUrl);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String startUrl) {
                if (checkURL(startUrl)) {
                    return true;
                }

                return super.shouldOverrideUrlLoading(view, startUrl);
            }
        });
        cleanAuthCache();
    }

    /**
     * 認証用URLを取り出す
     */
    @Background
    void cleanAuthCache() {
        final File cacheDirectory = new File(getActivity().getCacheDir(), "com.eaglesakura.gvolley.auth");

        FileUtil.mkdir(cacheDirectory);
        FileUtil.cleanDirectory(cacheDirectory);
        UIHandler.postUI(new Runnable() {

            @Override
            public void run() {
                webView.getSettings().setAppCachePath(cacheDirectory.getAbsolutePath());
            }
        });
        CookieSyncManager.createInstance(getActivity());
        CookieManager.getInstance().removeAllCookie();

        final WebViewDatabase db = WebViewDatabase.getInstance(getActivity());
        db.clearFormData();
        db.clearHttpAuthUsernamePassword();
        db.clearUsernamePassword();

        // 認証開始
        startAuthorization();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        listener = (OAuth2Listener) activity;
        if (requests == null) {
            requests = Volley.newRequestQueue(activity);
        }
    }

    @Override
    public void onDestroy() {
        if (requests != null) {
            requests.stop();
            requests = null;
        }
        super.onDestroy();
    }

    /**
     * フラグメントが生きている場合はtrue
     * @return
     */
    protected boolean isExist() {
        if (getActivity() == null) {
            return false;
        }

        if (!isAdded()) {
            return false;
        }

        if (isDetached()) {
            return false;
        }

        return true;
    }

    /**
     * 認証URL取得中のダイアログ
     * @return
     */
    protected Dialog createAuthUrlLoadingDialog() {
        ProgressDialog result = new ProgressDialog(getActivity());
        result.setMessage("認証準備中です");
        return result;
    }

    /**
     * アクセストークン取得中のダイアログを作成する
     * @return
     */
    protected Dialog createAccesTokenLoadingDialog() {
        ProgressDialog result = new ProgressDialog(getActivity());
        result.setMessage("認証情報を問い合わせ中です");
        return result;
    }

    /**
     * 認証コードを取得できた
     * @param authCode
     */
    protected void onReceiveAuthCode(final String authCode) {
        if (!isExist()) {
            return;
        }

        ProgressRequestController<AuthToken> tokenListener = new ProgressRequestController<AuthToken>(getActivity(),
                requests) {
            @Override
            protected void onSuccess(AuthToken response) {
                listener.onMakeTokenComplete(get_this(), response);
            }

            @Override
            protected void onError(VolleyError error) {
                listener.onErrorMakeAuthToken(get_this(), error);
            }

            @Override
            public void onCancel(DialogInterface dialog) {
                super.onCancel(dialog);
                listener.onAuthCanceled(get_this());
            }
        };
        tokenListener.addRequestQueue(
                GoogleOAuth2Helper.getAuthToken(clientId, clientSecret, redirectUri, authCode, tokenListener)).show();
    }

    private GoogleOAuth2Fragment get_this() {
        return this;
    }

    /**
     * 
     */
    @UiThread
    protected void startAuthorization() {
        // 認証URLを開く
        webView.loadUrl(GoogleOAuth2Helper.getAuthorizationUrl(clientId, redirectUri, scopes));
    }

    /**
     * OAuth2のトークン取得状態を受け取るリスナ
     *
     */
    public interface OAuth2Listener {
        /**
         * トークンの作成に成功した
         * @param token
         */
        public void onMakeTokenComplete(GoogleOAuth2Fragment fragment, GoogleOAuth2Helper.AuthToken token);

        /**
         * トークンの取得に失敗した
         */
        public void onErrorMakeAuthToken(GoogleOAuth2Fragment fragment, VolleyError e);

        /**
         * キャンセルされた
         */
        public void onAuthCanceled(GoogleOAuth2Fragment fragment);
    }

    public static Bundle newArgments(final String clientId, final String clientSecret, final String[] scopes) {

        Bundle bundle = new Bundle();

        bundle.putString(ARG_CLIENT_ID, clientId);
        bundle.putString(ARG_CLIENT_SECRET, clientSecret);
        bundle.putString(ARG_REDIRECT_URI, ARG_DEFAULT_REDIRECT_URI);
        bundle.putStringArray(ARG_SCOPE_URLs, scopes);
        return bundle;
    }

    /**
     * 
     * @param clientId
     * @param clientSecret
     * @param scopes
     * @return
     */
    public static GoogleOAuth2Fragment newFragment(final String clientId, final String clientSecret,
            final String[] scopes) {
        GoogleOAuth2Fragment fragment = new GoogleOAuth2Fragment();
        fragment.setArguments(newArgments(clientId, clientSecret, scopes));
        return fragment;
    }
}
