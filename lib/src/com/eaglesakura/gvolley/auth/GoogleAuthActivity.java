package com.eaglesakura.gvolley.auth;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.eaglesakura.aautil.EUtil;
import com.eaglesakura.gvolley.R;
import com.eaglesakura.gvolley.auth.GoogleOAuth2Helper.AuthToken;
import com.eaglesakura.lib.android.game.util.LogUtil;
import com.eaglesakura.lib.net.WebAPIException;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;

@EActivity(R.layout.activity_auth)
public class GoogleAuthActivity extends Activity implements GoogleOAuth2Fragment.OAuth2Listener {

    /**
     * クライアントID
     */
    static final String EXTRA_CLIENT_ID = "EXTRA_CLIENT_ID";

    /**
     * トークンシークレット
     */
    static final String EXTRA_CLIENT_SECRET = "EXTRA_CLIENT_SECRET";

    /**
     * アクセススコープ
     */
    static final String EXTRA_SCOPES = "EXTRA_SCOPES";

    @Extra(EXTRA_CLIENT_ID)
    String clientId;

    @Extra(EXTRA_CLIENT_SECRET)
    String clientSecret;

    @Extra(EXTRA_SCOPES)
    String[] scopes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 引数を設定する
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            {
                GoogleOAuth2Fragment fragment = EUtil.newFragment(GoogleOAuth2Fragment.class);
                fragment.setArguments(GoogleOAuth2Fragment.newArgments(clientId, clientSecret, scopes));
                transaction.add(R.id.content, fragment);
            }
            transaction.commit();
        }
    }

    @Override
    public void onAuthCanceled(GoogleOAuth2Fragment fragment) {

    }

    @Override
    public void onErrorMakeAuthToken(GoogleOAuth2Fragment fragment, WebAPIException e) {
        LogUtil.log(e);
        LogUtil.log("onErrorMakeAuthToken :: " + e.getMessage());
    }

    @Override
    public void onErrorMakeAuthURL(GoogleOAuth2Fragment fragment, WebAPIException e) {
        LogUtil.log(e);
        LogUtil.log("onErrorMakeAuthURL :: " + e.getMessage());
    }

    @Override
    public void onMakeTokenComplete(GoogleOAuth2Fragment fragment, AuthToken token) {
        LogUtil.log("onMakeTokenComplete :: " + token);
    }

    /**
     * Intentを構築する
     * @param clazz
     * @param clientId
     * @param clientSecret
     * @param scopes
     * @return
     */
    public static Intent newIntent(Context context, Class<? extends GoogleAuthActivity> clazz, String clientId,
            String clientSecret, String[] scopes) {
        Intent intent = new Intent(context, EUtil.annotation(clazz));
        intent.putExtra(EXTRA_CLIENT_ID, clientId);
        intent.putExtra(EXTRA_CLIENT_SECRET, clientSecret);
        intent.putExtra(EXTRA_SCOPES, scopes);

        return intent;
    }
}
