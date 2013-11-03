package com.eaglesakura.gvolley.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.eaglesakura.gvolley.R;
import com.eaglesakura.gvolley.auth.GoogleAuthActivity;
import com.eaglesakura.gvolley.auth.OAuthProvider;
import com.eaglesakura.gvolley.auth.Scopes;
import com.eaglesakura.gvolley.gdata.SpreadsheetHelper;
import com.eaglesakura.gvolley.gdata.model.Link;
import com.eaglesakura.gvolley.gdata.spreadsheet.CellEntry;
import com.eaglesakura.gvolley.gdata.spreadsheet.SheetCells;
import com.eaglesakura.gvolley.gdata.spreadsheet.SheetEntry;
import com.eaglesakura.gvolley.gdata.spreadsheet.SpreadsheetDocumentList;
import com.eaglesakura.gvolley.gdata.spreadsheet.Worksheet;
import com.eaglesakura.gvolley.gdata.spreadsheet.WorksheetEntry;
import com.eaglesakura.gvolley.request.listener.AuthorizedProgressRequestController;
import com.eaglesakura.lib.android.game.util.LogUtil;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OnActivityResult;
import com.googlecode.androidannotations.annotations.UiThread;

@EActivity
public class MainActivity extends Activity {

    private OAuthProvider provider;

    RequestQueue queue = null;

    SpreadsheetHelper spreadsheet;

    static final int REQUEST_ACCOUNT_AUTH = 0x3103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        provider = new OAuthProvider(this, "sptest", getString(R.string.oauth2_clientId),
                getString(R.string.oauth2_clientSecret));
        queue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        if (provider.isAuthorized()) {
            toast("authorized");
            spreadsheet = new SpreadsheetHelper(provider);
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
                    Scopes.Spreadsheet.getURL()
                }, provider);
        startActivity(intent);
        //        GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        //        Intent intent = provider.newAccountPickIntentFromPlayservice(this);
        //        startActivityForResult(intent, REQUEST_ACCOUNT_AUTH);
    }

    @Background
    void authFromPlayservice(String account) {
        try {
            provider.authFromPlayservice(this, account, new String[] {
                Scopes.Spreadsheet.getURL(),
            });
            loadSpreadsheets();
        } catch (Exception e) {
            LogUtil.log(e);
        }
    }

    @OnActivityResult(REQUEST_ACCOUNT_AUTH)
    void resultAuth(int result, Intent data) {
        String accoutName = provider.onAccountPickResult(result, data);
        if (accoutName != null) {
            authFromPlayservice(accoutName);
        }
    }

    /**
     * Spreadsheet一覧を取得する
     */
    @UiThread
    void loadSpreadsheets() {
        AuthorizedProgressRequestController<SpreadsheetDocumentList> dialog = new AuthorizedProgressRequestController<SpreadsheetDocumentList>(
                this, queue, provider) {

            @Override
            protected void onSuccess(SpreadsheetDocumentList documents) {
                LogUtil.log("id :: " + documents.getId());
                LogUtil.log("updated :: " + documents.getUpdated().toString());

                // シート一覧を取得する
                WorksheetEntry entry = documents.entries.get(0);
                LogUtil.log("title :: " + entry.getTitle());
                LogUtil.log("id :: " + entry.getId());
                LogUtil.log("worksheet :: " + entry.getWorksheetsUrl());

                loadWorksheet(entry);
            }

            @Override
            protected void onVolleyError(VolleyError error) {
                toast(error.getMessage());
            }
        };
        dialog.show().addRequestQueue(spreadsheet.listDocuments(dialog));
    }

    @UiThread
    void loadWorksheet(WorksheetEntry entry) {
        AuthorizedProgressRequestController<Worksheet> dialog = new AuthorizedProgressRequestController<Worksheet>(
                this, queue, provider) {
            @Override
            protected void onSuccess(Worksheet response) {
                LogUtil.log("title :: " + response.title);
                LogUtil.log("sheets :: " + response.entries.size());

                for (SheetEntry entry : response.entries) {
                    LogUtil.log(" sheet :: " + entry.getTitle());
                    for (Link link : entry.links) {
                        //                        LogUtil.log("  href :: " + link.href);
                    }
                }

                loadSheet(response.entries.get(0));
            }

            @Override
            protected void onVolleyError(VolleyError error) {
                toast(error.getMessage());
            }
        };
        dialog.show().addRequestQueue(spreadsheet.getWorksheet(dialog, entry.getKey()));
    }

    @UiThread
    void loadSheet(SheetEntry entry) {
        AuthorizedProgressRequestController<SheetCells> dialog = new AuthorizedProgressRequestController<SheetCells>(
                this, queue, provider) {

            @Override
            protected void onSuccess(SheetCells response) {
                LogUtil.log("title :: " + response.title);
                LogUtil.log(String.format("c%d / c%d", response.rows, response.cols));

                for (CellEntry entry : response.entries) {
                    LogUtil.log("title :: " + entry.title);
                    LogUtil.log("value :: " + entry.value.toString());
                }
            }

            @Override
            protected void onVolleyError(VolleyError error) {
                toast(error.getMessage());
            }
        };
        dialog.show().addRequestQueue(spreadsheet.getSheetCells(dialog, entry.getWorksheetId(), entry.getSheetId()));
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
