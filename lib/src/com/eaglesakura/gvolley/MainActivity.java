package com.eaglesakura.gvolley;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.eaglesakura.gvolley.auth.GoogleAuthActivity;
import com.eaglesakura.gvolley.auth.Scopes;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = GoogleAuthActivity.newIntent(this, GoogleAuthActivity.class,
                getString(R.string.oauth2_clientId), getString(R.string.oauth2_clientSecret), new String[] {
                    Scopes.SPREADSHEET.getURL()
                });
        startActivity(intent);
    }

    @UiThread
    void startAuth() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
