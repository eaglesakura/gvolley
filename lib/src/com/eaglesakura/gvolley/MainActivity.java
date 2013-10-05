package com.eaglesakura.gvolley;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.eaglesakura.gvolley.auth.AuthProvider;
import com.eaglesakura.gvolley.auth.GoogleAuthActivity;
import com.eaglesakura.gvolley.auth.Scopes;
import com.eaglesakura.gvolley.request.ProgressRequestListener;
import com.eaglesakura.gvolley.request.XmlRequest;
import com.eaglesakura.gvolley.spreadsheet.SpreadsheetDocumentList;
import com.eaglesakura.lib.android.game.util.LogUtil;
import com.eaglesakura.lib.io.XmlElement;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;

@EActivity
public class MainActivity extends Activity {

    private AuthProvider provider;

    RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        provider = new AuthProvider(this, "sptest");
        queue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        if (provider.isAuthorized()) {
            toast("authorized");
            loadSpreadsheets();
        } else {
            startAuth();
        }
    }

    @Override
    protected void onDestroy() {
        queue.stop();
        super.onDestroy();
    }

    @UiThread
    void startAuth() {
        Intent intent = GoogleAuthActivity.newIntent(this, GoogleAuthActivity.class,
                getString(R.string.oauth2_clientId), getString(R.string.oauth2_clientSecret), new String[] {
                    Scopes.SPREADSHEET.getURL()
                }, provider);
        startActivity(intent);
    }

    /**
     * Spreadsheet一覧を取得する
     */
    @UiThread
    void loadSpreadsheets() {
        String url = "https://spreadsheets.google.com/feeds/spreadsheets/private/full";
        ProgressRequestListener<XmlElement> dialog = new ProgressRequestListener<XmlElement>(this, queue) {

            @Override
            protected void onSuccess(XmlElement response) {
                LogUtil.log("success ::  " + response);

                SpreadsheetDocumentList documents = new SpreadsheetDocumentList(response);

                LogUtil.log("id :: " + documents.id);
                LogUtil.log("updated :: " + documents.updated.toString());
            }

            @Override
            protected void onError(VolleyError error) {
                LogUtil.log(error.getMessage());
            }
        };
        XmlRequest req = new XmlRequest(Request.Method.GET, url, dialog);
        provider.authorize(req, Scopes.SPREADSHEET);
        dialog.addRequestQueue(req).show();
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
