package com.eaglesakura.gvolley.auth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.eaglesakura.gvolley.VolleyUtil;
import com.eaglesakura.gvolley.json.JSON;
import com.eaglesakura.gvolley.json.Model;
import com.eaglesakura.gvolley.request.SimpleModelRequest;
import com.eaglesakura.gvolley.request.listener.RequestListener;
import com.eaglesakura.lib.android.game.util.GameUtil;
import com.eaglesakura.lib.android.game.util.LogUtil;
import com.eaglesakura.lib.net.WebAPIException;

/**
 * Googleの認証用ヘルパ
 * @author TAKESHI YAMASHITA
 *
 */
public class GoogleOAuth2Helper {
    private static final String ENDPOINT = "https://accounts.google.com/o/oauth2";

    private static final int TIMEOUT_MS = 1000 * 15;

    /**
     * 認証コードを取得する
     * 各コードは"https://code.google.com/apis/console"から作成
     * @throws GDataException
     */
    public static String getAuthorizationUrl(final String clientId, final String redirectUri, final String[] scopeUrls) {
        String SCOPES = "";
        {
            for (int i = 0; i < scopeUrls.length; ++i) {
                SCOPES += scopeUrls[i];
                if (i < (scopeUrls.length - 1)) {
                    SCOPES += " ";
                }
            }
        }

        // パラメータの組み立て
        Map<String, String> queries = new HashMap<String, String>();
        queries.put("response_type", "code");
        queries.put("client_id", clientId);
        queries.put("redirect_uri", redirectUri);
        queries.put("scope", SCOPES);
        queries.put("status", "1");
        queries.put("access_type", "offline");
        queries.put("approval_prompt", "force");

        return VolleyUtil.makeQueryUrl(ENDPOINT + "/auth", queries);
    }

    /**
     * 認証コードからアクセストークンとリフレッシュトークンを取得する
     * @param clientId
     * @param clientSecret
     * @param redirectUri
     * @param authCode
     * @return
     */
    public static SimpleModelRequest<AuthToken> getAuthToken(final String clientId, final String clientSecret,
            final String redirectUri, final String authCode, RequestListener<AuthToken> listener) {
        SimpleModelRequest<AuthToken> req = new SimpleModelRequest<AuthToken>(Request.Method.POST, ENDPOINT + "/token",
                AuthToken.class, listener);
        req.putForm("code", authCode);
        req.putForm("client_id", clientId);
        req.putForm("client_secret", clientSecret);
        req.putForm("redirect_uri", redirectUri);
        req.putForm("grant_type", "authorization_code");
        return req;
    }

    /**
     * リフレッシュトークンを渡す
     * @param clientId
     * @param clientSecret
     * @param refreshTocken
     * @param listener
     * @return
     */
    public static SimpleModelRequest<AuthToken> refreshAuthToken(final String clientId, final String clientSecret,
            final String refreshTocken, RequestListener<AuthToken> listener) {

        SimpleModelRequest<AuthToken> req = new SimpleModelRequest<AuthToken>(Request.Method.POST, ENDPOINT + "/token",
                AuthToken.class, listener);
        req.putForm("client_id", clientId);
        req.putForm("client_secret", clientSecret);
        req.putForm("grant_type", "refresh_token");
        req.putForm("refresh_token", refreshTocken);
        return req;
    }

    /**
     * 取得した認証コードからアクセス用のトークンとリフレーッシュトークンを作成する
     * @param authCode
     */
    public static AuthToken refreshAuthToken(final String clientId, final String clientSecret,
            final String refreshTocken) throws WebAPIException {

        try {
            // パラメータを組み立てる
            StringBuilder b = new StringBuilder();
            b.append("&client_id=").append(URLEncoder.encode(clientId, "utf-8"));
            b.append("&client_secret=").append(URLEncoder.encode(clientSecret, "utf-8"));
            b.append("&grant_type=").append(URLEncoder.encode("refresh_token", "utf-8"));
            b.append("&refresh_token=").append(URLEncoder.encode(refreshTocken, "utf-8"));
            byte[] payload = b.toString().getBytes();

            // POST メソッドでリクエストする
            URL url = new URL(ENDPOINT + "/token");
            LogUtil.log("url = " + url.toString());
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("POST");
            c.setDoOutput(true);
            c.setDoInput(true);
            c.setConnectTimeout(TIMEOUT_MS);
            c.setRequestProperty("Content-Length", String.valueOf(payload.length));
            c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            c.connect();

            {
                OutputStream os = c.getOutputStream();
                os.write(payload);
                os.flush();
            }

            int response = c.getResponseCode();
            LogUtil.log("Response Code = " + response);

            // 戻り値を確認する
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream is = c.getInputStream();
                String json = new String(GameUtil.toByteArray(is));
                //                LogUtil.log("json = " + json);

                AuthToken token = JSON.decode(json, AuthToken.class);

                c.disconnect();
                if (token == null || token.access_token == null) {
                    throw new WebAPIException("token == null", WebAPIException.Type.APIResponseError);
                }
                return token;
            } else {
                InputStream is = c.getErrorStream();
                String json = new String(GameUtil.toByteArray(is));
                LogUtil.log("error = " + json);
                c.disconnect();
                throw new WebAPIException(response);
            }
        } catch (IOException e) {
            LogUtil.log(e);
            throw new WebAPIException(e);
        }
    }

    public static final String ERROR_INVALID_REQUEST = "invalid_request";

    public static final String ERROR_INVALID_GRANT = "invalid_grant";

    /**
     * エラーコード解析用
     * @author TAKESHI YAMASHITA
     *
     */
    public static class ErrorCode extends Model {
        public String error = null;
    }

    /**
     * 認証やりとりを行うトークン
     */
    public static class AuthToken extends Model {
        /**
         * アクセス用のトークン
         */
        public String access_token = null;

        /**
         * リフレッシュ用のトークン
         */
        public String refresh_token = null;

        /**
         * 
         */
        public String token_type = null;

        /**
         * 
         */
        public Integer expires_in = null;
    }
}
