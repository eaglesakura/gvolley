package com.eaglesakura.gvolley.auth;

import java.io.File;
import java.io.IOException;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.eaglesakura.gvolley.R;
import com.eaglesakura.gvolley.auth.GoogleOAuth2Helper.AuthToken;
import com.eaglesakura.gvolley.request.BaseRequest;
import com.eaglesakura.gvolley.request.SimpleModelRequest;
import com.eaglesakura.gvolley.request.listener.RequestListener;
import com.eaglesakura.lib.android.db.DBType;
import com.eaglesakura.lib.android.db.TextKeyValueStore;
import com.eaglesakura.lib.android.game.util.GameUtil;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

/**
 * OAuth2認証の補助を行う
 */
public class OAuthProvider {

    /**
     * アクセストークン
     */
    private String token;

    /**
     * リフレッシュトークン
     */
    private String refresh;

    /**
     * アクセス対象のアカウント
     */
    private String account;

    /**
     * データベースの実ファイル
     */
    private final File dbFile;

    /**
     * テーブルの管理ID
     */
    private final String uniqueId;

    /**
     * 
     */
    private final Context context;

    /**
     * OAuth2 クライアントID
     */
    private final String clientId;

    /**
     * OAuth2 クライアントシークレット
     */
    private final String clientSecret;

    private static final String TABLE_NAME = "tokens";

    private static final int DB_VERSION = 0x1;

    /**
     * OAuth認証用のキーを保持する
     * @param context
     * @param uniqueId ストア内で一意に識別するためのID。英数で指定
     * @param dbFile
     */
    public OAuthProvider(Context context, String uniqueId, String clientId, String clientSecret) {
        this.context = context;
        this.uniqueId = uniqueId;
        this.clientId = clientId;
        this.clientSecret = clientSecret;

        this.dbFile = new File(context.getFilesDir(), "com.eaglesakura.gvolley-auth.db");
        dbFile.getParentFile().mkdirs();
        load();
    }

    private String getTokenKey() {
        return uniqueId + "/tkn";
    }

    private String getRefreshKey() {
        return uniqueId + "/rfs";
    }

    private String getAccountKey() {
        return uniqueId + "/acnt";
    }

    /**
     * DBからキャッシュ情報を読み出す
     */
    public void load() {
        final TextKeyValueStore db = new TextKeyValueStore(dbFile, context, TABLE_NAME, DBType.Read, DB_VERSION);
        {
            token = db.getOrNull(getTokenKey());
            refresh = db.getOrNull(getRefreshKey());
            account = db.getOrNull(getAccountKey());
        }
        db.dispose();
    }

    /**
     * 認証成功時に呼び出す
     * 認証成功、もしくはリフレッシュ時に呼び出す
     * @param token
     */
    public void onAuthCompleted(AuthToken token) {
        TextKeyValueStore db = new TextKeyValueStore(dbFile, context, TABLE_NAME, DBType.Write, DB_VERSION);
        try {
            db.beginTransaction();
            db.insertOrUpdate(getTokenKey(), token.access_token);
            // リフレッシュトークンが設定されていればリフレッシュする
            if (token.refresh_token != null) {
                db.insertOrUpdate(getRefreshKey(), token.refresh_token);
            }
        } finally {
            db.endTransaction();
            db.dispose();
        }

        // メモリキャッシュに載せる
        this.token = token.access_token;
        if (token.refresh_token != null) {
            this.refresh = token.refresh_token;
        }
    }

    /**
     * 認証成功時に呼び出す
     * @param account
     * @param token
     */
    public void onAuthCompletedFromPlayservice(String account, String token) {
        TextKeyValueStore db = new TextKeyValueStore(dbFile, context, TABLE_NAME, DBType.Write, DB_VERSION);
        try {
            db.beginTransaction();

            // トークンを書き込む
            db.insertOrUpdate(getTokenKey(), token);
            db.insertOrUpdate(getAccount(), token);
        } finally {
            db.endTransaction();
            db.dispose();
        }

        // メモリキャッシュに載せる
        this.token = token;
        this.account = account;
    }

    /**
     * 認証済みであればtrue
     * @return
     */
    public boolean isAuthorized() {
        return !GameUtil.isEmpty(token);
    }

    /**
     * アクセストークンを取得する
     * @return
     */
    public String getAccessToken() {
        return token;
    }

    /**
     * リフレッシュトークンを取得する
     * @return
     */
    public String getRefreshToken() {
        return refresh;
    }

    /**
     * プロバイダ管理用ユニークID
     * @return
     */
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * ログインしているアカウント
     * @return
     */
    public String getAccount() {
        return account;
    }

    /**
     * OAuth2認証を付与する
     * @param <T>
     * @param req リクエスト
     * @param accessApi アクセス対象のAPI
     */
    public <T> void authorize(BaseRequest<T> req, Scopes accessApi) {
        if (!isAuthorized()) {
            throw new IllegalStateException("token not found");
        }
        req.putHeader("Authorization", "OAuth " + getAccessToken());
        if (accessApi != null) {
            req.putHeader("GData-Version", accessApi.getVersion());
        }
    }

    /**
     * Play Service経由でTokenを生成する
     * @param context
     * @param account
     * @param scopes
     * @throws GoogleAuthException
     * @throws IOException
     */
    public void authFromPlayservice(Context context, String account, String[] scopes) throws GoogleAuthException,
            IOException {

        String SCOPE = "oauth2:";
        {
            for (int i = 0; i < scopes.length; ++i) {
                SCOPE += scopes[i];
                if (i < (scopes.length - 1)) {
                    SCOPE += " ";
                }
            }
        }

        final String token = GoogleAuthUtil.getToken(context, account, SCOPE);
        onAuthCompletedFromPlayservice(account, token);
    }

    /**
     * PlayService経由でアカウント選択するためのIntentを生成する
     * @param context
     * @return
     */
    public Intent newAccountPickIntentFromPlayservice(Context context) {
        Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[] {
            GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE
        }, true, context.getString(R.string.pickaccount_message), null, null, null);
        return intent;
    }

    /**
     * 選択されたアカウント名を返す
     * @param result
     * @param data
     * @return
     */
    public String onAccountPickResult(int result, Intent data) {
        if (result == Activity.RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            return accountName;
        }
        return null;
    }

    /**
     * リフレッシュ用リクエストを発行する
     * @param listener
     * @return
     */
    public SimpleModelRequest<AuthToken> requestTokenRefresh(RequestListener<AuthToken> listener) {
        return GoogleOAuth2Helper.refreshAuthToken(clientId, clientSecret, getRefreshToken(), listener);
    }

}
