package com.eaglesakura.gvolley.auth;

import android.content.Context;

import com.eaglesakura.gvolley.auth.GoogleOAuth2Helper.AuthToken;
import com.eaglesakura.lib.android.game.util.GameUtil;

/**
 * OAuth2認証の補助を行う
 */
public class AuthProvider {

    AuthPref_ pref;

    public AuthProvider(Context context) {
        pref = new AuthPref_(context);
    }

    /**
     * 認証成功時に呼び出す
     * @param token
     */
    public void onAuthCompleted(AuthToken token) {
        pref.token().put(token.access_token);
        pref.refreshToken().put(token.refresh_token);
    }

    /**
     * 認証済みであればtrue
     * @return
     */
    public boolean hasAuthorization() {
        return !GameUtil.isEmpty(pref.token().get());
    }
}
