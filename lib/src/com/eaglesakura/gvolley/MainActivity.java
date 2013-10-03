package com.eaglesakura.gvolley;

import android.app.Activity;
import android.view.Menu;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    @AfterViews
    void onAfterViews() {

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
