package com.eaglesakura.gvolley.auth;

import java.io.File;

import android.content.Context;

import com.eaglesakura.gvolley.auth.GoogleOAuth2Helper.AuthToken;
import com.eaglesakura.lib.android.db.DBType;
import com.eaglesakura.lib.android.db.TextKeyValueStore;
import com.eaglesakura.lib.android.game.util.GameUtil;

/**
 * OAuth2認証の補助を行う
 */
public class AuthProvider {

    /**
     * アクセストークン
     */
    String token;

    /**
     * リフレッシュトークン
     */
    String refresh;

    final File dbFile;

    final String uniqueId;

    final Context context;

    private static final String TABLE_NAME = "tokens";

    private static final int DB_VERSION = 0x1;

    /**
     * OAuth認証用のキーを保持する
     * @param context
     * @param uniqueId ストア内で一意に識別するためのID。英数で指定
     * @param dbFile
     */
    public AuthProvider(Context context, String uniqueId) {
        this.context = context;
        this.uniqueId = uniqueId;
        this.dbFile = new File(context.getFilesDir(), "gvolley-auth.db");
        dbFile.getParentFile().mkdirs();
        loadFromDB();
    }

    private String getTokenKey() {
        return uniqueId + "/tkn";
    }

    private String getRefreshKey() {
        return uniqueId + "/rfs";
    }

    /**
     * DBからキャッシュ情報を読み出す
     */
    void loadFromDB() {
        final TextKeyValueStore db = new TextKeyValueStore(dbFile, context, TABLE_NAME, DBType.Read, DB_VERSION);
        {
            token = db.getOrNull(getTokenKey());
            refresh = db.getOrNull(getRefreshKey());
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
        {
            db.insertOrUpdate(getTokenKey(), token.access_token);
            // リフレッシュトークンが設定されていればリフレッシュする
            if (token.refresh_token != null) {
                db.insertOrUpdate(getRefreshKey(), token.refresh_token);
            }
        }
        db.dispose();

        // メモリキャッシュに載せる
        this.token = token.access_token;
        if (token.refresh_token != null) {
            this.refresh = token.refresh_token;
        }
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
}
