package com.eaglesakura.gvolley;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.eaglesakura.gvolley.auth.AuthProvider;
import com.eaglesakura.gvolley.auth.GoogleAuthActivity;
import com.eaglesakura.gvolley.auth.Scopes;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;

@EActivity
public class MainActivity extends Activity {

    private AuthProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        provider = new AuthProvider(this, "sptest");
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        if (provider.isAuthorized()) {
            toast("authorized");
        } else {
            startAuth();
        }
    }

    @UiThread
    void startAuth() {
        Intent intent = GoogleAuthActivity.newIntent(this, GoogleAuthActivity.class,
                getString(R.string.oauth2_clientId), getString(R.string.oauth2_clientSecret), new String[] {
                    Scopes.SPREADSHEET.getURL()
                }, provider);
        startActivity(intent);
    }

    @UiThread
    void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
