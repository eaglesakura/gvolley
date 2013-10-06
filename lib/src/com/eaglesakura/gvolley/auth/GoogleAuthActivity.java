package com.eaglesakura.gvolley.auth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.eaglesakura.aautil.EUtil;
import com.eaglesakura.gvolley.R;
import com.eaglesakura.gvolley.auth.GoogleOAuth2Helper.AuthToken;
import com.eaglesakura.lib.android.game.util.LogUtil;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;

@EActivity
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

    /**
     * AuthProvider ID
     */
    static final String EXTRA_PROVIDER_ID = "EXTRA_PROVIDER_ID";

    @Extra(EXTRA_CLIENT_ID)
    String clientId;

    @Extra(EXTRA_CLIENT_SECRET)
    String clientSecret;

    @Extra(EXTRA_SCOPES)
    String[] scopes;

    @Extra(EXTRA_PROVIDER_ID)
    String providerId;

    /**
     * oauth情報保持クラス
     */
    OAuthProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_auth);
        provider = new OAuthProvider(this, providerId, clientId, clientSecret);
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
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onErrorMakeAuthToken(GoogleOAuth2Fragment fragment, VolleyError e) {
        LogUtil.log("onErrorMakeAuthToken");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.network_failed);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        builder.show();
    }

    @Override
    public void onMakeTokenComplete(GoogleOAuth2Fragment fragment, AuthToken token) {
        //        LogUtil.log("onMakeTokenComplete :: " + token.access_token + "/" + token.refresh_token);
        provider.onAuthCompleted(token);
        Toast.makeText(this, R.string.auth_complete, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
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
            String clientSecret, String[] scopes, OAuthProvider provider) {
        Intent intent = new Intent(context, EUtil.annotation(clazz));
        intent.putExtra(EXTRA_CLIENT_ID, clientId);
        intent.putExtra(EXTRA_CLIENT_SECRET, clientSecret);
        intent.putExtra(EXTRA_SCOPES, scopes);
        intent.putExtra(EXTRA_PROVIDER_ID, provider.getUniqueId());

        return intent;
    }
}
