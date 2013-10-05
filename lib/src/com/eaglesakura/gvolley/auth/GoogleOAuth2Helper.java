package com.eaglesakura.gvolley.auth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.eaglesakura.gvolley.json.JSON;
import com.eaglesakura.gvolley.json.Model;
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

    static final int TIMEOUT_MS = 1000 * 15;

    /**
     * クエリ用URLを生成する
     * @param url
     * @param queries
     * @return
     */
    public static String makeQueryUrl(String url, Map<String, String> queries) {
        try {
            if (!queries.isEmpty()) {
                StringBuilder sb = new StringBuilder(url);
                sb.append("?");

                Iterator<Entry<String, String>> iterator = queries.entrySet().iterator();
                // URLにクエリを追加する
                while (iterator.hasNext()) {
                    Entry<String, String> entry = iterator.next();
                    sb.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), "utf-8"));
                    if (iterator.hasNext()) {
                        sb.append('&');
                    }
                }
                return sb.toString();
            } else {
                return url;
            }
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }

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

        return makeQueryUrl(ENDPOINT + "/auth", queries);
    }

    /**
     * 取得した認証コードからアクセス用のトークンとリフレーッシュトークンを作成する
     * @param authCode
     */
    public static AuthToken getAuthToken(final String clientId, final String clientSecret, final String redirectUri,
            final String authCode) throws WebAPIException {

        try {
            // パラメータを組み立てる
            StringBuilder b = new StringBuilder();
            b.append("code=").append(URLEncoder.encode(authCode, "utf-8"));
            b.append("&client_id=").append(URLEncoder.encode(clientId, "utf-8"));
            b.append("&client_secret=").append(URLEncoder.encode(clientSecret, "utf-8"));
            b.append("&redirect_uri=").append(URLEncoder.encode(redirectUri, "utf-8"));
            //            b.append("&grant_type=authorization_code");
            b.append("&grant_type=").append(URLEncoder.encode("authorization_code", "utf-8"));
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
                os.close();
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
